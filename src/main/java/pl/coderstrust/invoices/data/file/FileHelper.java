package pl.coderstrust.invoices.data.file;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

class FileHelper {

    public static void main(String[] args) throws IOException {
        FileHelper f = new FileHelper();

//        ArrayList <String>list = new ArrayList(Arrays.asList("Iwona", "Grzesiek", "Zyrafa"));
//        f.writeLinesToFile(list, "myNewFile");

//        ArrayList<String> list2 = (ArrayList) f.readLinesFromFile("myNewFile");
//        System.out.println(list2);

//        f.removeEmptyLineFromFile("myNewFile");
        f.removeLineFromFile("Grzesiek", "myNewFile");
    }

    public List<String> readLinesFromFile(String fileName) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public void writeLinesToFile(List<String> list, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            for (int i = 0; i < list.size(); i++) {
                writer.write(list.get(i));
                if (i < list.size() - 1) {
                    writer.newLine();
                }
            }
        }
    }

    public void removeLineFromFile(String line, String fileName) throws IOException {
        File inputFile = new File(fileName);
        File tempFile = new File("tempFile");
        String lineFromFile;
        boolean isFirstLine = true;

        ArrayList<String> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            while ((lineFromFile = reader.readLine()) != null) {
                if (!lineFromFile.equals(line)) {

                    list.add(lineFromFile);

                    if (isFirstLine) {
                        writer.write(lineFromFile);
                        isFirstLine = false;
                    } else {
                        writer.newLine();
                        writer.write(lineFromFile);
                    }
                }
            }
        }

        Files.move(tempFile.toPath(), inputFile.toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
    }

    public void removeEmptyLinesFromFile(String fileName) throws IOException {
        removeLineFromFile("", fileName);
    }

    public void appendLineToFile(String line, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.newLine();
            writer.write(line);
        }
    }
}
