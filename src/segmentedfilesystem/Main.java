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
        byte[] buf = new byte[1028];

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

        File file1 = new File(str);
        File file2 = new File(str);
        File file3 = new File(str);

        while((!file1.isComplete() && !file2.isComplete() && !file3.isComplete())) {

            // Receive from server
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] received = packet.getData();

            // End check
            if (received[0] % 4 == 3) {
                if (received[1] == file1.getID()) {
                    file1.addFooter(received);
                    System.out.println("foot 1");
                } else if (received[1] == file2.getID()) {
                    file2.addFooter(received);
                    System.out.println("foot 2");
                } else if (received[1] == file3.getID()){
                    file3.addFooter(received);
                    System.out.println("foot 3");
                }
            }

            // Header check
            else if (received[0] % 2 == 0) {

                    StringBuilder name = new StringBuilder();

                    // get name of file
                    for (int i = 2; i < received.length; i++) {
                            name.append(Byte.toString(received[i]));
                    }

                    byte id = received[1];

                    if (file1.name.equals("null")) {
                        file1 = new File(name, id);
                        file1.addHeader(received);
                        System.out.println("found 1");
                        System.out.println(file1.getID());
                    } else if (file2.name.equals("null")) {
                        file2 = new File(name, id);
                        file2.addHeader(received);
                        System.out.println("found 2");
                        System.out.println(file2.getID());
                    } else if (file3.name.equals("null")){
                        file3 = new File(name, id);
                        file3.addHeader(received);
                        System.out.println("found 3");
                    } else {
                        throw new IllegalStateException("We should never see more than 3 files.");
                    }
            }

            // Data check
            else if (received[0] % 2 == 1){
                // System.out.println("Found data packet");
                if (received[1] == file1.getID()) {
                    file1.addData(received);
                    System.out.println("add 1");
                } else if (received[1] == file2.getID()) {
                    file2.addData(received);
                    System.out.println("add 2");
                } else if (received[1] == file3.getID()){
                    file3.addData(received);
                    System.out.println("add 3");
                }
            } else {
                throw new IllegalStateException("received packet is invalid");
            }
        }

        System.out.println(file1.name);
        System.out.println(file2.name);
        System.out.println(file3.name);

    }

}
