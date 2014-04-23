package com.github.siga111.customELV.app;

/**
 * Some mock data. Just for test.
 */
public class Data {

    public String title;

    public Data(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Data{" +
                "title='" + title + '\'' +
                '}';
    }
}
