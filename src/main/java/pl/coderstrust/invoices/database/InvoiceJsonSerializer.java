package pl.coderstrust.invoices.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderstrust.invoices.model.Invoice;

@Component
public class InvoiceJsonSerializer {

    private ObjectMapper jsonConverter;

    @Autowired
    public InvoiceJsonSerializer() {
        jsonConverter = new ObjectMapper();
        jsonConverter.registerModule(new JavaTimeModule());
        jsonConverter.registerModule(new ParameterNamesModule());
        jsonConverter.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        jsonConverter.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
    }

    public Invoice getInvoiceFromJson(String invoiceInJson) throws IOException {
        return jsonConverter.readValue(invoiceInJson, Invoice.class);
    }

    public String getJsonFromInvoice(Invoice invoice) throws JsonProcessingException {
        return jsonConverter.writeValueAsString(invoice);
    }

    public ArrayList<Invoice> getInvoicesFromLines(ArrayList<String> lines) throws IOException {
        ArrayList<Invoice> invoices = new ArrayList<>();
        for (String line : lines) {
            int colonPosition = line.indexOf(": ");
            if (colonPosition > 0) {
                String invoiceInJson = line.substring(colonPosition + 2);
                Invoice invoice = getInvoiceFromJson(invoiceInJson);
                if (invoice != null) {
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    public Collection getInvoicesIds(String line) throws IOException {
        return jsonConverter.readValue(line, TreeSet.class);
    }

    public String sendIdsToJson(Collection<Long> ids) throws JsonProcessingException {
        return jsonConverter.writeValueAsString(ids);
    }
}
