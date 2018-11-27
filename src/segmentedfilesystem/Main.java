package segmentedfilesystem;

import java.io.IOException;
import java.net.*;

public class Main {
    
    public static void main(String[] args) {
        int port = 6014;
        InetAddress address = null;
        DatagramSocket socket = null;
        DatagramPacket packet;
        // byte[] sendBuf = new byte[1024];

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

        // Request to server
        byte[] buf = new byte[1024];

        try {
            address = InetAddress.getByName(args[0]);
        } catch (UnknownHostException uhe) {
            System.err.println("An Unknown Host Exception was caught: " + uhe.getMessage());
        }

        packet = new DatagramPacket(buf, buf.length, address, port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Receive from server
        packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the Moment: " + received);
        */

        byte[] received = packet.getData();
        // System.out.println(received.length);
        /*
        for (int i = 0; i < received.length; i++) {
            System.out.println(received[i]);
        }
        */
    }

}
