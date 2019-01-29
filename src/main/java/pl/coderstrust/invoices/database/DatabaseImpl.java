package pl.coderstrust.invoices.database;

import java.time.LocalDate;
import java.util.Collection;
import pl.coderstrust.invoices.model.Invoice;

public class DatabaseImpl implements Database {

    @Override
    public void saveInvoice(Invoice invoice) throws DatabaseOperationException {

    }

    @Override
    public void deleteInvoice(Long id) throws DatabaseOperationException {

    }

    @Override
    public Invoice getInvoice(Long id) throws DatabaseOperationException {
        return null;
    }

    @Override
    public Collection<Invoice> getInvoices() throws DatabaseOperationException {
        return null;
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate)
        throws DatabaseOperationException {
        return null;
    }
}
