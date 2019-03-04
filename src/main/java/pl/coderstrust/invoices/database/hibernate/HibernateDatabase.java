package pl.coderstrust.invoices.database.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateDatabase implements Database {

    @Autowired
    private InvoiceRepository repository;

    public HibernateDatabase(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new DatabaseOperationException("Invoice can not be null.");
        }
        try {
            Optional<Invoice> optionalInvoice = repository.findById(invoice.getId());
            if (optionalInvoice.isPresent()) {
                deleteInvoice(optionalInvoice.get().getId());
                return repository.save(invoice);
            }
            return repository.save(invoice);
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Failed to add/update invoice", e);
        }
    }

    @Override
    public void deleteInvoice(Long id) throws DatabaseOperationException {
        if (id == null || id < 1) {
            throw new IllegalArgumentException(String.format("ID = [%d] can not be NULL or negative.", id));
        }
        try {
            repository.deleteById(id);
        } catch (PersistenceException e) {
            throw new DatabaseOperationException(String.format("Failed to remove invoice with ID = [%d].", id), e);
        }
    }

    @Override
    public Invoice getInvoice(Long id) throws DatabaseOperationException {
        if (id == null || id < 1) {
            throw new IllegalArgumentException(String.format("ID = [%d] can not be NULL or negative.", id));
        }
        try {
            Optional<Invoice> optionalInvoice = repository.findById(id);
            if (optionalInvoice.isPresent()) {
                return optionalInvoice.get();
            } else {
                throw new DatabaseOperationException(String.format("There is no invoice with this ID = [%d]", id));
            }
        } catch (PersistenceException e) {
            throw new DatabaseOperationException(String.format("Failed to find invoice with ID = [%d].", id), e);
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
            Iterable<Invoice> iterator = repository.findAll();
            List<Invoice> invoiceList = new ArrayList<>();
            for (Invoice invoice : iterator) {
                invoiceList.add(invoice);
            }
            return invoiceList;
        } catch (PersistenceException e) {
            throw new DatabaseOperationException("Failed to find invoices in database.", e);
        }
    }

}
