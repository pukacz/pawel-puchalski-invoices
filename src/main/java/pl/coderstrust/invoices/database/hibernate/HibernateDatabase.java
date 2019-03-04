package pl.coderstrust.invoices.database.hibernate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.model.Invoice;

@Repository
public class HibernateDatabase implements Database {

    @Autowired
    private InvoiceRepository repository;

    public HibernateDatabase(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        Optional<Invoice> optionalInvoice = repository.findById(invoice.getId());
        if (optionalInvoice.isPresent()) {
            deleteInvoice(optionalInvoice.get().getId());
            repository.save(invoice);
        } else {
            repository.save(invoice);
        }
        return repository.save(invoice);
    }

    @Override
    public void deleteInvoice(Long id) throws DatabaseOperationException {
        repository.deleteById(id);
    }

    @Override
    public Invoice getInvoice(Long id) throws DatabaseOperationException {
        Optional<Invoice> optionalInvoice = repository.findById(id);
        if (optionalInvoice.isPresent()) {
            return optionalInvoice.get();
        } else {
            throw new DatabaseOperationException("there is no invoice with this ID");
        }
    }

    @Override
    public Collection<Invoice> getInvoices() throws DatabaseOperationException {
        return getAllInvoices();
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate)
            throws DatabaseOperationException {
        if (startDate.isAfter(endDate)) {
            throw new DatabaseOperationException("start date can not be higer than end date");
        }
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
    }

    private List<Invoice> getAllInvoices() {
        Iterable<Invoice> iterator = repository.findAll();
        List<Invoice> invoiceList = new ArrayList<>();
        for (Invoice invoice : iterator) {
            invoiceList.add(invoice);
        }
        return invoiceList;
    }

}
