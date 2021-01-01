package com.studygroup.study.services;

import java.security.SecureRandom;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class GeneratorService {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final String genderKeys = "MFO";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static String randomStringDate(int startYear, int endYear) {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randomIntBetween(startYear, endYear);
        int dayOfYear = randomIntBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));

        gc.set(Calendar.YEAR, year);
        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return gc.get(Calendar.YEAR) + "-" + (gc.get(Calendar.MONTH) + 1) + "-" + gc.get(Calendar.DAY_OF_MONTH);
    }

    public static Character randomGender() {
        return genderKeys.charAt(rnd.nextInt(genderKeys.length()));
    }

    public static Date randomSQLDate(int startYear, int endYear) {
        return Date.valueOf(randomStringDate(startYear, endYear));
    }

    public static int randomIntBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    public static<T> T randomListElement(List<T> list) {
        int indexOfElement = randomIntBetween(0, list.size() - 1);
        return list.get(indexOfElement);
    }

    public static<T> List<T> randomListElements(List<T> list) {
        int quantityOfElements = randomIntBetween(Math.min(1, list.size()), list.size() - 1);
        ArrayList<T> selectedElements = new ArrayList<T>();

        while (selectedElements.size() != quantityOfElements) {
            int indexOfNewElement = randomIntBetween(0, list.size() - 1);
            T selectedElement = list.get(indexOfNewElement);

            if (!selectedElements.contains(selectedElement)) {
                selectedElements.add(list.get(indexOfNewElement));
            }
        }
        return selectedElements;
    }

    public static<T> List<T> randomListElementsWithMaxCount(List<T> list, int max) {
        int quantityOfElements = randomIntBetween(Math.min(1, list.size()), Math.max(1, max));
        ArrayList<T> selectedElements = new ArrayList<T>();

        while (selectedElements.size() != quantityOfElements) {
            int indexOfNewElement = randomIntBetween(0, list.size() - 1);
            T selectedElement = list.get(indexOfNewElement);

            if (!selectedElements.contains(selectedElement)) {
                selectedElements.add(list.get(indexOfNewElement));
            }
        }
        return selectedElements;
    }
}
