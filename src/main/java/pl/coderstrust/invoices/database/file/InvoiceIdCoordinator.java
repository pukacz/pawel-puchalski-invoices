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
    private TreeSet<Long> invoiceIds;
    private Converter converter;

    InvoiceIdCoordinator() throws IOException {
        converter = new Converter();
        file = new Configuration().getInvoicesIdCoordinationFile();

        if (!file.exists()) {
            file.createNewFile();
            invoiceIds = new TreeSet<>();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                String emptyList = converter.sendIdsToJson(invoiceIds);
                writer.write(emptyList);
            }
        }
        invoiceIds = getIds();
    }

    void coordinateIds(Long invoiceId) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            invoiceIds.add(invoiceId);
            String line = converter.sendIdsToJson(invoiceIds);
            writer.write(line);
        }
    }

    TreeSet<Long> getIds() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            if ((line = reader.readLine()) != null) {
                invoiceIds = converter.getInvoiceIds(line);
            }
        }
        return invoiceIds;
    }

    Long generateId() {
        if (!invoiceIds.isEmpty()) {
            return invoiceIds.last() + 1;
        }
        return 1L;
    }
}
