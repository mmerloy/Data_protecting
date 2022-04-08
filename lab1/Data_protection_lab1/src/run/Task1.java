package run;
import java.io.*;

public class Task1 
{
 
    public static void main(String[] args) 
    {

        try(FileInputStream fin=new FileInputStream("H:\\8191102\\java\\Data_protection_lab1\\src\\Doc1.docx"))
        {
            byte[] buffer = new byte[fin.available()];
            // считываем буфер
            System.out.println(fin.read(buffer, 0, buffer.length));
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        } 
    } 
} 
