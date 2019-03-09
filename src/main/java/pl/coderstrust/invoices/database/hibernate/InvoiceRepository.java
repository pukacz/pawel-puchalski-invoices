package pl.coderstrust.invoices.database.hibernate;

import org.springframework.data.repository.CrudRepository;
import pl.coderstrust.invoices.model.StandardInvoice;

public interface InvoiceRepository extends CrudRepository<StandardInvoice, Long> {

}
