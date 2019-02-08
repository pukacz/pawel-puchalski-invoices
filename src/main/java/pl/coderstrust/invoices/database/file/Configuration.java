package pl.coderstrust.invoices.database.file;

import java.io.File;

class Configuration {

    private static final String INVOICES_FILE = "invoices.datxx";
    private static final String FOLDER = "src" + File.separator + "main" + File.separator
        + "resources" + File.separator;

    File getInvoicesFile() {
        return new File(FOLDER + INVOICES_FILE);
    }
}
