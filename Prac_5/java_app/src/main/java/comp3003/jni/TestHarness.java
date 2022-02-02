package comp3003.jni;

import java.util.LinkedList;
import java.util.List;

/*********************************************************
 * Author: George Aziz
 * Purpose: Test Harness for ConsoleIO JNI Implementation
 * Date Last Modified: 19/09/2021
 ********************************************************/
public class TestHarness
{
    //Main method for test harness
    public static void main(String[] args)
    {
        System.out.println("======Start of Test Harness======");
        //TEST #1 for the read() method
        System.out.println("[Test #1] read()");
        System.out.println("Default Value: -1");
        System.out.println("Input Value: ");
        double retVal = ConsoleIO.read(-1); //Hardcoded -1 as default value for any error
        System.out.println("Returned Value: " + retVal + "\n");

        //TEST #2 for the printStr() method
        System.out.println("[Test 2] printStr()");
        ConsoleIO.printStr("Test_String 123.456"); //Hardcoded input string

        //TEST #3 for the printlist() method
        System.out.println("\n[Test 3] printList()");
        System.out.println("Inputting List - [Hello, my, name, is, George!]");
        List<String> list = new LinkedList<>(); //Hardcoded list to be imported to method
        list.add("Hello");
        list.add("my");
        list.add("name");
        list.add("is");
        list.add("George!");
        ConsoleIO.printList(list);
        System.out.println("======End of Test Harness======");
    }

}
