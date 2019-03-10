package pl.coderstrust.invoices.database.hibernate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;
import pl.coderstrust.invoices.model.StandardInvoice;

@Repository
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "hibernate")
public class HibernateDatabase implements Database {

    @Autowired
    private HibernateInvoiceRepository hibernateRepository;

    public HibernateDatabase() {
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new DatabaseOperationException("Invoice can not be null.");
        }
        StandardInvoice standardInvoice = new StandardInvoice(invoice);
        try {
            StandardInvoice result = hibernateRepository.save(standardInvoice);
            return new Invoice(result);
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Failed to add/update invoice", e);
        }
    }

    @Override
    public void deleteInvoice(Object id) throws DatabaseOperationException {
        Long invoiceId = getIdFromObject(id);
        if (id == null || invoiceId < 1) {
            throw new IllegalArgumentException(String.format("ID = [%d] can not be NULL or negative.", invoiceId));
        }
        try {
            hibernateRepository.deleteById(invoiceId);
        } catch (PersistenceException e) {
            throw new DatabaseOperationException(String.format("Failed to remove invoice with ID = [%d].", invoiceId), e);
        }
    }

    @Override
    public Invoice getInvoice(Object id) throws DatabaseOperationException {
        Long invoiceId = getIdFromObject(id);
        if (id == null || invoiceId < 1) {
            throw new IllegalArgumentException(String.format("ID = [%d] can not be NULL or negative.", invoiceId));
        }
        try {
            Optional<StandardInvoice> optionalInvoice = hibernateRepository.findById(invoiceId);
            if (optionalInvoice.isPresent()) {
                StandardInvoice standardInvoice = optionalInvoice.get();
                return new Invoice(standardInvoice);
            } else {
                throw new DatabaseOperationException(String.format("There is no invoice with this ID = [%d]", invoiceId));
            }
        } catch (PersistenceException e) {
            throw new DatabaseOperationException(String.format("Failed to find invoice with ID = [%d].", invoiceId), e);
        }
    }

    @Override
    public Collection<Invoice> getInvoices() throws DatabaseOperationException {
        try {
            return getAllInvoices();
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Failed to find invoices in database.", e);
        }
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate)
        throws DatabaseOperationException {

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

    private List<Invoice> getAllInvoices() throws DatabaseOperationException {
        try {
            Iterable<StandardInvoice> iterator = hibernateRepository.findAll();
            List<Invoice> invoiceList = new ArrayList<>();
            for (StandardInvoice invoice : iterator) {
                invoiceList.add(new Invoice(invoice));
            }
            return invoiceList;
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Failed to find invoices in database.", e);
        }
    }

    Long getIdFromObject(Object id) {
        if (id == null) {
            return null;
        }

        if (id instanceof String) {
            if (NumberUtils.isParsable((String) id)) {
                return Long.parseLong((String) id);
            } else {
                throw new IllegalArgumentException("Argument Id must be Long type.");
            }
        }

        if (!(id instanceof Integer) && !(id instanceof Long)) {
            throw new IllegalArgumentException("Argument Id must be Long type.");
        }

        if (id instanceof Integer) {
            return ((Integer) id).longValue();
        }
        return (Long) id;
    }
}
