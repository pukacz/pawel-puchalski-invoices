package pl.coderstrust.invoices.data.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.coderstrust.invoices.model.Invoice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class InFileDatabase {

    private Collection<Invoice> invoices;
    private Collection<Long> ids;
    private FileHelper fileHelper;
    private ObjectMapper mapper;
    private Configuration configuration;

    public InFileDatabase() throws IOException {
        fileHelper = new FileHelper();
        mapper = new ObjectMapper();
        configuration = new Configuration();
        invoices = getInvoices();
        ids = getIDsOfExistingInvoices();
    }

    Collection<Invoice> getInvoices() throws IOException {
        List<String> invoicesInJson = fileHelper.readLinesFromFile(configuration.getInvoicesFile());
        ArrayList<Invoice> invoices = new ArrayList<>();

        for (String json : invoicesInJson) {
            invoices.add(getInvoiceFromJSONString(json));
        }
        return invoices;
    }

    Collection<Long> getIDsOfExistingInvoices() throws IOException {
        List<String> idsInString = fileHelper.readLinesFromFile(configuration.getInvoicesIDFile());
        List<Long> ids = idsInString.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return ids;
    }

    void saveInvoice(Invoice invoice) throws IOException {
        Long id = invoice.getId();

        if (isInvoiceExisting(id)) {
            deleteInvoice(id);
        }

        String json = sentInvoiceToJSONString(invoice);

        invoices.add(invoice);
    }


    void deleteInvoice(Long id) {
        if (isInvoiceExisting(id)) {
            for (Invoice i : invoices) {
                if (i.getId().equals(id)) {
                    invoices.remove(i);
                    break;
                }
            }
        }
    }

    private boolean isInvoiceExisting(Long id) {
        return ids.contains(id);
    }

    private Invoice getInvoiceFromJSONString(String line) throws IOException {
        return mapper.readValue(line, Invoice.class);
    }

    private String sentInvoiceToJSONString(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }


//    private void addInvoiceIDtoList(Long id) throws JsonProcessingException {
//        return mapper.writeValueAsString(invoice);
//    }
}
