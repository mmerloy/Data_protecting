import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {
        long number = new Random().nextInt(10) + 10;//генерируем рандомное число
        String message1;//для передачи сообщения Алисы
        boolean loop = true;
        Scanner in = new Scanner(System.in);
        List<Long> first = simpl(number);
        long a1 = getA(first);
        long p1 = getP(first);
        long f1 = getF( a1, p1);
        long d1 = getD(f1);
        long e1 = getE(f1, d1);
        long module1 = getModule(a1, p1);
        System.out.println("Открытый ключ = { " + d1 + ", " + module1 + "}");
        while (loop)
        {
            System.out.println("Алиса хочет отправить сообщение с текстом: ");
            System.out.println("");
            message1 = in.nextLine();
            char[] mesChar = message1.toCharArray();//создаем из строки массив char
            long resultChar;
            StringBuffer decod = new StringBuffer();
            List<Long> codMess = new ArrayList<>();
            for(int i = 0; i < mesChar.length;i++)
            {
                int kodChar = (int) mesChar[i];
                resultChar = 1;
                for(int j = 1; j<=d1; j++)
                {
                    resultChar = (kodChar * resultChar) % module1;//(d*e) mod φ(n) = 1.
                }
                codMess.add(resultChar);
            }
            System.out.println("Алиса отправляет: " );
            for(int i=0;i<codMess.size();i++)
            {
                System.out.print(codMess.get(i)+ " ");
            }
            System.out.println("\n");

            long result1Char;
            for(int i = 0; i<codMess.size();i++) {
                result1Char = 1;
                for (int j = 0; j < e1; j++) {
                    result1Char = (codMess.get(i) * result1Char) % module1;
                }
                char cur = (char) result1Char;
                decod.append(cur);
            }
            System.out.println("Боб расшифровывает сообщение секретным ключом и получает: " + decod);
            System.out.println("Отправить еще сообщение? y/n");
            String s = in.nextLine();
            if (s.equals("n"))
            {
                loop = false;
            }
        }
    }
//нахождение простых чисел до number
    private static List<Long> simpl(long number)
    {
        List<Long> numbers = new ArrayList<>();
        long st = 0;
        long n = 3;
        numbers.add((long)2);
        while (st < number)//проходимся по всем числам массива
        {
            for (int i = 0; i < numbers.size(); i++)
            {
                if (n % numbers.get(i) == 0)
                {
                    break;
                }
                if (numbers.get(i) >= Math.sqrt(n))
                {
                    st++;
                    numbers.add(n);
                    break;
                }
            }
            n += (long) 2;
        }
        return numbers;
    }

//выбор рандомного простого числа из списка
    private static long getA(List<Long> numbers)
    {
        long a;
        Random random1 = new Random();
        a = numbers.get(random1.nextInt(numbers.size() - 2));
        return a;
    }

    private static long getP(List<Long> numbers)
    {
        long p = numbers.get(numbers.size()-1);
        return p;
    }
    //вычисление произведения 2 простых чисел а и p
    private static long getModule(long a, long p)
    {
        long module;
        module = a * p;
        return module;
    }

    private static long getF(long a, long p)//вычисление функции Эйлера
    {
        long f;
        f = (p-1) * (a - 1);
        return f;
    }
    //Выбираем произвольное целое e: 0 < e < n взаимно простое с значением функции Эйлера φ(n).
    //открытый ключ шифра
    private static long getE(long f, long d)
    {
        long e = new Random().nextInt(11);
        while ((((e * d) % f) !=1) || (e==d))
        {
            e++;
        }
        System.out.println(e);
        return e;
    }

    private static long getD(long f)
    {
        int er = new Random().nextInt( 30000);
        int r = (int)(Math.random()*er);
        long d = simpl(er).get(er-r);
        for (long i = 2; i <= f; i++)
        {
            if ((f % i == 0) && (d % i == 0))
            {
                r--;
                d = simpl(er).get( er - r);
                i = 1;
            }
        }
        return d;
    }


}