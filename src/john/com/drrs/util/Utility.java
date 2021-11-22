package john.com.drrs.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;

public class Utility {
    public static String[] getTimeSlotsFromString(String timeSlotsRep, String mode) {
        //available modes: Create, Delete
        System.out.println("Getting time slots from string");
        System.out.println(String.format("Mode: %s", mode));
        //split by spaces
        //turn each number into an index
        //fill the output array with 'available' at each of the indicated indices

        // if in delete mode,

        String[] input = timeSlotsRep.split(" ");
        String[] output = new String[12];

        if (mode.equals("Create")) {
            for (String s : input) {
                if (s != null) {
                    int index = Integer.parseInt(s);
                    output[index] = "Available";
                }
            }
        } else if (mode.equals("Delete")) {
            for (String s : input) {
                if (s!=null) {
                    int index = Integer.parseInt(s);
                    output[index] = "Delete";
                }
            }
        } else if (mode.equals("Book")) {
            for (String s : input) {
                if (s != null) {
                    int index = Integer.parseInt(s);
                    output[index] = "Book";
                }
            }
        }

        System.out.println("Finished getting time slots from string");  //debug
        return output;
    }

    public static Date getDateFromString(String dateRep) throws Exception {
        //date in the form "yyyy mm dd"
        String[] parts = dateRep.split("-");

        Date output = null;
        try {
            output = new Date(Integer.parseInt(parts[0])-1900, Integer.parseInt(parts[1])-1, Integer.parseInt(parts[2]));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("DateUtil - %s", output.toString()));
        return output;
    }

    public static Booking getBookingFromString(String bookingRep) {
        // expected format "id yyyy-mm-dd"
        String[] pieces = bookingRep.split(" ");
        String[] datePieces = pieces[1].split("-");

        Date d = new Date(Integer.parseInt(datePieces[0]), Integer.parseInt(datePieces[1])-1, Integer.parseInt(datePieces[2]));

        Booking output = new Booking(pieces[0], d, Integer.parseInt(pieces[2]), Integer.parseInt(pieces[3]));

        return output;
    }

    public static ArrayList<String> getValidServerNames() {
        ArrayList<String> output = new ArrayList<String>();
        output.add("DVL");
        output.add("KKL");
        output.add("WST");

        return output;
    }

    public static void writeFile(Log log, String fileName) {
        //get the abs path
        String path = FileSystems.getDefault().getPath("src/com/drrs/logs/" + fileName + ".txt").normalize().toAbsolutePath().toString();

        //create new file with abs path and the correct name
        File newFile = new File(path);

        //if file does not exist, create it
        try {
            newFile.createNewFile();
//            System.out.println("File created");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //if file exists, write log to it in append mode
        try {
//            System.out.println("Path : " + path);
            Files.write(Paths.get(path), (log.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
