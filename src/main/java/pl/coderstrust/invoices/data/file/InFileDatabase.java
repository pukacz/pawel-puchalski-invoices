package pl.coderstrust.invoices.data.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.coderstrust.invoices.model.Invoice;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        ArrayList<Invoice> invoices = new ArrayList<>();

        for (String json : invoicesInJson) {
            invoices.add(getInvoiceFromJSONString(json));
        }
        return invoices;
    }

    void saveInvoice(Invoice invoice) throws IOException {
        Long id = invoice.getId();

        if (getIDsOfInvoices().contains(id)) {
            deleteInvoice(id);
        }

        String json = sentInvoiceToJSONString(invoice);

        ArrayList<Invoice> invoices = new ArrayList<>(getInvoices());
        invoices.add(invoice);
    }

    void deleteInvoice(Long id) throws IOException {
        ArrayList<Invoice> invoices = new ArrayList<>(getInvoices());

        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getId() == id) {
                invoices.remove(i);
                break;
            }
        }
    }

    private Invoice getInvoiceFromJSONString(String line) throws IOException {
        return mapper.readValue(line, Invoice.class);
    }

    private String sentInvoiceToJSONString(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    private List<Long> getIDsOfInvoices() throws IOException {
        List<String> idsInString = fileHelper.readLinesFromFile(configuration.getInvoicesIDFile());
        List<Long> ids = idsInString.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return ids;
    }

    private void addInvoiceIDtoList(Long id) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }
}
