package pl.coderstrust.invoices.data.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

class FileHelper {

    private RandomAccessFile file;

    FileHelper(RandomAccessFile file) {
        this.file = file;
    }

    public ArrayList<String> getInvoices() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        String line;
        file.seek(0);

        while ((line = file.readLine()) != null && file.getFilePointer() < file.length() + 1) {
            list.add(line);
        }
        return list;
    }

    boolean saveInvoice(Long invoiceId, String invoiceInJson) throws IOException {

        deleteInvoice(invoiceId);

        String line = "" + invoiceId + ": " + invoiceInJson + "\n";
        Long cursor = getPositionOfInvoice(invoiceId);

        file.seek(cursor);
        file.writeBytes(line);
        return true;
    }

    boolean deleteInvoice(Long invoiceId) throws IOException {
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
        return true;
    }

    private Long getPositionOfInvoice(Long invoiceId) throws IOException {

        String line;
        String idInString;
        int colonIndex;
        long cursor = file.getFilePointer();

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
        return cursor;
    }
}
