package segmentedfilesystem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) {
        int port = 6014;
        InetAddress address = null;
        DatagramSocket socket = null;
        DatagramPacket packet;

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

        // Specify files for number of files
        File file1 = new File();
        File file2 = new File();
        File file3 = new File();

        // Receive from server
        packet = new DatagramPacket(buf, buf.length);

        ArrayList<DataPacket> temp = new ArrayList<DataPacket>();

        // Checkers and file "builder" - recieve packets
        while(!(file1.isComplete() && file2.isComplete() && file3.isComplete())) {

            // Recieve new packets
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Store packet in byte array for later and get length for completeness later
            byte[] received = packet.getData();
            int length = packet.getLength();

            // Header Check
            if (received[0] % 2 == 0) {
                Header header = new Header(received, length);

                String name = header.getName();
                byte id = header.getId();

                // Create files with headers
                if (file1.name.equals("empty")) {
                    file1 = new File(name, id);
                } else if (file2.name.equals("empty")) {
                    file2 = new File(name, id);
                } else {
                    file3 = new File(name, id);
                }

            // Data Packet
            } else {
                DataPacket data = new DataPacket(received, length);

                if (data.getId() == file1.getId()) {
                    file1.addData(data);
                } else if (data.getId() == file2.getId()) {
                    file2.addData(data);
                } else if (data.getId() == file3.getId()) {
                    file3.addData(data);
                } else {
                    temp.add(data);
                }
            }

            // Empty temp array
            for (int i = 0; i < temp.size(); i ++) {
                byte id = temp.get(i).getId();
                if (id == file1.getId()) {
                    file1.addData(temp.remove(i));
                } else if (id == file2.getId()) {
                    file2.addData(temp.remove(i));
                } else if (id == file3.getId()) {
                    file3.addData(temp.remove(i));
                }
            }
        }

        socket.close();

        // Sort by packet number
        file1.sortPackets();
        file2.sortPackets();
        file3.sortPackets();

        // Info
        System.out.println();
        System.out.println(file1.name);
        System.out.println(file2.name);
        System.out.println(file3.name);
        System.out.println("DONE!");

        // Build file 1 for read
        try {
            FileOutputStream fos = new FileOutputStream(file1.name);
            for (int i = 0; i < file1.packets.size(); i++) {
                fos.write(file1.packets.get(i).getData());
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Build file 2 for read
        try {
            FileOutputStream fos = new FileOutputStream(file2.name);
            for (int i = 0; i < file2.packets.size(); i++) {
                fos.write(file2.packets.get(i).getData());
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Build file 3 for read
        try {
            FileOutputStream fos = new FileOutputStream(file3.name);
            for (int i = 0; i < file3.packets.size(); i++) {
                fos.write(file3.packets.get(i).getData());
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
