package pl.coderstrust.invoices.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import pl.coderstrust.invoices.model.Invoice;

public class InvoiceConverter {

    private ObjectMapper invoiceMapper;

    public InvoiceConverter() {
        invoiceMapper = new ObjectMapper();
        invoiceMapper.registerModule(new JavaTimeModule());
        invoiceMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public Invoice getInvoiceFromJson(String invoiceInJson) throws IOException {
        return invoiceMapper.readValue(invoiceInJson, Invoice.class);
    }

    public String getJsonFromInvoice(Invoice invoice) throws JsonProcessingException {
        return invoiceMapper.writeValueAsString(invoice);
    }
}
