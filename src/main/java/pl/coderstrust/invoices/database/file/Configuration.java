package pl.coderstrust.invoices.database.file;

import java.io.File;

class Configuration {

    private static final String INVOICES_FILE = "invoices.dat";
    private static final String DIRECTORY = "src/main/resources/inFileData";

    String getInvoicesFilePath() {
        File dataFolder = new File(DIRECTORY);
        return dataFolder.getAbsolutePath() + "\\" + INVOICES_FILE;
    }
}
