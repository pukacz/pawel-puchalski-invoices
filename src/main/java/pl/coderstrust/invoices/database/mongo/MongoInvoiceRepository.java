package pl.coderstrust.invoices.database.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.coderstrust.invoices.model.MongoInvoice;

interface MongoInvoiceRepository extends MongoRepository<MongoInvoice, String> {
}
