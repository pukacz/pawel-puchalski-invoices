package pl.coderstrust.invoices.database.file;

import java.io.File;

class Configuration {

    private String invoicesFilePath;
    private String invoicesIdsFilePath;

    private static final String INVOICES_FILE = "invoices.dat";
    private static final String INVOICES_ID_FILE_COORDINATOR = "invoiceIds.cor";

    private static final String SEPARATOR = File.separator;
    private static final String FOLDER = "src" + SEPARATOR + "main" + SEPARATOR
        + "resources" + SEPARATOR + "inFileData" + SEPARATOR;

    public String getInvoicesFilePath() {
        return invoicesFilePath;
    }

    public String getInvoicesIdsFilePath() {
        return invoicesIdsFilePath;
    }

    public static String getDefaultInvoicesFilePath() {
        return FOLDER + INVOICES_FILE;
    }

    public static String getDefaultInvoicesIdsFilePath() {
        return FOLDER + INVOICES_ID_FILE_COORDINATOR;
    }

    public Configuration(String invoicesFilePath, String invoicesIdsFilePath) {
        this.invoicesFilePath = invoicesFilePath;
        this.invoicesIdsFilePath = invoicesIdsFilePath;
    }
}
