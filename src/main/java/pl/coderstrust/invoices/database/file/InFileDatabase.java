package pl.coderstrust.invoices.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;

@Repository
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "file")
public class InFileDatabase implements Database {

    private InvoiceFileAccessor fileAccessor;
    private InvoiceIdCoordinator idCoordinator;

    @Autowired
    InFileDatabase(InvoiceFileAccessor fileAccessor, InvoiceIdCoordinator idCoordinator) {
        this.fileAccessor = fileAccessor;
        this.idCoordinator = idCoordinator;
    }

    @Override
    public void saveInvoice(Invoice invoice) throws DatabaseOperationException {
        Long invoiceId = invoice.getId();

        try {
            Collection<Long> ids = idCoordinator.getIds();
            if (!invoiceId.equals(null) && !ids.contains(invoiceId)) {
                throw new DatabaseOperationException(invoiceNotExistingMessage(invoiceId)
                    + " Update failed.");
            }

            if (invoiceId.equals(null)) {
                invoiceId = new IdGenerator().generateId(ids);
                invoice = new Invoice(invoice, invoiceId);
            } else if (ids.contains(invoiceId)) {
                deleteInvoice(invoiceId);
            }

            String line = getLineFromInvoice(invoice);

            fileAccessor.saveLine(line);
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
        } catch (DatabaseOperationException e) {
            e.getMessage();
        }
    }

    @Override
    public Invoice getInvoice(Long invoiceId) throws DatabaseOperationException {
        ArrayList<Invoice> invoices = new ArrayList<>(getInvoices());

        for (Invoice invoice : invoices) {
            if (invoice.getId().equals(invoiceId)) {
                return invoice;
            }
        }

        if (true) {
            throw new DatabaseOperationException(
                invoiceNotExistingMessage(invoiceId));
        } else {
            return null;
        }
    }

    @Override
    public Collection<Invoice> getInvoices() throws DatabaseOperationException {
        ArrayList<Invoice> invoices = new ArrayList<>();

        try {
            ArrayList<String> lines = fileAccessor.readLines();
            invoices = new Converter().getInvoicesFromLines(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (invoices.size() == 0) {
            throw new DatabaseOperationException("No invoices in file database.");
        }
        return invoices;
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate)
        throws DatabaseOperationException {
        if (startDate.isAfter(endDate)) {
            throw new DatabaseOperationException(
                "Start date [" + startDate + "] is after end date [" + endDate + "].");
        }

        Collection<Invoice> invoices = new ArrayList<>(getInvoices()).stream()
            .filter(invoice -> invoice.getIssueDate().toEpochDay() >= startDate.toEpochDay())
            .filter(invoice -> invoice.getIssueDate().toEpochDay() <= endDate.toEpochDay())
            .collect(Collectors.toList());

        if (invoices.size() == 0) {
            throw new DatabaseOperationException(
                "No invoices between [" + startDate + "] - [" + endDate + "] in file - database.");
        }
        return invoices;
    }

    private String getLineFromInvoice(Invoice invoice) throws JsonProcessingException {
        return invoice.getId() + ": " + new Converter().getJsonFromInvoice(invoice);
    }

    private String invoiceNotExistingMessage(Long invoiceId) {
        return "Invoice id=[" + invoiceId + "] doesn't exists.";
    }
}
