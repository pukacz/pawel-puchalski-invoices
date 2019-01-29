package pl.coderstrust.invoices.database;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.stereotype.Component;
import pl.coderstrust.invoices.model.Invoice;

@Component
public class DatabaseImpl implements Database {

    @Override
    public void saveInvoice(Invoice invoice) throws DatabaseOperationException {
        System.out.println("dane zapisywanie");

    }

    @Override
    public void deleteInvoice(Long id) throws DatabaseOperationException {
        System.out.println("udane kasowanie");
    }

    @Override
    public Invoice getInvoice(Long id) throws DatabaseOperationException {
        System.out.println("udane pobranie");
        return null;
    }

    @Override
    public Collection<Invoice> getInvoices() throws DatabaseOperationException {
        System.out.println("udane pobranie całości");
        return null;
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate)
        throws DatabaseOperationException {
        System.out.println("udane pobranie po dacie");
        return null;
    }
}
