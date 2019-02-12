package pl.coderstrust.invoices.database.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

class InvoiceIdCoordinator {

    private File file;
    private TreeSet<Long> invoicesIds;
    private Converter converter;

    InvoiceIdCoordinator(Configuration configuration, Converter converter) throws IOException {
        file = configuration.getInvoicesIdsCoordinationFile();
        this.converter = converter;
        invoicesIds = getIds();
    }


    TreeSet<Long> getIds() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            if ((line = reader.readLine()) != null) {
                invoicesIds = converter.getInvoicesIds(line);
            }
        }
        return invoicesIds;
    }

    void coordinateIds(Long invoiceId) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            invoicesIds.add(invoiceId);
            String line = converter.sendIdsToJson(invoicesIds);
            writer.write(line);
        }
    }

    Long generateId() {
        Long invoiceId = 1L;
        if (!invoicesIds.isEmpty()) {
            while (invoicesIds.contains(invoiceId)) {
                invoiceId++;
            }
        }
        return invoiceId;
    }
}
