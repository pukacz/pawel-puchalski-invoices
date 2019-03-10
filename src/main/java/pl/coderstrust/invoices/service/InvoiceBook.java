package pl.coderstrust.invoices.service;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;

@Service
public class InvoiceBook implements InvoiceService {

    private static final String INVOICE_ID_MUST_NOT_BE_NULL = "Invoice ID must not be null.";
    private static final String INVOICE_MUST_NOT_BE_NULL = "Invoice must not be null.";
    private Database database;

    @Autowired
    public InvoiceBook(Database database) {
        this.database = database;
    }

    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        try {
            return database.getInvoices();
        } catch (Exception e) {
            // TODO errors should be logged here
            throw e;
        }
    }

    public Collection<Invoice> getAllOfRange(LocalDate fromDate, LocalDate toDate)
        throws DatabaseOperationException {
        if (fromDate == null) {
            throw new IllegalArgumentException("From date must not be null.");
        }
        if (toDate == null) {
            throw new IllegalArgumentException("To date must not be null.");
        }
        try {
            return database.getInvoicesByDate(fromDate, toDate);
        } catch (Exception e) {
            // TODO errors should be logged here
            throw e;
        }
    }

    public Invoice getInvoiceById(Object id) throws DatabaseOperationException {
        if (id == null) {
            throw new IllegalArgumentException(INVOICE_ID_MUST_NOT_BE_NULL);
        }
        try {
            return database.getInvoice(id);
        } catch (Exception e) {
            // TODO errors should be logged here
            throw e;
        }
    }

    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new IllegalArgumentException(INVOICE_MUST_NOT_BE_NULL);
        }
        try {
            return database.saveInvoice(invoice);
        } catch (Exception e) {
            // TODO errors should be logged here
            throw e;
        }
    }

    public void deleteInvoice(Object id) throws DatabaseOperationException {
        if (id == null) {
            throw new IllegalArgumentException(INVOICE_ID_MUST_NOT_BE_NULL);
        }
        try {
            database.deleteInvoice(id);
        } catch (Exception e) {
            // TODO errors should be logged here
            throw e;
        }
    }
}
