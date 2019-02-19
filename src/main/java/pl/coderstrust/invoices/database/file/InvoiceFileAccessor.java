package pl.coderstrust.invoices.database.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

class InvoiceFileAccessor {

    private File invoicesFile;

    InvoiceFileAccessor(Configuration configuration) throws IOException {
        this.invoicesFile = new File(configuration.getInvoicesFilePath());

        if (!invoicesFile.exists()) {
            invoicesFile.createNewFile();
        }
    }

    ArrayList<String> readLines() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        String line;

        try (RandomAccessFile file = new RandomAccessFile(invoicesFile, "r")) {
            file.seek(0);
            while ((line = file.readLine()) != null && file.getFilePointer() < file.length() + 1) {
                list.add(line);
            }
        }
        return list;
    }

    void saveLine(String line) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(invoicesFile, "rw")) {
            long cursor = file.length();
            file.seek(cursor);
            file.writeBytes(line + "\n");
        }
    }

    void invalidateLine(Long invoiceId) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(invoicesFile, "rw")) {
            file.seek(0);
            file.seek(0);
            Long cursor = getPositionOfInvoice(invoiceId);

            if (!cursor.equals(file.length())) {
                file.seek(cursor);
                String line = file.readLine();

                file.seek(file.getFilePointer() - line.length() - 1);
                for (int i = 0; i < line.length(); i++) {
                    file.writeBytes(" ");
                }
                file.writeBytes("\n");
            }
        }
    }

    Long getPositionOfInvoice(Long invoiceId) throws IOException {
        String line;
        String idInString;
        int colonIndex;
        long cursor;

        try (RandomAccessFile file = new RandomAccessFile(invoicesFile, "rw")) {
            cursor = file.getFilePointer();

            while ((line = file.readLine()) != null && file.getFilePointer() < file.length() + 1) {

                if ((colonIndex = line.indexOf(": ")) > 0) {
                    idInString = line.substring(0, colonIndex);

                    if (invoiceId.equals(Long.parseLong(idInString))) {
                        return cursor;
                    }
                    cursor = file.getFilePointer();
                } else {
                    cursor += line.length() + 1;
                }
            }
        }
        return cursor;
    }
}
