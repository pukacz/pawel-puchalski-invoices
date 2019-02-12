package pl.coderstrust.invoices.database.file;

import java.util.TreeSet;

public class IdGenerator {

    public Long generateId(TreeSet<Long> ids) {
        Long invoiceId = 1L;
        if (!ids.isEmpty()) {
            while (ids.contains(invoiceId)) {
                invoiceId++;
            }
        }
        return invoiceId;
    }
}
