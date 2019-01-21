package pl.coderstrust.invoices.services;

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
            return  database.findAll();
        } catch (Exception e) {
            throw new InvoiceException("there are no invoices", e);
        }
    }

    public Collection<Invoice> getAllofRange(LocalDate fromDate, LocalDate toDate) throws InvoiceException {
        try {
            return  database.getAllofRange();
        } catch (Exception e) {
            throw new InvoiceException("there are no invoices in this data range", e);
        }
    }

    public Invoice getInvoiceByID(Long id) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null", new IllegalArgumentException());
        }
        try {
            return  database.getInvoiceByID();
        } catch (Exception e) {
            throw new InvoiceException("there are no invoice with this ID in the database", e);
        }
    }

    public void addInvoice(Long id, String issue, LocalDate issueDate, Company seller,
        Company buyer, List<InvoiceEntry> entries) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null", new IllegalArgumentException());
        }
        try {
            return  database.addInvoice();
        } catch (Exception e) {
            throw new InvoiceException("can not add an invoice to the database", e);
        }
    }

    public Invoice updateInvoice(Long id) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null", new IllegalArgumentException());
        }
        try {
            return  database.updateInvoice();
        } catch (Exception e) {
            throw new InvoiceException("there are no invoice with this ID in the database", e);
        }
    }

    public void deleteInvoice(Long id) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null", new IllegalArgumentException());
        }
        try {
            return  database.getAllofRange();
        } catch (Exception e) {
            throw new InvoiceException("there are no invoice with this ID in the database", e);
        }
    }

}
