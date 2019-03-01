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
            throw new DatabaseOperationException("Invoice must not be null");
        }
        Long invoiceId = invoice.getId();
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
    public synchronized void deleteInvoice(Long invoiceId) throws DatabaseOperationException {
        if (inMemoryDatabase.containsKey(invoiceId)) {
            inMemoryDatabase.remove(invoiceId);
        } else {
            throw new DatabaseOperationException(String.format("Failed to remove invoice. Invoice for id=[%d] doesn't exist.", invoiceId));
        }
    }

    @Override
    public Invoice getInvoice(Long invoiceId) throws DatabaseOperationException {
        if (inMemoryDatabase.containsKey(invoiceId)) {
            return inMemoryDatabase.get(invoiceId);
        } else {
            throw new DatabaseOperationException(String.format("Failed to get invoice. Invoice for id=[%d] doesn't exist.", invoiceId));
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
            throw new IllegalArgumentException(
                "Start date [" + startDate + "] is after end date [" + endDate + "].");
        }
        return new ArrayList<>(getInvoices()).stream()
            .filter(invoice -> invoice.getIssueDate().toEpochDay() >= startDate.toEpochDay())
            .filter(invoice -> invoice.getIssueDate().toEpochDay() <= endDate.toEpochDay())
            .collect(Collectors.toList());
    }
}
