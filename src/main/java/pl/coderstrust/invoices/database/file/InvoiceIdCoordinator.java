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
import pl.coderstrust.invoices.database.Converter;

@Component
class InvoiceIdCoordinator {

    private File invoicesIdsFile;
    private Collection<Long> invoicesIds;
    private Converter converter;

    @Autowired
    InvoiceIdCoordinator(Configuration configuration) throws IOException {
        this.converter = new Converter();
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
                invoicesIds = converter.getInvoicesIds(line);
            } else {
                writeEmptySet();
                return new TreeSet<>();
            }
        }
        return invoicesIds;
    }

    void coordinateIds(Long invoiceId) throws IOException {
        invoicesIds.add(invoiceId);
        String line = converter.sendIdsToJson(invoicesIds);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            writer.write(line);
        }
    }

    private void writeEmptySet() throws IOException {
        String emptyList = converter.sendIdsToJson(new TreeSet<>());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            writer.write(emptyList);
        }
    }

    void removeId(Long invoiceId) throws IOException {
        invoicesIds.remove(invoiceId);
        String line = converter.sendIdsToJson(invoicesIds);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            writer.write(line);
        }
    }

    boolean isDataSynchronized(Collection<Long> ids) {
        return invoicesIds.containsAll(ids) && ids.containsAll(invoicesIds);
    }

    void synchronizeData(Collection<Long> ids) throws IOException {
        invoicesIds = ids;
        String updatedList = converter.sendIdsToJson(new TreeSet<>(invoicesIds));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(invoicesIdsFile))) {
            writer.write(updatedList);
        }
    }
}
