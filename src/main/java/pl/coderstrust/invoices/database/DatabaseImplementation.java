package pl.coderstrust.invoices.database;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.model.Invoice;

@Primary
@Repository
public class DatabaseImplementation implements Database {

    @Override
    public Long saveInvoice(Invoice invoice) throws DatabaseOperationException {
        return invoice.getId();
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
