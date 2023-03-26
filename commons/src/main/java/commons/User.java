package commons;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
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




}
