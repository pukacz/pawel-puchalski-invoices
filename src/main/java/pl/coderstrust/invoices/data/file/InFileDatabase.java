package pl.coderstrust.invoices.data.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.coderstrust.invoices.model.Invoice;

import java.io.IOException;

public class InFileDatabase {

    private FileHelper fileHelper;
    private ObjectMapper mapper;


    public InFileDatabase() {
        fileHelper = new FileHelper();
        mapper = new ObjectMapper();
    }

    void saveInvoice(Invoice invoice) throws JsonProcessingException {
        String json = sentInvoiceToJSONString(invoice);
//        fileHelper.writeLineToFile(json);
    }

    private Invoice getInvoiceFromJSONString(String line) throws IOException {
        return mapper.readValue(line, Invoice.class);
    }

    private String sentInvoiceToJSONString(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }
}
