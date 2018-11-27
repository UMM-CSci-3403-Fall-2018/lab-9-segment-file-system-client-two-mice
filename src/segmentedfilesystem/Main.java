package segmentedfilesystem;

import java.io.IOException;
import java.net.*;

public class Main {
    
    public static void main(String[] args) {
        int port;
        InetAddress address;
        DatagramSocket socket = null;
        DatagramPacket packet;
        byte[] sendBuf = new byte[1024];

        // Test validity of request
        if (args.length != 1) {
            System.out.println("Usage: java QuoteClient <heartofgold.morris.umn.edu>");
            return;
        }

        try {
            socket = new DatagramSocket();
        } catch (SocketException se) {
            System.err.println("A SocketException was caught: " + se.getMessage());
        }

        byte[] buf = new byte[1024];

        try {
            address = InetAddress.getByName(args[0]);
        } catch (UnknownHostException uhe) {
            System.err.println("An Unknown Host Exception was caught: " + uhe.getMessage());
        }


        packet = new DatagramPacket(buf, buf.length, address, 6014);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
