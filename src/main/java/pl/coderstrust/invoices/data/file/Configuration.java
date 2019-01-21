package pl.coderstrust.invoices.data.file;

import java.io.File;

public class Configuration {

    private String invoicesFile = "invoices.dat";
    private String folder = "localData";

    public String getInvoicesFile() {
        return getFilePath(invoicesFile);
    }

    private String getFilePath(String fileName) {
        File localDataDirectory = new File(folder);
        return localDataDirectory.getAbsolutePath() + "\\" + fileName;
    }
}
