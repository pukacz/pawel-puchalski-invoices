package pl.coderstrust.invoices.services;

import java.time.LocalDate;
import java.util.Collection;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;

public interface InvoiceService {

    Collection<Invoice> getAllInvoices()
        throws DatabaseOperationException;

    Collection<Invoice> getAllofRange(LocalDate fromDate, LocalDate toDate)
        throws DatabaseOperationException;

    Invoice getInvoiceById(Long id) throws DatabaseOperationException;

    void saveInvoice(Invoice invoice) throws DatabaseOperationException;

    void deleteInvoice(Long id) throws DatabaseOperationException;
}
