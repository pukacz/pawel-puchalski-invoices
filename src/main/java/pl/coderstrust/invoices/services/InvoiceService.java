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

    public Collection<Invoice> getAllInvoices() throws InvoiceException {
        return new ArrayList<>();
    }

    public Collection<Invoice> getAllofRange(LocalDate fromDate, LocalDate toDate) throws InvoiceException {
        return new ArrayList<>();
    }

    public Invoice getInvoiceByID(Long id) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null");
        }
        return null;
    }

    public void addInvoice(Long id, String issue, LocalDate issueDate, Company seller,
        Company buyer, List<InvoiceEntry> entries) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null");
        }

    }

    public Invoice updateInvoice(Long id) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null");
        }
        return null;
    }

    public void deleteInvoice(Long id) throws InvoiceException {
        if (id <= 0 || id == null) {
            throw new InvoiceException("invoice ID can not be negative or null");
        }
    }

}
