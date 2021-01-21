package com.katyshevtseva.kikiorg.core;

public class CoreUtils {

    public static int getRowByIndexAndColumnNum(int index, int columnNum) {
        return index / columnNum;
    }

    public static int getColumnByIndexAndColumnNum(int index, int columnNum) {
        return index % columnNum;
    }
}
