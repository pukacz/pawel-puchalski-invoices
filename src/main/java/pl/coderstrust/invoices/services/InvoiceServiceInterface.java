package pl.coderstrust.invoices.services;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import pl.coderstrust.invoices.Exceptions.InvoiceException;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;

public interface InvoiceServiceInterface {

    Collection<Invoice> getAllInvoices() throws InvoiceException;

    Collection<Invoice> getAllofRange(LocalDate fromDate, LocalDate toDate)
        throws InvoiceException;

    Invoice getInvoiceByID(Long id) throws InvoiceException;

    void addInvoice(String issue, LocalDate issueDate, Company seller, Company buyer,
        List<InvoiceEntry> entries) throws InvoiceException;

    Invoice updateInvoice(Long id, String issue, LocalDate issueDate,
        Company seller, Company buyer,
        List<InvoiceEntry> entries) throws InvoiceException;

    void deleteInvoice(Long id) throws InvoiceException;
}
