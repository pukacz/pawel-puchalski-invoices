package pl.coderstrust.invoices.database.file;

import java.io.File;

class Configuration {

    private static final String INVOICES_FILE = "invoices.dat";
    private static final String INVOICES_ID_FILE_COORDINATOR = "invoiceIds.cor";

    private static final String SEPARATOR = File.separator;
    private static final String FOLDER = "src" + SEPARATOR + "main" + SEPARATOR
        + "resources" + SEPARATOR + "inFileData" + SEPARATOR;

    File getInvoicesFile() {
        return new File(FOLDER + INVOICES_FILE);
    }

    File getInvoicesIdsFile() {
        return new File(FOLDER + INVOICES_ID_FILE_COORDINATOR);
    }
}
