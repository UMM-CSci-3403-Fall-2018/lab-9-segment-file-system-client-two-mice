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

        File file1 = new File();
        File file2 = new File();
        File file3 = new File();

        // Receive from server
        packet = new DatagramPacket(buf, buf.length);

        ArrayList<DataPacket> temp = new ArrayList<DataPacket>();

        while((!file3.isComplete())) {

            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] received = packet.getData();
            int length = packet.getLength();
            //byte[] received = Arrays.copyOf(received1, range);

            //Header Check
            if (received[0] % 2 == 0) {
                Header header = new Header(received, length);

                String name = header.getName();
                byte id = header.getId();

                //create files with headers
                if (file1.name.equals("empty")) {
                    file1 = new File(name, id);
                    System.out.println("found 1");
                } else if (file2.name.equals("empty")) {
                    file2 = new File(name, id);
                    System.out.println("found 2");
                } else {
                    file3 = new File(name, id);
                    System.out.println("found 3");
                }

            //Data Packet
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

            //empty temp array
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

        System.out.println(file1.name);
        System.out.println(file2.name);
        System.out.println(file3.name);

    }

}
