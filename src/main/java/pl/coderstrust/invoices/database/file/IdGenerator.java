package pl.coderstrust.invoices.database.file;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class IdGenerator {

    @Autowired
    public IdGenerator() {
    }

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
