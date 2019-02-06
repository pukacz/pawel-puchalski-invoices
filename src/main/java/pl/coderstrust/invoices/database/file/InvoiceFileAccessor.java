package pl.coderstrust.invoices.database.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

class InvoiceFileAccessor {

    private RandomAccessFile file;

    InvoiceFileAccessor(RandomAccessFile file) {
        this.file = file;
    }

    public ArrayList<String> getInvoiceFileLines() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        String line;
        file.seek(0);

        while ((line = file.readLine()) != null && file.getFilePointer() < file.length() + 1) {
            list.add(line);
        }
        return list;
    }

    boolean saveLine(Long invoiceId, String invoiceInJson) throws IOException {
        invalidateLine(invoiceId);
        String line = "" + invoiceId + ": " + invoiceInJson + "\n";
        Long cursor = getPositionOfInvoice(invoiceId);

        file.seek(cursor);
        file.writeBytes(line);
        return true;
    }

    boolean invalidateLine(Long invoiceId) throws IOException {
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
