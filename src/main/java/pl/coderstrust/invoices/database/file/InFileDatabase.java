package pl.coderstrust.invoices.database.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.model.Invoice;

public class InFileDatabase implements Database {

    public static void main(String[] args) throws IOException {
        InFileDatabase inf = new InFileDatabase();

        Invoice invoice1 = new Invoice(1L, null, null, null, null, null);
        Invoice invoice2 = new Invoice(2L, null, null, null, null, null);
        Invoice invoice3 = new Invoice(null, null, null, null, null);

        inf.saveInvoice(invoice1);
        inf.saveInvoice(invoice2);
        inf.saveInvoice(invoice3);
    }

    private Converter converter;
    private File invoicesFile;
    private InvoiceIdCoordinator idCoordinator;

    public InFileDatabase() throws IOException {
        converter = new Converter();
        invoicesFile = new Configuration().getInvoicesFile();
        idCoordinator = new InvoiceIdCoordinator();

        if (!invoicesFile.exists()) {
            invoicesFile.createNewFile();
        }
    }

    @Override
    public void saveInvoice(Invoice invoice) throws IOException {
        Long invoiceId = invoice.getId();

        if (invoiceId.equals(0L)) {
            invoiceId = new InvoiceIdCoordinator().generateId();
            invoice.setId(invoiceId);
        }

        try (RandomAccessFile file = new RandomAccessFile(invoicesFile, "rw")) {
            String invoiceInJson = converter.getJsonFromInvoice(invoice);
            InvoiceFileAccessor fileAccessor = new InvoiceFileAccessor(file);
            fileAccessor.saveLine(invoiceId, invoiceInJson);
        }

        idCoordinator.coordinateIds(invoiceId);
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        try (RandomAccessFile file = new RandomAccessFile(invoicesFile, "rw")) {
            InvoiceFileAccessor fileAccessor = new InvoiceFileAccessor(file);
            fileAccessor.invalidateLine(invoiceId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Invoice getInvoice(Long invoiceId) {
        ArrayList<Invoice> invoices = new ArrayList<>(getInvoices());

        for (Invoice invoice : invoices) {
            if (invoice.getId().equals(invoiceId)) {
                return invoice;
            }
        }
        return null;
    }

    public Collection<Invoice> getInvoices() {
        ArrayList<Invoice> invoices = new ArrayList<>();

        try (RandomAccessFile file = new RandomAccessFile(invoicesFile, "r")) {
            InvoiceFileAccessor fileAccessor = new InvoiceFileAccessor(file);
            ArrayList<String> lines = fileAccessor.getInvoiceFileLines();
            invoices = converter.getInvoicesFromLines(lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    @Override
    public Collection<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate) {
        return new ArrayList<>(getInvoices()).stream()
            .filter(invoice -> invoice.getIssueDate().toEpochDay() >= startDate.toEpochDay())
            .filter(invoice -> invoice.getIssueDate().toEpochDay() <= endDate.toEpochDay())
            .collect(Collectors.toList());
    }
}
