package john.com.drrs.test;

import john.com.drrs.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class Testing {
    public static void main(String[] args) {
        Log l = new Log("servername", "userid", new Date(100, 1, 1), "req", "params", "fail", "res");
        System.out.println(l);
        String fileName = "aaa";
        Log log = l;

        writeFile(l, "admin");


//        String fileName = "aaa";
//        String newFilePath = FileSystems.getDefault().getPath("src/com/drrs/logs").normalize().toAbsolutePath().toString();
//
//        File newFile = new File(newFilePath + fileName + ".txt");
//
//        try {
//            newFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String path = "D:\\Coding\\SOEN\\jaxrpc\\src\\com\\drrs\\logs\\logs.txt";
//
//        try {
//            Files.write(Paths.get(path), (l.toString()+"\n").getBytes(), StandardOpenOption.APPEND);
//        }catch (IOException e) {
//            //exception handling left as an exercise for the reader
//        }
//
//        String absolutePath = FileSystems.getDefault().getPath("src/com/drrs/logs").normalize().toAbsolutePath().toString();
//
//        File abs1 = new File(absolutePath);
//
//
//        File f = new File("D:\\Coding\\SOEN\\jaxrpc\\src\\com\\drrs\\logs");
//        File[] arr = abs1.listFiles();
//        for (File x : arr) {
//            System.out.println(x.getAbsolutePath());
//        }

    }

    //take log and user ID
    //get abs path
    //if file does not exist, create it
    //else write in append mode
    static void writeFile(Log log, String fileName) {
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
