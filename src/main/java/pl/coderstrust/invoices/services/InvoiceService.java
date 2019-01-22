package pl.coderstrust.invoices.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;

@Service
public class InvoiceService implements InvoiceServiceInterface {

    private Database database;

    public InvoiceService(Database database) {
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

    public void saveInvoice(Long id, String issue, LocalDate issueDate, Company seller,
        Company buyer, List<InvoiceEntry> entries) throws DatabaseOperationException {
        try {
            Invoice invoice = new Invoice(id, issue, issueDate, seller, buyer, entries);
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
