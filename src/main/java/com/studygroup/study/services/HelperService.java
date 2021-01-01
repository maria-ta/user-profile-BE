package com.studygroup.study.services;

import java.util.LinkedList;
import java.util.List;

public class HelperService {
    public static<T> List<T> getListFromIterable(Iterable<T> iterable) {
        java.util.List<T> newList = new LinkedList<T>();
        for (T e: iterable) {
            newList.add(e);
        }
        return newList;
    }
}
