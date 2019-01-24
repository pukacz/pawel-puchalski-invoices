package pl.coderstrust.invoices.data.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pl.coderstrust.invoices.model.Invoice;

public class InFileDatabase {

    private ObjectMapper mapper;
    private Configuration configuration;

    public InFileDatabase() throws IOException {
        mapper = new ObjectMapper();
        configuration = new Configuration();

        if (configuration.getInvoicesFile().equals(null)) {
            File file = new File(configuration.getInvoicesFile());
            file.createNewFile();
        }
    }

    Collection<Invoice> getInvoices() throws IOException {

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
                    Invoice invoice = getInvoiceFromJSONString(invoiceInJson);
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    void saveInvoice(Invoice invoice) throws IOException {
        Long invoiceId = invoice.getId();
        String invoiceInJson = sentInvoiceToJSONString(invoice);

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
            "rw")) {
            FileHelper fileHelper = new FileHelper(file);
            fileHelper.saveInvoice(invoiceId, invoiceInJson);
        }
    }

    void deleteInvoice(Long invoiceId) throws IOException {

        try (RandomAccessFile file = new RandomAccessFile(configuration.getInvoicesFile(),
            "rw")) {
            FileHelper fileHelper = new FileHelper(file);
            fileHelper.deleteInvoice(invoiceId);
        }
    }

    private Invoice getInvoiceFromJSONString(String line) throws IOException {
        return mapper.readValue(line, Invoice.class);
    }

    private String sentInvoiceToJSONString(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }
}
