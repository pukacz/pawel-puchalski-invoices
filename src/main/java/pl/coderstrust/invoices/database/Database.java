package pl.coderstrust.invoices.database;

import java.time.LocalDate;
import java.util.Collection;
import pl.coderstrust.invoices.model.Invoice;

public interface Database {

    Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException;

    void deleteInvoice(Object id) throws DatabaseOperationException;

    Invoice getInvoice(Object id) throws DatabaseOperationException;

    Collection<Invoice> getInvoices() throws DatabaseOperationException;

    Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate)
        throws DatabaseOperationException;
}
