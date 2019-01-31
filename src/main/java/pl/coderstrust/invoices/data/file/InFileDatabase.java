package pl.coderstrust.invoices.data.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    private ObjectMapper mapper;
    private Configuration configuration;

    public InFileDatabase() throws IOException {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configuration = new Configuration();

        File file = new File(configuration.getInvoicesFilePath());
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        Long invoiceId = invoice.getId();

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFilePath(),
            "rw")) {
            String invoiceInJson = sentInvoiceToJsonString(invoice);
            FileHelper fileHelper = new FileHelper(file);
            fileHelper.saveInvoice(invoiceId, invoiceInJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInvoice(Long invoiceId) {

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFilePath(),
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
        List<String> invoiceIdAndInvoiceInJson;
        String invoiceInJson;

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFilePath(),
            "r")) {
            FileHelper fileHelper = new FileHelper(file);
            invoiceIdAndInvoiceInJson = fileHelper.getInvoices();

            for (String line : invoiceIdAndInvoiceInJson) {
                int colonPosition = line.indexOf(": ");
                if (colonPosition > 0) {
                    invoiceInJson = line.substring(colonPosition + 2);
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

    String sentInvoiceToJsonString(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }
}
