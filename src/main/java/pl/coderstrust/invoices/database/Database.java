package pl.coderstrust.invoices.database;

import pl.coderstrust.invoices.model.Invoice;

import java.time.LocalDate;
import java.util.Collection;

public interface Database {

    void saveInvoice(Invoice invoice);

    void deleteInvoice(Long id);

    Invoice getInvoice(Long id);

    Collection<Invoice> getInvoices();

    Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate);
}
