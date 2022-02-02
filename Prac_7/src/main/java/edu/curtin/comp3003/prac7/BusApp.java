/********************************************************************************
 * Author: George Aziz
 * Purpose: Main Program that runs BusParser and parses content from BusFile.txt
 * Date Last Modified: 01/10/2021
 *******************************************************************************/

package edu.curtin.comp3003.prac7;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BusApp
{
    public static void main(String[] args) 
    {
        try
        {
            BusParser parser = new BusParser(new FileInputStream("BusFile.txt"));
            System.out.println("===============Parser Result===============\n");
            parser.Input();
        }
        catch(ParseException ex)
        {
            System.out.println("ERROR: Parsing error!");
            System.out.println(ex.getMessage());
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("ERROR: File could not be found!");
            System.out.println(ex.getMessage());
        }
    }
}

