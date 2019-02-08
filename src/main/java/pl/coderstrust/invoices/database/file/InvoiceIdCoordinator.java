package pl.coderstrust.invoices.database.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InvoiceIdCoordinator {

    private Long invoiceId = 0L;
    private File invoicesLastIdFile;

    public InvoiceIdCoordinator() throws IOException {
        invoicesLastIdFile = new Configuration().getInvoicesLastIdFile();
        if (!invoicesLastIdFile.exists()) {
            invoicesLastIdFile.createNewFile();
        }
    }

    public Long getId() {
        try (BufferedReader br = new BufferedReader(new FileReader(invoicesLastIdFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                invoiceId = Long.parseLong(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoiceId + 1;
    }

    public void saveLastId() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invoicesLastIdFile))) {
            String lastInvoiceId = "" + getId();
            bw.write(lastInvoiceId);
        } catch (IOException e) {
            e.printStackTrace();
            
        }
    }
}
