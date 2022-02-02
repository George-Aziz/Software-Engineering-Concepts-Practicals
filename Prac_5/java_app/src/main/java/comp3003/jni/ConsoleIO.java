package comp3003.jni;

import java.util.List;

/*************************************************************
 * Author: George Aziz
 * Purpose: Java method headers for JNI C Code Implementation
 * Date Last Modified: 19/09/2021
 ************************************************************/
public class ConsoleIO
{
    //Import of c code implementation of the java methods
    static
    {
        System.loadLibrary("c_library");
    }

    //Method headers
    public native static double read(double defaultValue);
    public native static void printStr(String text);
    public native static void printList(List<String> list);
}
