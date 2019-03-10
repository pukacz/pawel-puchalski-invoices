package pl.coderstrust.invoices.database.memory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.database.IdGenerator;
import pl.coderstrust.invoices.model.Invoice;

@Repository
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "memory")
public class InMemoryDatabase implements Database {

    private static final String INVOICE_NOT_EXISTING_MSG = "Invoice id=[%d] doesn't exist.";
    private static final String INVOICE_ID_NOT_NULL_MSG = "Invoice Id must not be null.";
    private static final String ARGUMENT_ID_MUST_BE_LONG_TYPE_MSG = "Argument id must be long type";
    private HashMap<Long, Invoice> inMemoryDatabase;
    private IdGenerator idGenerator;

    @Autowired
    InMemoryDatabase(IdGenerator idGenerator) {
        inMemoryDatabase = new HashMap<>();
        this.idGenerator = idGenerator;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null.");
        }
        if (!(invoice.getId() == null) && !(invoice.getId() instanceof Long)) {
            throw new DatabaseOperationException(ARGUMENT_ID_MUST_BE_LONG_TYPE_MSG);
        }
        Long invoiceId = (Long) invoice.getId();
        if (invoiceId == null) {
            invoiceId = idGenerator.generateId(inMemoryDatabase.keySet());
            invoice = new Invoice(invoice, invoiceId);
        }
        synchronized (this) {
            inMemoryDatabase.put(invoiceId, invoice);
        }
        return invoice;
    }

    @Override
    public synchronized void deleteInvoice(Object invoiceId) throws DatabaseOperationException {
        if (invoiceId == null) {
            throw new IllegalArgumentException(INVOICE_ID_NOT_NULL_MSG);
        }
        if (!(invoiceId instanceof Long)) {
            throw new IllegalArgumentException(ARGUMENT_ID_MUST_BE_LONG_TYPE_MSG);
        }
        Long id = (Long) invoiceId;
        if (inMemoryDatabase.containsKey(id)) {
            inMemoryDatabase.remove(id);
        } else {
            throw new DatabaseOperationException(String.format(INVOICE_NOT_EXISTING_MSG, id));
        }
    }

    @Override
    public Invoice getInvoice(Object invoiceId) throws DatabaseOperationException {
        if (invoiceId == null) {
            throw new IllegalArgumentException(INVOICE_ID_NOT_NULL_MSG);
        }
        if (!(invoiceId instanceof Long)) {
            throw new IllegalArgumentException(ARGUMENT_ID_MUST_BE_LONG_TYPE_MSG);
        }
        Long id = (Long) invoiceId;
        if (inMemoryDatabase.containsKey(id)) {
            return inMemoryDatabase.get(id);
        } else {
            throw new DatabaseOperationException(String.format(INVOICE_NOT_EXISTING_MSG, id));
        }
    }

    @Override
    public Collection<Invoice> getInvoices() {
        return inMemoryDatabase.values();
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate) throws IllegalArgumentException {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date must not be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date [" + startDate + "] is after end date [" + endDate + "].");
        }
        return new ArrayList<>(getInvoices()).stream()
            .filter(invoice -> invoice.getIssueDate().toEpochDay() >= startDate.toEpochDay())
            .filter(invoice -> invoice.getIssueDate().toEpochDay() <= endDate.toEpochDay())
            .collect(Collectors.toList());
    }
}
