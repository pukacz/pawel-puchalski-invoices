package pl.coderstrust.invoices.database.mongo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.MongoInvoice;

@Repository
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "mongo")
public class MongoDatabase implements Database {

    private static final String INVOICE_ID_NOT_NULL_MSG = "Invoice Id must not be null.";
    private static final String INVOICE_NOT_EXISTING_MSG = "Invoice Id=[%s] doesn't exist.";

    @Autowired
    private MongoInvoiceRepository repository;

    @Autowired
    public MongoDatabase() {
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null.");
        }
        MongoInvoice mongoInvoice = new MongoInvoice(invoice);

        if ((mongoInvoice.getId() != null) && (!repository.findById(mongoInvoice.getId()).isPresent())) {
            throw new DatabaseOperationException(String.format(INVOICE_NOT_EXISTING_MSG, invoice.getId()));
        }
        MongoInvoice save = repository.save(mongoInvoice);
        return new Invoice(save);
    }

    @Override
    public void deleteInvoice(Object id) throws DatabaseOperationException {
        String invoiceId = getIdFromObject(id);
        checkIds(id, invoiceId);
        repository.deleteById(invoiceId);
    }

    @Override
    public Invoice getInvoice(Object id) throws DatabaseOperationException {
        String invoiceId = getIdFromObject(id);
        Optional<MongoInvoice> optionalMongoInvoice = checkIds(id, invoiceId);
        return new Invoice(optionalMongoInvoice.get());
    }

    @Override
    public Collection<Invoice> getInvoices() throws DatabaseOperationException {
        List<MongoInvoice> list = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(list::add);
        return list.stream().map(Invoice::new).collect(Collectors.toList());
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate) throws DatabaseOperationException {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date must not be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new DatabaseOperationException(
                "Start date [" + startDate + "] is after end date [" + endDate + "].");
        }

        return new ArrayList<>(getInvoices()).stream()
            .filter(invoice -> invoice.getIssueDate().toEpochDay() >= startDate.toEpochDay())
            .filter(invoice -> invoice.getIssueDate().toEpochDay() <= endDate.toEpochDay())
            .collect(Collectors.toList());
    }

    private String getIdFromObject(Object id) {
        if (id == null) {
            return null;
        }

        if (!(id instanceof String) && !(id instanceof Integer) && !(id instanceof Long)) {
            throw new IllegalArgumentException("Argument Id must be String type.");
        }

        if ((id instanceof Integer) || (id instanceof Long)) {
            return String.valueOf(id);
        }
        return (String) id;
    }

    private Optional<MongoInvoice> checkIds(Object id, String invoiceId) throws DatabaseOperationException {
        if ((id == null) || (invoiceId == null)) {
            throw new IllegalArgumentException(INVOICE_ID_NOT_NULL_MSG);
        }

        Optional<MongoInvoice> optionalInvoice = repository.findById(invoiceId);

        if (!optionalInvoice.isPresent()) {
            throw new DatabaseOperationException(String.format(INVOICE_NOT_EXISTING_MSG, invoiceId));
        }
        return optionalInvoice;
    }
}
