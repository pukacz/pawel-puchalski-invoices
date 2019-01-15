package pl.coderstrust.invoices.data.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class FileHelper {

    public List<String> readLinesFromFile(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
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

    public void appendLineToFile(String line, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.newLine();
            writer.write(line);
        }
    }

    public void removeEmptyLinesFromFile(String fileName) throws IOException {
        try (Scanner scanner = new Scanner(new FileReader(fileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter("temp.txt"))) {
            ArrayList<String> tempArray = new ArrayList<>();
            boolean hasEmptyLine = false;

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (!line.matches("\\s")) {
                    tempArray.add(line);
                } else hasEmptyLine = true;
            }

            if (hasEmptyLine) {
                for (String line : tempArray) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        }
    }
}
