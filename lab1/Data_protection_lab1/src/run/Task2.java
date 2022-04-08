package run;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class Task2 
{
	public static int remove_Duplicate_Elements(byte[] buffer, int n)
	{  
        if (n==0 || n==1)
        {  
            return n;  
        }  
        int[] tempA = new int[n];  
        int j = 0;  
        for (int i=0; i<n-1; i++){  
            if (buffer[i] != buffer[i+1]){  
                tempA[j++] = buffer[i];  
            }  
         }  
        tempA[j++] = buffer[n-1];       
        for (int i=0; i<j; i++)
        {  
            buffer[i] = (byte) tempA[i];  
        }  
        return j;  
    } 
	

				
	public static void main(String[] args) 
    {
        try(FileInputStream fin = new FileInputStream("H:\\8191102\\java\\Data_protection_lab1\\src\\doc2.txt"))
        {
            byte[] buffer = new byte[fin.available()];//available()-Возвращает оценку количества байтов, которые могут быть прочитаны 
            fin.read(buffer, 0, buffer.length); // считываем буфер
            byte[] copyBuffer = new byte[buffer.length];
            System.arraycopy(buffer, 0, copyBuffer, 0, buffer.length);
            Arrays.sort(copyBuffer); 
            int length = remove_Duplicate_Elements(copyBuffer, copyBuffer.length); 
            
            for (int i = 0;i < length;i ++)
            {
            	int count = 0;
            	for (int j = 0; j < buffer.length;j ++)
            	{
            		if (copyBuffer[i] == buffer[j])		{count ++;}
            	}
            	System.out.println("Byte "+ copyBuffer[i] + ":"+ count);
            	//System.out.println("Byte "+ (char)copyBuffer[i] + ":"+ count);
            }
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
