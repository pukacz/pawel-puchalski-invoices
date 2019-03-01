package pl.coderstrust.invoices.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.database.IdGenerator;
import pl.coderstrust.invoices.database.JsonConverter;
import pl.coderstrust.invoices.model.Invoice;

@Repository
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "file")
public class InFileDatabase implements Database {

    private static final String INVOICE_NOT_EXISTING_MSG = "Invoice id=[%d] doesn't exist.";
    private static final String DATABASE_CORRUPTED_MSG =
        "You are trying to read/update invoice which is "
            + "not recognized in coordination file. Please synchronize database files first.";
    private static final String INVOICE_ID_NOT_NULL_MSG = "Invoice Id must not be null.";
    private InvoiceFileAccessor fileAccessor;
    private InvoiceIdCoordinator idCoordinator;
    private IdGenerator idGenerator;

    @Autowired
    InFileDatabase(InvoiceFileAccessor fileAccessor, InvoiceIdCoordinator idCoordinator, IdGenerator idGenerator) {
        this.fileAccessor = fileAccessor;
        this.idCoordinator = idCoordinator;
        this.idGenerator = idGenerator;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null.");
        }
        Long invoiceId = invoice.getId();

        try {
            Collection<Long> ids = idCoordinator.getIds();

            if (invoiceId == null) {
                invoiceId = idGenerator.generateId(ids);
                invoice = new Invoice(invoice, invoiceId);
            } else {
                if (ids.contains(invoiceId)) {
                    deleteInvoice(invoiceId);
                } else {
                    throw new DatabaseOperationException(DATABASE_CORRUPTED_MSG);
                }
            }
            fileAccessor.saveLine(getLineFromInvoice(invoice));
            idCoordinator.coordinateIds(invoiceId);
        } catch (IOException e) {
            throw new DatabaseOperationException("Unable to save invoice.", e);
        }
        return invoice;
    }

    @Override
    public void deleteInvoice(Long invoiceId) throws DatabaseOperationException {
        if (invoiceId == null) {
            throw new IllegalArgumentException(INVOICE_ID_NOT_NULL_MSG);
        }

        try {
            if (!fileAccessor.invalidateLine(invoiceId)) {
                throw new DatabaseOperationException(
                    "Invoice id=[" + invoiceId + "] doesn't exist.");
            }
            idCoordinator.removeId(invoiceId);
        } catch (IOException e) {
            throw new DatabaseOperationException("Unable to delete invoice.", e);
        }
    }

    @Override
    public Invoice getInvoice(Long invoiceId) throws DatabaseOperationException {
        if (invoiceId == null) {
            throw new IllegalArgumentException(INVOICE_ID_NOT_NULL_MSG);
        }

        try {
            if (!idCoordinator.getIds().contains(invoiceId)) {
                throw new DatabaseOperationException(
                    String.format(INVOICE_NOT_EXISTING_MSG, invoiceId));
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Unable to read invoice.", e);
        }

        for (Invoice invoice : getInvoices()) {
            if (invoice.getId().equals(invoiceId)) {
                return invoice;
            }
        }
        throw new DatabaseOperationException(DATABASE_CORRUPTED_MSG);
    }

    @Override
    public Collection<Invoice> getInvoices() throws DatabaseOperationException {
        ArrayList<Invoice> invoices;

        try {
            ArrayList<String> lines = fileAccessor.readLines();
            invoices = new JsonConverter().getInvoicesFromLines(lines);
        } catch (IOException e) {
            throw new DatabaseOperationException("Unable to read invoices.", e);
        }
        return invoices;
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate)
        throws DatabaseOperationException {
        if (startDate.isAfter(endDate)) {
            throw new DatabaseOperationException(
                "Start date [" + startDate + "] is after end date [" + endDate + "].");
        }

        return new ArrayList<>(getInvoices()).stream()
            .filter(invoice -> invoice.getIssueDate().toEpochDay() >= startDate.toEpochDay())
            .filter(invoice -> invoice.getIssueDate().toEpochDay() <= endDate.toEpochDay())
            .collect(Collectors.toList());
    }

    private String getLineFromInvoice(Invoice invoice) throws JsonProcessingException {
        return invoice.getId() + ": " + new JsonConverter().getJsonFromInvoice(invoice);
    }

    void synchronizeDbFiles() throws DatabaseOperationException {
        try {
            if (!idCoordinator.isDataSynchronized(getIdsFromDataFile())) {
                idCoordinator.synchronizeData(getIdsFromDataFile());
            }
        } catch (IOException e) {
            throw new DatabaseOperationException(DATABASE_CORRUPTED_MSG, e);
        }
    }

    ArrayList<Long> getIdsFromDataFile() throws DatabaseOperationException {
        ArrayList<Long> idsFromDataFile = new ArrayList<>();
        for (Invoice invoice : getInvoices()) {
            idsFromDataFile.add(invoice.getId());
        }
        return idsFromDataFile;
    }
}
