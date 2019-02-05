package pl.coderstrust.invoices.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import pl.coderstrust.invoices.model.Invoice;

public class InvoiceConverter {

    public InvoiceConverter(){
        ObjectMapper invoiceMapper = new ObjectMapper();
        invoiceMapper = new ObjectMapper();
        invoiceMapper.registerModule(new JavaTimeModule());
        invoiceMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public  Invoice getInvoiceFromJsonString(String line) throws IOException {
        return new ObjectMapper().readValue(line, Invoice.class);
    }

    public  String sentInvoiceToJsonString(Invoice invoice) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(invoice);
    }
}
