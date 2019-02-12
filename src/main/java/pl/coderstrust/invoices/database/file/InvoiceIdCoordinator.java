package pl.coderstrust.invoices.database.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

class InvoiceIdCoordinator {

    private File invoicesIdsFile;
    private TreeSet<Long> invoicesIds;

    InvoiceIdCoordinator(File invoicesIdsFile) throws IOException {
        this.invoicesIdsFile = invoicesIdsFile;

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

    void coordinateIds(Long invoiceId) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            invoicesIds.add(invoiceId);
            String line = new Converter().sendIdsToJson(invoicesIds);
            writer.write(line);
        }
    }
}
