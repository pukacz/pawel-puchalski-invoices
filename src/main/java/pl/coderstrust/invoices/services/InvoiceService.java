package pl.coderstrust.invoices.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import pl.coderstrust.invoices.Exceptions.InvoiceException;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;

@Service
public class InvoiceService implements InvoiceServiceInterface {

    private Database database;

    public InvoiceService(Database database) {
        this.database = database;
    }

    public Collection<Invoice> getAllInvoices() throws InvoiceException {
        try {
            return  database.getInvoices();
        } catch (Exception e) {
            throw new InvoiceException("there are no invoices", e);
        }
    }

    public Collection<Invoice> getAllofRange(LocalDate fromDate, LocalDate toDate) throws InvoiceException {
        try {
            return  database.getInvoicesByDate(fromDate, toDate);
        } catch (Exception e) {
            throw new InvoiceException("there are no invoices in this data range", e);
        }
    }

    public Invoice getInvoiceByID(Long id) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null", new IllegalArgumentException());
        }
        try {
            return  database.getInvoice(id);
        } catch (Exception e) {
            throw new InvoiceException("there are no invoice with this ID in the database", e);
        }
    }

    @JsonCreator
    public void addInvoice(@JsonProperty("issue") String issue, @JsonProperty("issueDate") LocalDate issueDate, @JsonProperty("seller") Company seller,
        @JsonProperty("buyer") Company buyer, @JsonProperty("entries") List<InvoiceEntry> entries) throws InvoiceException {
        try {
            Invoice invoice = new Invoice(0L, issue, issueDate, seller, buyer, entries);
            return  database.saveInvoice(invoice);
        } catch (Exception e) {
            throw new InvoiceException("can not add an invoice to the database", e);
        }
    }

    @JsonCreator
    public Invoice updateInvoice(@JsonProperty("id") Long id, @JsonProperty("issue") String issue, @JsonProperty("issueDate") LocalDate issueDate, @JsonProperty("seller") Company seller,
        @JsonProperty("buyer") Company buyer, @JsonProperty("entries") List<InvoiceEntry> entries) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null", new IllegalArgumentException());
        }
        try {
            Invoice invoice = new Invoice(id, issue, issueDate, seller, buyer, entries);
            return  database.saveInvoice(invoice);
        } catch (Exception e) {
            throw new InvoiceException("can not update an invoice to the database", e);
        }
    }

    public void deleteInvoice(Long id) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null", new IllegalArgumentException());
        }
        try {
            return  database.deleteInvoice(id);
        } catch (Exception e) {
            throw new InvoiceException("there are no invoice with this ID in the database", e);
        }
    }

}
