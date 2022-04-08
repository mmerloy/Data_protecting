package run;

import java.io.*;
import java.util.Scanner;
import java.io.*;

public class Task3 
{

    private static final String test_file_path = "H:\\8191102\\java\\Data_protection_lab1\\src\\doc2.txt";
    private static final String key_path = "H:\\8191102\\java\\Data_protection_lab1\\src\\key.txt";
    private static final int column_total_count = 5;

    public static void main(String[] args) {
        encrypt(test_file_path, key_path);

        System.out.println("Encrypt done ");
        Scanner scanner = new Scanner(System.in);
        scanner.hasNextLine();

        decrypt(test_file_path, key_path);
        System.out.println("Decrypt done ");
    }

    public static byte[][] get_table(String path, String path_key) throws FileNotFoundException, IOException {
        FileInputStream reader = new FileInputStream(path);
        int bytes_total_count = reader.available();

        final int column_len = bytes_total_count % column_total_count != 0 ?
                bytes_total_count / column_total_count + 1 :
                bytes_total_count / column_total_count;
        byte[][] table = new byte[column_total_count][column_len];


        int b;
        int column = 0, row = 0;
        while ((b = reader.read()) != -1) {
            table[column][row] = (byte) b;
            ++column;
            if (column == column_total_count) { //need test
                column = 0;
                ++row;
            }
        }
        reader.close();

        if (column != 0)
            for (int i = column; i < column_total_count; ++i)
                table[i][row] = (byte) 'z';

        return table;
    }

    private static void print_table_to_file(String path, byte[][] table, int column_len) throws IOException {
        FileOutputStream writer = new FileOutputStream(path);
        for (int j = 0; j < column_len; ++j)
            for (int i = 0; i < column_total_count; ++i)
                writer.write(table[i][j]);
    }
    
    public static void encrypt(String path, String path_key) {
        try {
            var table = get_table(path, path_key);

            int column_len = table[0].length;

            byte[][] copy_table = new byte[column_total_count][column_len];
            for (int i = 0; i < column_total_count; ++i)
                System.arraycopy(table[i], 0, copy_table[i], 0, column_len);


            Scanner key_scanner = new Scanner(new File(path_key));
            int old_column_place = 0;
            while (key_scanner.hasNextInt()) {
                int new_column_place = key_scanner.nextInt() - 1;
                table[new_column_place] = copy_table[old_column_place];
                ++old_column_place;
            }
            key_scanner.close();

            print_table_to_file(path, table, column_len);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }



    public static void decrypt(String path, String path_key) {
        try {
            var table = get_table(path, path_key);

            int column_len = table[0].length;

            byte[][] copy_table = new byte[column_total_count][column_len];
            for (int i = 0; i < column_total_count; ++i)
                System.arraycopy(table[i], 0, copy_table[i], 0, column_len);

            Scanner key_scanner = new Scanner(new File(path_key));
            int old_column_place = 0;
            while (key_scanner.hasNextInt()) {
                int new_column_place = key_scanner.nextInt() - 1;
                table[old_column_place] = copy_table[new_column_place];
                ++old_column_place;
            }
            key_scanner.close();

            print_table_to_file(path, table, column_len);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

