package pl.coderstrust.invoices.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import pl.coderstrust.invoices.model.Invoice;

class Converter {

    private ObjectMapper jsonConverter;

    Converter() {
        jsonConverter = new ObjectMapper();
        jsonConverter.registerModule(new JavaTimeModule());
        jsonConverter.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        jsonConverter.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
    }

    private Invoice getInvoiceFromJson(String invoiceInJson) throws IOException {
        return jsonConverter.readValue(invoiceInJson, Invoice.class);
    }

    String getJsonFromInvoice(Invoice invoice) throws JsonProcessingException {
        return jsonConverter.writeValueAsString(invoice);
    }

    ArrayList<Invoice> getInvoicesFromLines(ArrayList<String> lines) throws IOException {
        ArrayList<Invoice> invoices = null;
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

    TreeSet<Long> getInvoiceIds(String line) throws IOException {
        return jsonConverter.readValue(line, TreeSet.class);
    }

    String sendIdsToJson(TreeSet<Long> ids) throws JsonProcessingException {
        return jsonConverter.writeValueAsString(ids);
    }
}
