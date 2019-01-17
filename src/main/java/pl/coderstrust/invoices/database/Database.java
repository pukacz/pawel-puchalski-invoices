package pl.coderstrust.invoices.database;

import pl.coderstrust.invoices.model.Invoice;

import java.time.LocalDate;
import java.util.Collection;

public interface Database {

    void saveInvoice(Invoice invoice) throws DatabaseOperationException;

    void deleteInvoice(Long id) throws DatabaseOperationException;

    Invoice getInvoice(Long id) throws DatabaseOperationException;

    Collection<Invoice> getInvoices() throws DatabaseOperationException;

    Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate)
            throws DatabaseOperationException;
}
