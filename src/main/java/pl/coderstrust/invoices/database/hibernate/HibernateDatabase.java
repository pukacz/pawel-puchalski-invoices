package pl.coderstrust.invoices.database.hibernate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.persistence.PersistenceException;

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
    private InvoiceRepository repository;

    public HibernateDatabase(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new DatabaseOperationException("StandardInvoice can not be null.");
        }
        StandardInvoice standardInvoice = new StandardInvoice(invoice);
        try {
            StandardInvoice result = repository.save(standardInvoice);
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
            repository.deleteById(invoiceId);
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
            Optional<StandardInvoice> optionalInvoice = repository.findById(invoiceId);
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
        if (startDate == null || endDate == null) {
            throw new DatabaseOperationException("Dates can not be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new DatabaseOperationException("start date can not be higer than end date");
        }
        try {
            List<Invoice> invoiceList = getAllInvoices();
            List<Invoice> invoiceByDateList = new ArrayList<>();
            for (Invoice invoice : invoiceList) {
                if (invoice.getIssueDate().isAfter(startDate) || invoice.getIssueDate().isEqual(startDate)) {
                    invoiceByDateList.add(invoice);
                }
                if (invoice.getIssueDate().isAfter(endDate)) {
                    break;
                }
            }
            return invoiceByDateList;
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Failed to find invoices in database.", e);
        }
    }

    private List<Invoice> getAllInvoices() throws DatabaseOperationException {
        try {
            Iterable<StandardInvoice> iterator = repository.findAll();
            List<Invoice> invoiceList = new ArrayList<>();
            for (StandardInvoice invoice : iterator) {
                invoiceList.add(new Invoice(invoice));
            }
            return invoiceList;
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Failed to find invoices in database.", e);
        }
    }

    private Long getIdFromObject(Object id) {
        if (id == null) {
            return null;
        }
        if (id instanceof String) {
            return Long.parseLong((String) id);
        }

        if (!(id instanceof String) && !(id instanceof Integer) && !(id instanceof Long)) {
            throw new IllegalArgumentException("Argument Id must be Long type.");
        }

        return (Long) id;
    }

}
