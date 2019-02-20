package pl.coderstrust.invoices.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.model.Invoice;

@Repository
public class InFileDatabase implements Database {

    private InvoiceFileAccessor fileAccessor;
    private InvoiceIdCoordinator idCoordinator;

    InFileDatabase(InvoiceFileAccessor fileAccessor, InvoiceIdCoordinator idCoordinator) {
        this.fileAccessor = fileAccessor;
        this.idCoordinator = idCoordinator;
    }

    @Override
    public Long saveInvoice(Invoice invoice) {
        Long invoiceId = invoice.getId();

        try {
            if (invoiceId == null) {
                invoiceId = new IdGenerator().generateId(idCoordinator.getIds());
                invoice = new Invoice(invoice, invoiceId);
            } else if (idCoordinator.getIds().contains(invoiceId)) {
                deleteInvoice(invoiceId);
            }

            String line = getLineFromInvoice(invoice);

            fileAccessor.saveLine(line);
            idCoordinator.coordinateIds(invoiceId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoiceId;
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        try {
            fileAccessor.invalidateLine(invoiceId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Invoice getInvoice(Long invoiceId) {
        ArrayList<Invoice> invoices = new ArrayList<>(getInvoices());

        for (Invoice invoice : invoices) {
            if (invoice.getId().equals(invoiceId)) {
                return invoice;
            }
        }
        return null;
    }

    @Override
    public Collection<Invoice> getInvoices() {
        ArrayList<Invoice> invoices = new ArrayList<>();

        try {
            ArrayList<String> lines = fileAccessor.readLines();
            invoices = new Converter().getInvoicesFromLines(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate) {
        return new ArrayList<>(getInvoices()).stream()
            .filter(invoice -> invoice.getIssueDate().toEpochDay() >= startDate.toEpochDay())
            .filter(invoice -> invoice.getIssueDate().toEpochDay() <= endDate.toEpochDay())
            .collect(Collectors.toList());
    }

    private String getLineFromInvoice(Invoice invoice) throws JsonProcessingException {
        return invoice.getId() + ": " + new Converter().getJsonFromInvoice(invoice);
    }
}
