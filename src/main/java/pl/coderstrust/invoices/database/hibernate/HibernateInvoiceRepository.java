package pl.coderstrust.invoices.database.hibernate;

import org.springframework.data.repository.CrudRepository;
import pl.coderstrust.invoices.model.HibernateInvoice;

interface HibernateInvoiceRepository extends CrudRepository<HibernateInvoice, Long> {
}
