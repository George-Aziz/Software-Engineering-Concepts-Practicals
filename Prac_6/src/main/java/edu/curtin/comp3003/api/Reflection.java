package edu.curtin.comp3003.api;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.lang.*;
import java.lang.reflect.*;

/*******************************************
 * Author: George Aziz
 * Purpose: API Class that uses reflection
 * Date Last Modified: 29/09/2021
 ******************************************/
public class Reflection {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            String pluginName = inputString("Class name: edu.curtin.comp3003.api.");
            pluginName = "edu.curtin.comp3003.api." + pluginName;
            Class<?> pluginClass = Class.forName(pluginName); //Finds Class with user input
            //Finds constructor that takes a single String within class and if nothing found throws exception
            Constructor<?> stringConstructor = pluginClass.getConstructor(String.class);
            //String input only if constructor is found
            String stringInput = inputString("Please input a string to be used within class constructor: ");
            //Creates new object with string
            Object newObject = stringConstructor.newInstance(stringInput);

            Method[] methods = newObject.getClass().getMethods();
            List<Method> intMethods = new ArrayList<>();
            //Filters out methods to only non-static single parameter integer methods
            for (Method curMethod : methods) {
                if (!Modifier.isStatic(curMethod.getModifiers()) && curMethod.getParameterCount() == 1
                        && curMethod.getParameterTypes()[0] == int.class) {
                    intMethods.add(curMethod);
                }
            }

            //If no methods found then throw an exception
            if (intMethods.size() == 0) {
                throw new ReflectiveOperationException("Method that takes a single integer and is non-static could not be found!");
            }
            //Output for all methods that take single integer parameters & non-static in a specified format
            System.out.println("List of all non-static methods that take a single integer parameter:\n");
            for (int i = 0; i < intMethods.size(); i++) {
                System.out.println("[" + i + "] " + intMethods.get(i).getName());
            }
            int methodOption = inputMethodOption("Please select one of the methods above via number option:", intMethods.size() - 1);
            int param = inputParam();

            Object returnValue = intMethods.get(methodOption).invoke(newObject, param);
            System.out.println("Method output: " + returnValue.toString());
        } catch (ReflectiveOperationException ex) {
            System.out.println("[ReflectiveOperationException]\n" + ex);
        } catch (RuntimeException ex) {
            System.out.println("[RuntimeException]\n" + ex);
        }
    }

    //Method responsible for string input for class name and string for constructor
    private static String inputString(String prompt) {
        String outputPrompt = prompt;
        String retString = null;
        do {
            System.out.println(outputPrompt);
            outputPrompt = "ERROR: Input cannot be empty or null\n" + prompt;
            retString = sc.next();
        }
        while (retString == null || retString.isEmpty());

        return retString;
    }

    //Method responsible for taking the method option with boundaries from 0 - Max imported value
    private static int inputMethodOption(String prompt, int max) {
        String outputString;
        int integerInput = -1;
        outputString = prompt; //The output is set to the prompt that is imported when submodule is called
        do {
            try {
                System.out.println(outputString); //Outputs the prompt for the user
                outputString = "ERROR: Option must be between 0 and " + max + " (Inclusive)\n" + prompt;
                integerInput = sc.nextInt(); //User inputs an integer
            } catch (InputMismatchException e) {
                sc.nextLine(); //Advances scanner to the next line
            }
        }
        while ((integerInput < 0 || integerInput > max)); //Validation boundaries for integers that get imported
        return integerInput;
    }

    //Method responsible for taking the parameter to be passed to a method
    private static int inputParam()
    {
        boolean validInput = false;
        int param = Integer.MIN_VALUE;
        do {
            try {
                System.out.println("Please input an integer: ");
                param = sc.nextInt();
                validInput = true;
            } catch (InputMismatchException ex) {
                System.out.println("Please input integers only!");
                sc.nextLine(); //Advances scanner to the next line
            }
        } while (!validInput);
        return param;
    }
}


