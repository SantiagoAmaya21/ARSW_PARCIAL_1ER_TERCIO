package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.List;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;


    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count, int N) {
        List<SumThread> threads = new ArrayList<>();
        Object lockObject = new Object();
        byte[] digits = new byte[count];
        boolean threadIsRunning = true;
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        createThread(start, DigitsPerSum, N, threads, lockObject);

        try {
            while (threadIsRunning){
                threadIsRunning = false;
                for (SumThread t : threads){
                    if (t.isAlive()) {
                        threadIsRunning = true;
                        break;
                    }
                }
                Thread.sleep(5000);
                stopAndExecute(threads,lockObject);
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        getResults(threads, digits, DigitsPerSum);


        return digits;
    }

    public static void createThread(int start, int digitsPerSum, int N, List<SumThread> threads, Object lockObject){
        for (int i = 0; i < N; i++) {
            SumThread t = new SumThread(start, digitsPerSum, lockObject);
            start += digitsPerSum;
            threads.add(t);
            t.start();
        }
    }


    public static void getResults(List<SumThread> threads, byte[] digits, int digitsPerSum){
        int total_amount = 0;
        for (SumThread t : threads){
            try {
                t.join();

            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private static void stopAndExecute(List<SumThread> threads, Object lockObject){

    }

}
