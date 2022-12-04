package com.cannabank.cannabank.utils;

import java.util.Random;

public class BankNum {
    public static String generateAccountNumber() {
        String start = "BE";
        Random value = new Random();

        // Generate two values to append to 'BE'
        int r1 = value.nextInt(10);
        int r2 = value.nextInt(10);
        start += Integer.toString(r1) + Integer.toString(r2) + " ";

        int count = 0;
        int n = 0;
        for (int i = 0; i < 12; i++) {
            if (count == 4) {
                start += " ";
                count = 0;
            } else
                n = value.nextInt(10);
            start += Integer.toString(n);
            count++;

        }
        return start;
    }
}
