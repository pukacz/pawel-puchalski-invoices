package pl.coderstrust.invoices.service;

import java.time.LocalDate;
import java.util.Collection;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;

public interface InvoiceService {

    Collection<Invoice> getAllInvoices()
        throws DatabaseOperationException;

    Collection<Invoice> getAllOfRange(LocalDate fromDate, LocalDate toDate)
        throws DatabaseOperationException;

    Invoice getInvoiceById(Object id) throws DatabaseOperationException;

    Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException;

    void deleteInvoice(Object id) throws DatabaseOperationException;
}
