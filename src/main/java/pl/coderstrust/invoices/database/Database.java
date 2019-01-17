package pl.coderstrust.invoices.database;

import java.time.LocalDate;
import java.util.Collection;
import pl.coderstrust.invoices.model.Invoice;

public interface Database {

    void saveInvoice(Invoice invoice);

    void deleteInvoice(Long id);

    Invoice getInvoice(Long id);

    Collection<Invoice> getInvoices();

    Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate);
}
