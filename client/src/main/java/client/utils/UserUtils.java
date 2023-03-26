package client.utils;

import commons.Board;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class UserUtils {
    public static void writeToCsv(String filename, HashMap<String, Integer> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                    writer.write(data.toString());
            }
        }


    public static HashMap<String, Integer> readFromCsv(String filename) throws IOException {
        HashMap<String, Integer> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 2) {
                    String key = tokens[0].trim();
                    int value = Integer.parseInt(tokens[1].trim());
                    data.put(key, value);
                }
            }
        }
        return data;
    }
}
