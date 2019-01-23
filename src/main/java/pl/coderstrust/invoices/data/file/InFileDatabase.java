package pl.coderstrust.invoices.data.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pl.coderstrust.invoices.model.Invoice;

public class InFileDatabase {

    private FileHelper fileHelper;
    private ObjectMapper mapper;
    private Configuration configuration;

    public InFileDatabase() {
        //sprawdzamy czy plik invoices.dat istnieje, jesli nie to go tworzymy
        this.fileHelper = new FileHelper();
        mapper = new ObjectMapper();
        configuration = new Configuration();
    }

    Collection<Invoice> getInvoices() throws IOException {

        ArrayList<String> list = new ArrayList<>();
        String line;

        try (RandomAccessFile reader = new RandomAccessFile(configuration.getInvoicesFile(), "r")) {
//            FileHelper fileHelper = new FileHelper(reader);
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        }
//        return list;

        List<String> lines = fileHelper.getInvoices();
        ArrayList<Invoice> invoices = new ArrayList<>();
        int colonPosition;

//        for (String line : lines) {
//            colonPosition = line.indexOf(":");
//            invoices.add(getInvoiceFromJSONString(line.substring(colonPosition)));
//        }
        return null;
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
