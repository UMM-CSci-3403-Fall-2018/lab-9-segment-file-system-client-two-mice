package segmentedfilesystem;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

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



        /*
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Quote of the Moment: " + received);
        */
        StringBuilder str = new StringBuilder("null");

        File file1 = new File(str, Byte.parseByte("0"));
        File file2 = new File(str, Byte.parseByte("0"));
        File file3 = new File(str, Byte.parseByte("0"));

        ArrayList<byte[]> headers = new ArrayList<>();

        // while loop goes here
        while(!file1.isComplete() && !file2.isComplete() && !file3.isComplete()) {

            // Receive from server
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] received = packet.getData();

            // System.out.println(received[0]);

            // End check
            if (received[0] % 4 == 3) {
                if (received[1] == file1.getID()) {
                    file1.addFooter(received);
                } else if (received[1] == file2.getID()) {
                    file2.addFooter(received);
                } else {
                    file3.addFooter(received);
                }
            }

            // Header check and else data check
            else if (received[0] % 2 == 0) {
                if (!headers.contains(received)) {
                    headers.add(received);

                    StringBuilder name = new StringBuilder();

                    // get name of file
                    for (int i = 2; i < received.length; i++) {
                        name.append(Byte.toString(received[i]));
                    }

                    byte id = received[1];

                    if (file1.name.equals("null")) {
                        file1 = new File(name, id);
                        file1.addHeader(received);
                        System.out.println(name + "Print found 1");
                    } else if (file2.name.equals("null")) {
                        file2 = new File(name, id);
                        file2.addHeader(received);
                        System.out.println("Print found 2");
                    } else {
                        file3 = new File(name, id);
                        file3.addHeader(received);
                        System.out.println("Print found 3");
                    }
                }
            } else {
                if (received[1] == file1.getID()) {
                    file1.addData(received);
                } else if (received[1] == file2.getID()) {
                    file2.addData(received);
                } else {
                    file3.addData(received);
                }
            }
        }

        System.out.println(file1.name);
        System.out.println(file2.name);
        System.out.println(file3.name);

    }

}
