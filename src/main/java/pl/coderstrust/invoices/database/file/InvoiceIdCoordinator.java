package pl.coderstrust.invoices.database.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeSet;

class InvoiceIdCoordinator {

    private File invoicesIdsFile;
    private Collection<Long> invoicesIds;

    InvoiceIdCoordinator(Configuration configuration) throws IOException {
        this.invoicesIdsFile = new File(configuration.getInvoicesIdsFilePath());

        if (!invoicesIdsFile.exists()) {
            invoicesIdsFile.createNewFile();
            writeEmptySet();
        }
        invoicesIds = getIds();
    }

    Collection<Long> getIds() throws IOException {
        Collection<Long> invoicesIds;
        try (BufferedReader reader = new BufferedReader(new FileReader(invoicesIdsFile))) {
            String line;
            if ((line = reader.readLine()) != null) {
                invoicesIds = new Converter().getInvoicesIds(line);
            } else {
                writeEmptySet();
                return new TreeSet<>();
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

    private void writeEmptySet() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            String emptyList = new Converter().sendIdsToJson(new TreeSet<>());
            writer.write(emptyList);
        }
    }
}
