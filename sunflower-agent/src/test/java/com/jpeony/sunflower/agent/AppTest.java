package com.jpeony.sunflower.agent;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public static void main(String[] args) {
        String str = "*";
        String[] strArr = str.split("#");
        Integer i = strArr.length == 2 ? Integer.parseInt(strArr[0]) : 0;
        System.out.println(i);
    }
}
