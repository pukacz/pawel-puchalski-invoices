package pl.coderstrust.invoices.services;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;

public interface InvoiceServiceInterface {

    Collection<Invoice> getAllInvoices() throws DatabaseOperationException, DatabaseOperationException;

    Collection<Invoice> getAllofRange(LocalDate fromDate, LocalDate toDate)
        throws DatabaseOperationException;

    Invoice getInvoiceByID(Long id) throws DatabaseOperationException;

    void saveInvoice(String issue, LocalDate issueDate, Company seller, Company buyer,
        List<InvoiceEntry> entries) throws DatabaseOperationException;

    void deleteInvoice(Long id) throws DatabaseOperationException;
}
