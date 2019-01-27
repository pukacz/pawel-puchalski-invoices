package pl.coderstrust.invoices.services;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.coderstrust.invoices.model.Invoice;

@Service
public class InvoiceServiceImplementation implements InvoiceService {

    private Database database;

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

    public Collection<Invoice> getAllofRange(LocalDate fromDate, LocalDate toDate)
        throws DatabaseOperationException {
        try {
            return database.getInvoicesByDate(fromDate, toDate);
        } catch (Exception e) {
            throw new DatabaseOperationException("there are no invoices in this data range", e);
        }
    }

    public Invoice getInvoiceByID(Long id) throws DatabaseOperationException {
        if (id <= 0 || id == null) {
            throw new DatabaseOperationException("invoice ID can not be negative or null",
                new IllegalArgumentException());
        }
        try {
            return database.getInvoice(id);
        } catch (Exception e) {
            throw new DatabaseOperationException(
                "there are no invoice with this ID in the database", e);
        }
    }

    public void saveInvoice(@RequestBody Invoice invoice) throws DatabaseOperationException {
        try {
            database.saveInvoice(invoice);
        } catch (Exception e) {
            throw new DatabaseOperationException("can not add an invoice to the database", e);
        }
    }

    public void deleteInvoice(Long id) throws DatabaseOperationException {
        if (id <= 0 || id == null) {
            throw new DatabaseOperationException("invoice ID can not be negative or null",
                new IllegalArgumentException());
        }
        try {
            database.deleteInvoice(id);
        } catch (Exception e) {
            throw new DatabaseOperationException(
                "there are no invoice with this ID in the database", e);
        }
    }

}
