package com.mindhub.homebanking.utils;

public class CardUtils {



    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String getCardRandomNumber() {
        return getRandomNumber(1,9999)+"-"+getRandomNumber(1,9999)+"-"+getRandomNumber(1,9999)+"-"+getRandomNumber(1,9999);
    }
}
