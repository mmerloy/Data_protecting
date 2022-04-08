package IO;

import java.io.*;
import java.util.Scanner;

public class ReaderWriter {

    public static void write(String filename, String text) {
        try {
            Writer writer = new FileWriter("./src/Files/" + filename);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static String read(String filename) {
        String text = "";
        try {
            Reader reader = new FileReader("./src/Files/" + filename);
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                text += scanner.nextLine();
                text += "\n";
            }
            scanner.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        return text;
    }
}