package com.drrs.client;

import com.drrs.util.Log;
import com.drrs.util.Utility;
import com.drrs.ws.Primary;
import com.drrs.ws.PrimaryImplService;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class AdminClient {
    public static void main(String[] args) throws Exception {
        String adminID = "DVLA0001";

        PrimaryImplService primaryService = new PrimaryImplService();
        Primary primary = primaryService.getPrimaryImplPort();

        System.out.println("Admin Client");
        boolean go = true;
        Scanner sc = new Scanner(System.in);
        String input = null;
        while (go) {
            System.out.println("Please Enter a choice:\n1. Create a room\n2. Delete a room\n3. Quit\n");
            String choice = sc.nextLine();
            switch (choice) {
                case "1": {
                    System.out.println("Create a room");
                    System.out.println("Enter a room number");
                    int roomNumber = Integer.parseInt(sc.nextLine());


                    System.out.println("Enter a date in ISO format");
                    String dateRep = sc.nextLine();

                    System.out.println("Enter a list of time slots separated by spaces");
                    String timeSlotsRep = sc.nextLine();

                    boolean result = primary.createRoom(adminID, roomNumber, dateRep, timeSlotsRep);
                    System.out.println(result);
                    String params = String.format(" RoomNumber: %s, Date: %s, TimeSlots: [%s]", roomNumber, dateRep, timeSlotsRep);
                    //updateLogs(adminID.substring(0,3), adminID, Utility.getDateFromString(dateRep), "Create Room", "test", result+"", result+"");
                    Log log = new Log(adminID.substring(0, 3), adminID, Utility.getDateFromString(dateRep), "CreateRoom", params, result + "", result + "");
                    AdminClient.writeFile(log, adminID);
                    break;
                }
                case "2": {
                    System.out.println("Delete a room");
                    System.out.println("Enter a room number");
                    int roomNumber = Integer.parseInt(sc.nextLine());


                    System.out.println("Enter a date in ISO format");
                    String dateRep = sc.nextLine();

                    System.out.println("Enter a list of time slots separated by spaces");
                    String timeSlotsRep = sc.nextLine();

                    boolean result = primary.deleteRoom(adminID, roomNumber, dateRep, timeSlotsRep);
                    System.out.println(result);
                    //updateLogs(adminID.substring(0,3), adminID, Utility.getDateFromString(dateRep), "Delete Room", "test", result+"", result+"");
                    String params = String.format(" RoomNumber: %s, Date: %s, TimeSlots: [%s]", roomNumber, dateRep, timeSlotsRep);
                    //updateLogs(adminID.substring(0,3), adminID, Utility.getDateFromString(dateRep), "Create Room", "test", result+"", result+"");
                    Log log = new Log(adminID.substring(0, 3), adminID, Utility.getDateFromString(dateRep), "DeleteRoom", params, result + "", result + "");
                    AdminClient.writeFile(log, adminID);
                    break;
                }
                case "3":
                    System.out.println("Quit");
                    go = false;
                    break;
            }
        }


    }

    static void writeFile(Log log, String fileName) {
        //get the abs path
        String path = FileSystems.getDefault().getPath("src/com/drrs/logs/" + fileName + ".txt").normalize().toAbsolutePath().toString();
        System.out.println(path);
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
