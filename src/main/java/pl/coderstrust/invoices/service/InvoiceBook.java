package pl.coderstrust.invoices.service;

import java.time.LocalDate;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;

@Service
public class InvoiceBook implements InvoiceService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        } catch (Exception exception) {
            logger.error("Failed to obtain invoices from database. {}", exception.getLocalizedMessage());
            throw exception;
        }
    }

    public Collection<Invoice> getAllOfRange(LocalDate fromDate, LocalDate toDate)
        throws DatabaseOperationException {
        if (fromDate == null) {
            IllegalArgumentException exception = new IllegalArgumentException(
                "From date must not be null.");
            logger.error("Failed to obtain invoices. {}", exception.getLocalizedMessage());
            throw exception;
        }
        if (toDate == null) {
            IllegalArgumentException exception = new IllegalArgumentException(
                "To date must not be null.");
            logger.error("Failed to obtain invoices. {}", exception.getLocalizedMessage());
            throw exception;
        }
        try {
            return database.getInvoicesByDate(fromDate, toDate);
        } catch (Exception exception) {
            logger.error("Failed to obtain invoices from database. {}", exception.getLocalizedMessage());
            throw exception;
        }
    }

    public Invoice getInvoiceById(Object id) throws DatabaseOperationException {
        if (id == null) {
            IllegalArgumentException exception = new IllegalArgumentException(
                INVOICE_ID_MUST_NOT_BE_NULL);
            logger.error("Failed to get invoice. {}", exception.getLocalizedMessage());
            throw exception;
        }
        try {
            return database.getInvoice(id);
        } catch (Exception exception) {
            logger.error("Failed to obtain invoice with id=[{}] from database. {}", id, exception.getLocalizedMessage());
            throw exception;
        }
    }

    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            IllegalArgumentException exception = new IllegalArgumentException(
                INVOICE_MUST_NOT_BE_NULL);
            logger.error("Failed to save invoice. {}", exception.getLocalizedMessage());
            throw exception;
        }
        try {
            return database.saveInvoice(invoice);
        } catch (Exception exception) {
            logger.error("Failed to save invoice {}. {}", invoice, exception.getLocalizedMessage());
            throw exception;
        }
    }

    public void deleteInvoice(Object id) throws DatabaseOperationException {
        if (id == null) {
            IllegalArgumentException exception = new IllegalArgumentException(
                INVOICE_ID_MUST_NOT_BE_NULL);
            logger.error("Failed to save invoice. {}", exception.getLocalizedMessage());
            throw exception;
        }
        try {
            database.deleteInvoice(id);
        } catch (Exception exception) {
            logger.error("Failed to delete invoice with id = [{}]. {}", id, exception.getLocalizedMessage());
            throw exception;
        }
    }
}
