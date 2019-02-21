package pl.coderstrust.invoices.database.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class Configuration {

    @Value("${pl.coderstrust.database.file.database-file}")
    private String invoicesFilePath;
    @Value("${pl.coderstrust.database.file.id-controller}")
    private String invoicesIdsFilePath;

    String getInvoicesFilePath() {
        return invoicesFilePath;
    }

    String getInvoicesIdsFilePath() {
        return invoicesIdsFilePath;
    }

    Configuration() {
    }

    Configuration(String invoicesFilePath, String invoicesIdsFilePath) {
        this.invoicesFilePath = invoicesFilePath;
        this.invoicesIdsFilePath = invoicesIdsFilePath;
    }
}
