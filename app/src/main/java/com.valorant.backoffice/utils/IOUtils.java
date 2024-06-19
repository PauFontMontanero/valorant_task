package com.valorant.backoffice.utils;

import com.valorant.backoffice.exceptions.BackOfficeException;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    public static String readLine(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new BackOfficeException("Error while reading input", e);
        }
    }
}
