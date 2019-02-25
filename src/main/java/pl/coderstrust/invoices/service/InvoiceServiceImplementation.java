package pl.coderstrust.invoices.service;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;

@Service
public class InvoiceServiceImplementation implements InvoiceService {

    private Database database;
    private String invoiceNotExistingMessage = "there is no invoice with this ID in the database";
    private String invoiceNullIdMessage = "Invoice ID must not be null.";

    @Autowired
    public InvoiceServiceImplementation(Database database) {
        this.database = database;
    }

    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        try {
            return database.getInvoices();
        } catch (Exception e) {
            throw new DatabaseOperationException("there are no invoices", e);
        }
    }

    public Collection<Invoice> getAllOfRange(LocalDate fromDate, LocalDate toDate)
        throws DatabaseOperationException {
        try {
            return database.getInvoicesByDate(fromDate, toDate);
        } catch (Exception e) {
            throw new DatabaseOperationException("there are no invoices in this data range", e);
        }
    }

    public Invoice getInvoiceById(Long id) throws DatabaseOperationException {
        if (id == null) {
            throw new DatabaseOperationException(invoiceNullIdMessage,
                new IllegalArgumentException());
        }
        try {
            return database.getInvoice(id);
        } catch (Exception e) {
            throw new DatabaseOperationException(invoiceNotExistingMessage, e);
        }
    }

    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        try {
            database.saveInvoice(invoice);
        } catch (Exception e) {
            throw new DatabaseOperationException(
                "can not add/update this invoice to/in the database", e);
        }
        return invoice;
    }

    public void deleteInvoice(Long id) throws DatabaseOperationException {
        if (id == null) {
            throw new DatabaseOperationException(invoiceNullIdMessage,
                new IllegalArgumentException());
        }
        try {
            database.deleteInvoice(id);
        } catch (Exception e) {
            throw new DatabaseOperationException(invoiceNotExistingMessage, e);
        }
    }
}
