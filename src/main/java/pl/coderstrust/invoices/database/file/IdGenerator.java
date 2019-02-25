package pl.coderstrust.invoices.database.file;

import java.util.Collection;

class IdGenerator {

    Long generateId(Collection<Long> ids) {
        Long invoiceId = 1L;
        if (!ids.isEmpty()) {
            while (ids.contains(invoiceId)) {
                invoiceId++;
            }
        }
        return invoiceId;
    }
}
