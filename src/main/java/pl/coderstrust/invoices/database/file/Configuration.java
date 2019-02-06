package pl.coderstrust.invoices.database.file;

import java.io.File;

class Configuration {

    private static final String INVOICES_FILE = "invoices.dat";

     File getFile() {
        return new File(
            "src" + File.separator + "main" + File.separator
                + "resources" + File.separator + INVOICES_FILE);
    }
}
