package pl.coderstrust.invoices.data.file;

import java.io.File;

class Configuration {

    private String invoicesFile = "invoices.dat";
    private String folder = "localData";

    String getInvoicesFile() {
        File dataFolder = new File(folder);
        return dataFolder.getAbsolutePath() + "\\" + invoicesFile;
    }
}
