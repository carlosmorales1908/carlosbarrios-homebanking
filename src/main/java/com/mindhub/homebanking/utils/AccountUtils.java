package com.mindhub.homebanking.utils;

public class AccountUtils {

    public static int getAccountRandomNumber() {
        return (int) ((Math.random() * (99999999 - 1)) + 1);
    }
}
