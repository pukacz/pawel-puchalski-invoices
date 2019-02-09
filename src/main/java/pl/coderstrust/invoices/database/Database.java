package pl.coderstrust.invoices.database;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import pl.coderstrust.invoices.model.Invoice;

public interface Database {

    void saveInvoice(Invoice invoice) throws DatabaseOperationException, IOException;

    void deleteInvoice(Long id) throws DatabaseOperationException;

    Invoice getInvoice(Long id) throws DatabaseOperationException;

    Collection<Invoice> getInvoices() throws DatabaseOperationException;

    Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate)
        throws DatabaseOperationException;
}
