package commons;


import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    /**
     * writes user's favorite boards and their hashed passwords to file on their computer
     * @throws IOException exception for input
     */
    public void writeToCsv() throws IOException {
        File dir = new File(System.getProperty("user.home") + "/Documents/Talio");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "data.csv");
        if(file.exists()){file.delete();}
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(this.getBoards().toString());
        }
    }


    /**
     * reads user's saved data(if they exist) from the local file
     * @return list of names of baords
     * @throws IOException shouldn't happen
     */
    public List<String> readFromCsv() throws IOException {
        List<String> boardNames=new ArrayList<>();
        if(isFirstTime()) return boardNames;
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

                    boardNames.add(key);
                    int value = Integer.parseInt(tokens[1].trim());
                    data.put(key, value);
                }
            }
        }
        this.setBoards(data);
        return boardNames;
    }

    /**
     * @return if the user ever used the application before
     */
    public boolean isFirstTime() {
        File file = new File(System.getProperty("user.home") + "/Documents/Talio/first_time");
        if (!file.exists()) {
            file.mkdirs();
            return true;
        }
        return false;
    }


    /**
     * @return returns hashmap of boards
     */
    public HashMap<String, Integer> getBoards() {
        return boards;
    }

    /**
     * @param boards board hashmap setter
     */
    public void setBoards(HashMap<String, Integer> boards) {
        this.boards = boards;
    }
}
