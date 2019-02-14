package pl.coderstrust.invoices.database.file;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.model.Invoice;

public class InFileDatabase implements Database {

    private InvoiceFileAccessor fileAccessor;
    private InvoiceIdCoordinator idCoordinator;

    InFileDatabase(InvoiceFileAccessor fileAccessor, InvoiceIdCoordinator idCoordinator) {
        this.fileAccessor = fileAccessor;
        this.idCoordinator = idCoordinator;
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        Long invoiceId = invoice.getId();

        try {
            if (invoiceId == null) {
                invoiceId = new IdGenerator().generateId(idCoordinator.getIds());
                invoice.setId(invoiceId);
            }

            if (idCoordinator.getIds().contains(invoiceId)) {
                fileAccessor.invalidateLine(invoiceId);
            }

            String invoiceInJson = new Converter().getJsonFromInvoice(invoice);
            fileAccessor.saveLine(invoiceId, invoiceInJson);
            idCoordinator.coordinateIds(invoiceId);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            ArrayList<String> lines = fileAccessor.getInvoiceFileLines();
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
}
