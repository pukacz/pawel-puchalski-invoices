package pl.coderstrust.invoices.database;

import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;

import java.util.List;

public interface Database {

    void saveInvoice(Invoice invoice);

    void updateInvoiceById(Long id);

    void deleteInvoiceById(Long id);

    Invoice getInvoiceById(Long id);

    List<InvoiceEntry> getInvoices();
}
