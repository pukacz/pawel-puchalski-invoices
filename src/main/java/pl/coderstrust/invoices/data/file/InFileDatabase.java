package pl.coderstrust.invoices.data.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static void main(String[] args) throws IOException {
        InFileDatabase fileDatabase = new InFileDatabase();
    }

    private ObjectMapper mapper;
    private Configuration configuration;

    public InFileDatabase() throws IOException {
        mapper = new ObjectMapper();
        configuration = new Configuration();

        File file = new File(configuration.getInvoicesFile());
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        Long invoiceId = invoice.getId();
        String invoiceInJson;

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
            "rw")) {
            invoiceInJson = sentInvoiceToJsonString(invoice);
            FileHelper fileHelper = new FileHelper(file);
            fileHelper.saveInvoice(invoiceId, invoiceInJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInvoice(Long invoiceId) {

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
            "rw")) {
            FileHelper fileHelper = new FileHelper(file);
            fileHelper.deleteInvoice(invoiceId);
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
        List<String> lines;
        String invoiceInJson;

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(), "r")) {
            FileHelper fileHelper = new FileHelper(file);
            lines = fileHelper.getInvoices();

            for (String line : lines) {
                int colonPosition = line.indexOf(": ");
                if (colonPosition > 0) {
                    invoiceInJson = line.substring(colonPosition);
                    Invoice invoice = getInvoiceFromJsonString(invoiceInJson);
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

    private Invoice getInvoiceFromJsonString(String line) throws IOException {
        return mapper.readValue(line, Invoice.class);
    }

    private String sentInvoiceToJsonString(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }
}
