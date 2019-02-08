package pl.coderstrust.invoices.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.ArrayList;
import pl.coderstrust.invoices.model.Invoice;

class InvoiceConverter {

    private ObjectMapper invoiceMapper;

    InvoiceConverter() {
        invoiceMapper = new ObjectMapper();
        invoiceMapper.registerModule(new JavaTimeModule());
        invoiceMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private Invoice getInvoiceFromJson(String invoiceInJson) throws IOException {
        return invoiceMapper.readValue(invoiceInJson, Invoice.class);
    }

    String getJsonFromInvoice(Invoice invoice) throws JsonProcessingException {
        return invoiceMapper.writeValueAsString(invoice);
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
}
