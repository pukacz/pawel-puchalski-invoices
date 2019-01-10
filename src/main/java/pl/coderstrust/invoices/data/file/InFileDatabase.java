package pl.coderstrust.invoices.data.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.coderstrust.invoices.model.Invoice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InFileDatabase {

    private FileHelper fileHelper;
    private ObjectMapper mapper;
    private Configuration configuration;

    public InFileDatabase() {
        fileHelper = new FileHelper();
        mapper = new ObjectMapper();
        configuration = new Configuration();
    }

    Collection<Invoice> getInvoices() throws IOException {
        List<String> invoicesInJson = fileHelper.readLinesFromFile(configuration.getInvoicesFile());
        List<Invoice> invoices = new ArrayList<>();

        for (String json : invoicesInJson) {
            invoices.add(getInvoiceFromJSONString(json));
        }
        return invoices;
    }

    void saveInvoice(Invoice invoice) throws IOException {
        String json = sentInvoiceToJSONString(invoice);
        Long id = invoice.getId();

        List<Invoice> invoices = new ArrayList<>(getInvoices());

        boolean isInvoiceFound = false;

        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getId() == id) {
                invoices.remove(i);
                invoices.add(i, invoice);
                isInvoiceFound = true;
            }
        }

        if (!isInvoiceFound) {
            invoices.add(invoice);
        }
    }

    private Invoice getInvoiceFromJSONString(String line) throws IOException {
        return mapper.readValue(line, Invoice.class);
    }

    private String sentInvoiceToJSONString(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }
}
