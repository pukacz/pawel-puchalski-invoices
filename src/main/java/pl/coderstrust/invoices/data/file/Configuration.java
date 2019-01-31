package pl.coderstrust.invoices.data.file;

import java.io.File;

class Configuration {

    private static final String INVOICES_FILE = "invoices.dat";
    private static final String FOLDER = "localData";

    String getInvoicesFilePath() {
        File dataFolder = new File(FOLDER);
        return dataFolder.getAbsolutePath() + "\\" + INVOICES_FILE;
    }
}
