package pl.coderstrust.invoices.services;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import pl.coderstrust.invoices.model.Company;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.InvoiceEntry;

public interface InvoiceService {

    Collection<Invoice> getAllInvoices() throws DatabaseOperationException, DatabaseOperationException;

    Collection<Invoice> getAllofRange(LocalDate fromDate, LocalDate toDate)
        throws DatabaseOperationException;

    Invoice getInvoiceByID(Long id) throws DatabaseOperationException;

    void saveInvoice(@RequestBody Invoice invoice) throws DatabaseOperationException;

    void deleteInvoice(Long id) throws DatabaseOperationException;
}
