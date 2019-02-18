package pl.coderstrust.invoices.database.file;

class Configuration {

    private String invoicesFilePath;
    private String invoicesIdsFilePath;

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
