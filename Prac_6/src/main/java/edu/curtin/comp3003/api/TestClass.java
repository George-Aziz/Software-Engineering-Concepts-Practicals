package edu.curtin.comp3003.api;

/*******************************************
 * Author: George Aziz
 * Purpose: Test Class for Reflection API
 * Date Last Modified: 22/09/2021
 ******************************************/
public class TestClass
{
    private String name;

    public TestClass(String name)
    {
        this.name = name;
    }

    public static void intPlus1(int value1)
    {
        int retVal = value1 + 1;
    }

    public int intPlus2(int value1)
    {
        return value1 + 2;
    }

    public int intPlus3(int value1)
    {
        return value1 + 3;
    }

    public int intPlus4(int value1)
    {
        return value1 + 4;
    }

    public static int intPlus5(int value1)
    {
        return value1 + 5;
    }

    public int intPlus6(int value1, int value2)
    {
        return value1 * value2 + 6;
    }

    public String stringMethod1(String value1)
    {
        return value1;
    }

    public static String stringMethod2(String value1)
    {
        return value1 + 1;
    }
}