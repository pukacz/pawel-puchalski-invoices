package pl.coderstrust.invoices.data.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pl.coderstrust.invoices.model.Invoice;

public class InFileDatabase {

    private FileHelper fileHelper;
    private ObjectMapper mapper;

    public InFileDatabase() {
        this.fileHelper = new FileHelper();
        mapper = new ObjectMapper();
    }

    Collection<Invoice> getInvoices() throws IOException {
        List<String> lines = fileHelper.getInvoices();
        ArrayList<Invoice> invoices = new ArrayList<>();
        int colonPosition;

        for (String line : lines) {
            colonPosition = line.indexOf(":");
            invoices.add(getInvoiceFromJSONString(line.substring(colonPosition)));
        }
        return invoices;
    }


    void saveInvoice(Invoice invoice) throws IOException {
        Long invoiceId = invoice.getId();
        String invoiceInJson = sentInvoiceToJSONString(invoice);
        fileHelper.saveInvoice(invoiceId, invoiceInJson);
    }


//    void deleteInvoice(Long id) {
//        if (isInvoiceExisting(id)) {
//            for (Invoice i : invoices) {
//                if (i.getId().equals(id)) {
//                    invoices.remove(i);
//                    break;
//                }
//            }
//        }
//    }

//    private boolean isInvoiceExisting(Long id) {
//        return ids.contains(id);
//    }

    private Invoice getInvoiceFromJSONString(String line) throws IOException {
        return mapper.readValue(line, Invoice.class);
    }

    private String sentInvoiceToJSONString(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }
}
