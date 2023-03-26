package commons;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.net.SocketException;
import java.util.List;
public class User {



    private HashMap<String, Integer> boards; // board and password hashed
    public User(){
        this.boards = new HashMap<>();
    }
    public void addBoard(String key, int password){
        boards.put(key,password);
    }

    public void deleteBoard(String key){
        boards.remove(key);
        }

    public void writeToCsv() throws IOException {
        File dir = new File(System.getProperty("user.home") + "/Documents/Talio");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "data.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(this.getBoards().toString());
        }
    }


    public void readFromCsv() throws IOException {
        if(isFirstTime()) return;
        System.out.println("not a virgin");
        HashMap<String, Integer> data = new HashMap<>();
        File dir = new File(System.getProperty("user.home") + "/Documents/Talio/data.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(dir))) {
            String line= reader.readLine();
            line= line.substring(1,line.length()-1);
            String[] boards = line.split(",* ");
            for(String string : boards) {
                String[] tokens = string.split("=");
                if (tokens.length == 2) {

                    String key = tokens[0].trim();

                    int value = Integer.parseInt(tokens[1].trim());
                    data.put(key, value);
                }
            }
        }
         this.setBoards(data);
        System.out.println(data);
    }

    public boolean isFirstTime() {
        File file = new File(System.getProperty("user.home") + "/Documents/Talio/first_time");
        if (!file.exists()) {
            file.mkdirs();
            return true;
        }
       return false;
    }

    public HashMap<String, Integer> getBoards() {
        return boards;
    }

    public void setBoards(HashMap<String, Integer> boards) {
        this.boards = boards;
    }
}
