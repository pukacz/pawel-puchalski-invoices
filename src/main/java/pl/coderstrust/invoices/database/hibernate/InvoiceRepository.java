package pl.coderstrust.invoices.database.hibernate;

import org.springframework.data.repository.CrudRepository;
import pl.coderstrust.invoices.model.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

}
