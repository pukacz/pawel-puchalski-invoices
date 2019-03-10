package pl.coderstrust.invoices.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.database.DatabaseOperationException;
import pl.coderstrust.invoices.database.IdGenerator;
import pl.coderstrust.invoices.database.InvoiceJsonSerializer;
import pl.coderstrust.invoices.model.Invoice;

@Repository
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "file")
public class InFileDatabase implements Database {

    private static final String DATABASE_CORRUPTED_MSG =
        "You are trying to read/update invoice which is "
            + "not recognized in coordination file. Please synchronize database files first.";
    private static final String INVOICE_NOT_EXISTING_MSG = "Invoice id=[%d] doesn't exist.";
    private static final String INVOICE_ID_NOT_NULL_MSG = "Invoice Id must not be null.";
    private static final String ID_MUST_BE_LONG_TYPE_MSG = "Argument Id must be Long type.";
    private InvoiceFileAccessor fileAccessor;
    private InvoiceIdCoordinator idCoordinator;
    private IdGenerator idGenerator;
    private InvoiceJsonSerializer invoiceJsonSerializer;

    @Autowired
    InFileDatabase(InvoiceFileAccessor fileAccessor, InvoiceIdCoordinator idCoordinator, IdGenerator idGenerator) {
        this.fileAccessor = fileAccessor;
        this.idCoordinator = idCoordinator;
        this.idGenerator = idGenerator;
        invoiceJsonSerializer = new InvoiceJsonSerializer();
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null.");
        }

        Long invoiceId = getIdFromObject(invoice.getId());
        Invoice invoiceGenerated;

        try {
            Collection<Long> ids = idCoordinator.getIds();

            if (invoiceId == null) {
                invoiceId = idGenerator.generateId(ids);
                invoiceGenerated = new Invoice(invoice, invoiceId);
            } else {
                if (ids.contains(invoiceId)) {
                    deleteInvoice(invoiceId);
                    invoiceGenerated = invoice;
                } else {
                    throw new DatabaseOperationException(DATABASE_CORRUPTED_MSG);
                }
            }
            fileAccessor.saveLine(getLineFromInvoice(invoiceGenerated));
            idCoordinator.coordinateIds(invoiceId);
        } catch (IOException e) {
            throw new DatabaseOperationException("Unable to save invoice.", e);
        }
        return new Invoice(invoiceGenerated);
    }

    @Override
    public void deleteInvoice(Object invoiceId) throws DatabaseOperationException {
        if (invoiceId == null) {
            throw new IllegalArgumentException(INVOICE_ID_NOT_NULL_MSG);
        }

        Long id = getIdFromObject(invoiceId);

        try {
            if (!fileAccessor.invalidateLine(id)) {
                throw new DatabaseOperationException("Invoice id=[" + invoiceId + "] doesn't exist.");
            }
            idCoordinator.removeId(id);
        } catch (IOException e) {
            throw new DatabaseOperationException("Unable to delete invoice.", e);
        }
    }

    @Override
    public Invoice getInvoice(Object invoiceId) throws DatabaseOperationException {
        if (invoiceId == null) {
            throw new IllegalArgumentException(INVOICE_ID_NOT_NULL_MSG);
        }

        Long id = getIdFromObject(invoiceId);

        try {
            if (!idCoordinator.getIds().contains(id)) {
                throw new DatabaseOperationException(
                    String.format(INVOICE_NOT_EXISTING_MSG, id));
            }
        } catch (IOException e) {
            throw new DatabaseOperationException("Unable to read invoice.", e);
        }

        for (Invoice invoice : getInvoices()) {
            if (invoice.getId().equals(id)) {
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
            invoices = invoiceJsonSerializer.getInvoicesFromLines(lines);
        } catch (IOException e) {
            throw new DatabaseOperationException("Unable to read invoices.", e);
        }
        return invoices;
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
            throw new DatabaseOperationException(
                "Start date [" + startDate + "] is after end date [" + endDate + "].");
        }

        return new ArrayList<>(getInvoices()).stream()
            .filter(invoice -> invoice.getIssueDate().toEpochDay() >= startDate.toEpochDay())
            .filter(invoice -> invoice.getIssueDate().toEpochDay() <= endDate.toEpochDay())
            .collect(Collectors.toList());
    }

    private String getLineFromInvoice(Invoice invoice) throws JsonProcessingException {
        return invoice.getId() + ": " + invoiceJsonSerializer.getJsonFromInvoice(invoice);
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
            idsFromDataFile.add((Long) invoice.getId());
        }
        return idsFromDataFile;
    }

    Long getIdFromObject(Object id) {
        if (id == null) {
            return null;
        }

        if (!(id instanceof String) && !(id instanceof Integer) && !(id instanceof Long)) {
            throw new IllegalArgumentException(ID_MUST_BE_LONG_TYPE_MSG);
        }

        if (id instanceof String) {
            if (NumberUtils.isParsable((String) id)) {
                return Long.parseLong((String) id);
            } else {
                throw new IllegalArgumentException(ID_MUST_BE_LONG_TYPE_MSG);
            }
        }

        if (id instanceof Integer) {
            return ((Integer) id).longValue();
        }
        return (Long) id;
    }
}
