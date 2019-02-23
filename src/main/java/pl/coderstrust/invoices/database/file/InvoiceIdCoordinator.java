package pl.coderstrust.invoices.database.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class InvoiceIdCoordinator {

    private File invoicesIdsFile;
    private Collection<Long> invoicesIds;

    @Autowired
    InvoiceIdCoordinator(Configuration configuration) throws IOException {
        this.invoicesIdsFile = new File(configuration.getInvoicesIdsFilePath());
        File parent = invoicesIdsFile.getParentFile();

        if (!invoicesIdsFile.exists()) {
            if (!parent.exists()) {
                parent.mkdirs();
            }
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
        invoicesIds.add(invoiceId);
        String line = new Converter().sendIdsToJson(invoicesIds);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            writer.write(line);
        }
    }

    private void writeEmptySet() throws IOException {
        String emptyList = new Converter().sendIdsToJson(new TreeSet<>());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            writer.write(emptyList);
        }
    }

    public void removeId(Long invoiceId) {
        invoicesIds.remove(invoiceId);
    }

    public boolean isDataSynchronized(Collection<Long> ids) throws IOException {
        invoicesIds = ids;
        String updatedList = new Converter().sendIdsToJson(new TreeSet<>(invoicesIds));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            writer.write(updatedList);
        }
        return true;
    }
}
