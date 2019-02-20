package pl.coderstrust.invoices.database.file;

import java.io.File;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
class Configuration {

    private String invoicesFilePath;
    private String invoicesIdsFilePath;

    private String defaultFolderPath = "src" + File.separator + "main" + File.separator;

    @Bean("invoicesFilePath")
    public String defaultInvoicesFilePath() {
        return defaultFolderPath + "invoices.dat";
    }

    @Bean("invoicesIdsFilePath")
    public String defaultInvoicesIdsFilePath() {
        return defaultFolderPath + "invoicesIds.cor";
    }

    String getInvoicesFilePath() {
        return invoicesFilePath;
    }

    String getInvoicesIdsFilePath() {
        return invoicesIdsFilePath;
    }

    Configuration(String invoicesFilePath, String invoicesIdsFilePath) {
        this.invoicesFilePath = invoicesFilePath;
        this.invoicesIdsFilePath = invoicesIdsFilePath;
    }
}
