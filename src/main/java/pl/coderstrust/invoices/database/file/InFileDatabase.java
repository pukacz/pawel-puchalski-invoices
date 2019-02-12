package pl.coderstrust.invoices.database.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;
import pl.coderstrust.invoices.database.Database;
import pl.coderstrust.invoices.model.Invoice;

public class InFileDatabase implements Database {

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        Converter converter = new Converter();
        InFileDatabase inf = new InFileDatabase(configuration, converter);

        Invoice invoice1 = new Invoice(1L, "cos", null, null, null, null);
        Invoice invoice2 = new Invoice(3L, "ccc", null, null, null, null);
        Invoice invoice3 = new Invoice(null, null, null, null, null);

            inf.saveInvoice(invoice1);
            inf.saveInvoice(invoice2);
            inf.saveInvoice(invoice3);
    }

    private Configuration configuration;
    private Converter converter;
    InvoiceFileAccessor fileAccessor;
    InvoiceIdCoordinator invoiceIdCoordinator;

    InFileDatabase(Configuration configuration, Converter converter) throws IOException {
        this.configuration = configuration;
        this.converter = converter;
        this.fileAccessor = new InvoiceFileAccessor(configuration);

        File invoicesFile = configuration.getInvoicesFile();
        if (!invoicesFile.exists()) {
            invoicesFile.createNewFile();
        }

        File invoicesIdsFile = configuration.getInvoicesIdsCoordinationFile();
        if (!invoicesIdsFile.exists()) {
            invoicesIdsFile.createNewFile();
            TreeSet<Long> invoicesIds = new TreeSet<>();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
                String emptyList = converter.sendIdsToJson(invoicesIds);
                writer.write(emptyList);
            }
        }
        this.invoiceIdCoordinator = new InvoiceIdCoordinator(configuration, converter);
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        Long invoiceId = invoice.getId();

        try {
            if (invoiceId == null) {
                invoiceId = invoiceIdCoordinator.generateId();
                invoice.setId(invoiceId);
            }

            if (invoiceIdCoordinator.getIds().contains(invoiceId)) {
                fileAccessor.invalidateLine(invoiceId);
            }

            String invoiceInJson = converter.getJsonFromInvoice(invoice);
            fileAccessor.saveLine(invoiceId, invoiceInJson);
            invoiceIdCoordinator.coordinateIds(invoiceId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        try {
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

    @Override
    public Collection<Invoice> getInvoices() {
        ArrayList<Invoice> invoices = new ArrayList<>();

        try {
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
