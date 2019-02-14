package pl.coderstrust.invoices.database.file;

import java.io.File;

class Configuration {

    private File invoicesFile;
    private File invoicesIdsFile;

    private static final String INVOICES_FILE = "invoices.dat";
    private static final String INVOICES_ID_FILE_COORDINATOR = "invoiceIds.cor";

    private static final String SEPARATOR = File.separator;
    private static final String FOLDER = "src" + SEPARATOR + "main" + SEPARATOR
        + "resources" + SEPARATOR + "inFileData" + SEPARATOR;

    public static File invoicesFile() {
        return new File(FOLDER + INVOICES_FILE);
    }

    public static File invoicesIdsFile() {
        return new File(FOLDER + INVOICES_ID_FILE_COORDINATOR);
    }

    public Configuration(File invoicesFile, File invoicesIdsFile) {
        this.invoicesFile = invoicesFile;
        this.invoicesIdsFile = invoicesIdsFile;
    }

    public File getInvoicesFile() {
        return invoicesFile;
    }

    public File getInvoicesIdsFile() {
        return invoicesIdsFile;
    }
}
