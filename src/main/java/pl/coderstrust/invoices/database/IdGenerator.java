package pl.coderstrust.invoices.database;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    @Autowired
    public IdGenerator() {
    }

    public Long generateId(Collection<Long> ids) {
        Long invoiceId = 1L;
        if (!ids.isEmpty()) {
            while (ids.contains(invoiceId)) {
                invoiceId++;
            }
        }
        return invoiceId;
    }
}
