package com.github.siga111.customELV.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates array of mock data.
 */
public class MockData {

    public static List<Data> getMockData() {

        List<Data> data = new ArrayList<Data>();
        data.add(new Data("ONE"));
        data.add(new Data("TWO"));
        data.add(new Data("THREE"));
        data.add(new Data("FOUR"));
        data.add(new Data("FIVE"));
        data.add(new Data("SUX"));
        data.add(new Data("SEVEN"));
        data.add(new Data("EIGHT"));
        data.add(new Data("NINE"));

        return data;
    }

}
