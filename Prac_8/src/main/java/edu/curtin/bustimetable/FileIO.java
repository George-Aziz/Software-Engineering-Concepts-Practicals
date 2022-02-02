package edu.curtin.bustimetable;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

/*****************************************************************************************************
 * Author: George Aziz
 * Purpose: Performs the reading/parsing and writing of the CSV files containing timetable entries
 * Date Last Modified: 04/10/2021
 * NOTE: This class has been provided by the unit to be used for this practical
 ****************************************************************************************************/
public class FileIO
{

    //Loads a bus timetable from a given CSV file
    public List<TimetableEntry> load(File file, Charset encoding) throws IOException, TimetableFormatException
    {
        List<TimetableEntry> entries = new ArrayList<>();
        //Decoder for file reading
        CharsetDecoder decoder = encoding.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);

        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), decoder);
        try(BufferedReader br = new BufferedReader(isr)) //Reads file with user chosen encoding
        {
            String line;
            while((line = br.readLine()) != null)
            {
                String[] fields = line.split(",");
                if(fields.length == 5)
                {
                    try
                    {
                        String routeId = fields[0];
                        String from = fields[1];
                        String destination = fields[2];
                        LocalTime departureTime = LocalTime.parse(fields[3]);
                        Duration duration = Duration.ofMinutes(Integer.parseInt(fields[4]));

                        entries.add(new TimetableEntry(routeId, from, destination, departureTime, duration));
                    }
                    catch(DateTimeParseException e)
                    {
                        throw new TimetableFormatException(String.format(
                                "Invalid departure time: '%s'", fields[3]), e);
                    }
                    catch(NumberFormatException e)
                    {
                        throw new TimetableFormatException(String.format(
                                "Invalid duration: '%s'", fields[4]), e);
                    }
                }
            }
        }
        return entries;
    }

    // Writes a bus timetable to a given CSV file in a specified encoding UTF-8, UTF-16 or UTF-32
    public void save(File file, List<TimetableEntry> entries, Charset encoding) throws IOException
    {
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
             BufferedWriter writer = new BufferedWriter(osw)) {

            for(TimetableEntry entry : entries) {
                writer.write(entry.getRouteId().replace(",", "") + "," +
                        entry.getFrom().replace(",", "") + "," +
                        entry.getDestination().replace(",", "") + "," +
                        entry.getDepartureTime().toString() + "," +
                        entry.getDuration().toMinutes() + "\n");
            }
        }
    }
}
