package commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.net.SocketException;
import java.util.List;

public class User {

    private List<Board> userBoards;
    private String sn;

    /**
     * testing method - development in progress
     * @throws SocketException exception
     */
    public final void test() throws SocketException {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            sn = getSerialNumberWindows();
        } else if (osName.startsWith("Mac")) {
            sn = getSerialNumberMac();
        } else if (osName.startsWith("Linux")) {
            System.out.println("tbd");
        }
        System.out.println(sn);
    }
    public final String getSerialNumberWindows() {

        if (sn != null) {
            return sn;
        }

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[] { "wmic", "bios", "get", "serialnumber" });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner sc = new Scanner(is);
        try {
            while (sc.hasNext()) {
                String next = sc.next();
                if ("SerialNumber".equals(next)) {
                    sn = sc.next().trim();
                    break;
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }

        return sn;
    }
    public  final String getSerialNumberMac() {

        if (sn != null) {
            return sn;
        }

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[] { "/usr/sbin/system_profiler", "SPHardwareDataType" });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        String marker = "Serial Number";
        try {
            while ((line = br.readLine()) != null) {
                if (line.contains(marker)) {
                    sn = line.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }

        return sn;
    }

}
