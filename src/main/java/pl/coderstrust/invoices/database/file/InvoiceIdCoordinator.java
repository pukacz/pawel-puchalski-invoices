package pl.coderstrust.invoices.database.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

public class InvoiceIdCoordinator {

    private File file;
    private TreeSet<Long> invoiceIds;
    private Converter converter;

    public static void main(String[] args) throws IOException {
        InvoiceIdCoordinator invoiceIdCoordinator = new InvoiceIdCoordinator();
        System.out.println(invoiceIdCoordinator.getIds());
        System.out.println(invoiceIdCoordinator.generateId());
        invoiceIdCoordinator.coordinateIds(8L);
        invoiceIdCoordinator.coordinateIds(4L);
        invoiceIdCoordinator.coordinateIds(5L);
    }

    public InvoiceIdCoordinator() throws IOException {
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

    public void coordinateIds(Long invoiceId) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            System.out.println(invoiceIds);
            invoiceIds.add(Long.parseLong("" + invoiceId));
            String line = converter.sendIdsToJson(invoiceIds);
            writer.write(line);
        }
    }

    private TreeSet<Long> getIds() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            if ((line = reader.readLine()) != null) {
                invoiceIds = converter.getInvoiceIds(line);
            }
        }
        return invoiceIds;
    }

    public Long generateId() {
        if (!invoiceIds.isEmpty()) {
            String last = "" + invoiceIds.last();
            return Long.parseLong(last) + 1;
        }
        return 1L;
    }
}
