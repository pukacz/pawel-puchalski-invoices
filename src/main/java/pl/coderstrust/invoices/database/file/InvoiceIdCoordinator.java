package pl.coderstrust.invoices.database.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

public class InvoiceIdCoordinator {

    private File invoicesIdsFile;
    private TreeSet<Long> invoicesIds;

    InvoiceIdCoordinator(Configuration configuration) throws IOException {
        this.invoicesIdsFile = configuration.getInvoicesIdsFile();

        if (!invoicesIdsFile.exists()) {
            invoicesIdsFile.createNewFile();
            TreeSet<Long> invoicesIds = new TreeSet<>();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
                String emptyList = new Converter().sendIdsToJson(invoicesIds);
                writer.write(emptyList);
            }
        }
        invoicesIds = getIds();
    }

    TreeSet<Long> getIds() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(invoicesIdsFile))) {
            String line;
            if ((line = reader.readLine()) != null) {
                invoicesIds = new Converter().getInvoicesIds(line);
            }
        }
        return invoicesIds;
    }

    boolean coordinateIds(Long invoiceId) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            invoicesIds.add(invoiceId);
            String line = new Converter().sendIdsToJson(invoicesIds);
            writer.write(line);
        }
        return true;
    }
}
