package pl.coderstrust.invoices.database.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.model.Invoice;

public class InFileDatabase implements Database {

    private InvoiceConverter invoiceConverter;
    private File invoicesFile;

    public InFileDatabase() throws IOException {
        invoiceConverter = new InvoiceConverter();
        invoicesFile = new Configuration().getFile();

        if (!invoicesFile.exists()) {
            invoicesFile.createNewFile();
        }
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        Long invoiceId = invoice.getId();

        try (RandomAccessFile file = new RandomAccessFile(invoicesFile, "rw")) {
            String invoiceInJson = invoiceConverter.getJsonFromInvoice(invoice);
            InvoiceFileAccessor fileAccessor = new InvoiceFileAccessor(file);
            fileAccessor.saveLine(invoiceId, invoiceInJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            List<String> lines = fileAccessor.getInvoiceFileLines();

            for (String line : lines) {
                int colonPosition = line.indexOf(": ");
                if (colonPosition > 0) {
                    String invoiceInJson = line.substring(colonPosition + 2);
                    Invoice invoice = invoiceConverter.getInvoiceFromJson(invoiceInJson);
                    invoices.add(invoice);
                }
            }
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
