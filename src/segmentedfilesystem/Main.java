package segmentedfilesystem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;

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

        // Sets placeholder name to be used for OurFiles
        StringBuilder str = new StringBuilder("null");

        OurFile file1 = new OurFile(str);
        OurFile file2 = new OurFile(str);
        OurFile file3 = new OurFile(str);

        ArrayList<byte[]> tempData = new ArrayList<byte[]>();
        ArrayList<byte[]> tempFooter = new ArrayList<byte[]>();

        // OurFile "builder" will run until all three files are received
        while((!file1.isComplete() && !file2.isComplete() && !file3.isComplete())) {

            // Receive from server
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] received1 = packet.getData();
            byte[] received = Arrays.copyOf(received1, packet.getLength());

            // Footer check
            if (received[0] % 4 == 3) {
                if (received[1] == file1.getID() && file1.footer == null) {
                    file1.addFooter(received);
                    System.out.println("foot 1");
                    // System.out.println(file1.total);
                } else if (received[1] == file2.getID() && file2.footer == null) {
                    file2.addFooter(received);
                    System.out.println("foot 2");
                    // System.out.println(file2.total);
                } else if (received[1] == file3.getID() && file3.footer == null){
                    file3.addFooter(received);
                    System.out.println("foot 3");
                    // System.out.println(file3.total);
                } else if (!tempFooter.contains(received)){
                    tempFooter.add(received);
                }
            }

            // Header check
            else if (received[0] % 2 == 0) {

                //StringBuilder name = new StringBuilder();
                int length = packet.getLength();
                byte[] names = java.util.Arrays.copyOfRange(received,2, length);
                //String name = new String(names);
                String name = new String(names);
               /* // get name of file
                for (int i = 2; i < received.length; i++) {
                    String temp = Byte.toString(received[i]);
                    // name.append(Byte.toString(received[i]));
                    name.append(temp);
                }*/

                byte id = received[1];

                if (file1.name.equals("null")) {
                    file1 = new OurFile(name, id);
                    file1.addHeader(received);
                    System.out.println("found 1");
                    // System.out.println(file1.getID());
                } else if (file2.name.equals("null")) {
                    file2 = new OurFile(name, id);
                    file2.addHeader(received);
                    System.out.println("found 2");
                    // System.out.println(file2.getID());
                } else if (file3.name.equals("null")){
                    file3 = new OurFile(name, id);
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
                    // System.out.println("add 1");
                } else if (received[1] == file2.getID()) {
                    file2.addData(received);
                    // System.out.println("add 2");
                } else if (received[1] == file3.getID()) {
                    file3.addData(received);
                    // System.out.println("add 3");
                } else if (!tempData.contains(received)) {
                    tempData.add(received);
                }
            } else {
                throw new IllegalStateException("received packet is invalid");
            }

            // Add tempFooter if possible
            for (int i = 0; i < tempFooter.size(); i++) {
                if (tempFooter.get(i)[1] == file1.getID() && file1.footer == null) {
                    file1.addFooter(tempFooter.get(i));
                    tempFooter.remove(i);
                    System.out.println("add tempfoot 1");
                    // System.out.println(file1.total);
                } else if (tempFooter.get(i)[1] == file2.getID() && file2.footer == null) {
                    file2.addFooter(tempFooter.get(i));
                    tempFooter.remove(i);
                    System.out.println("add tempfoot 2");
                    // System.out.println(file2.total);
                } else if (tempFooter.get(i)[1] == file3.getID() && file3.footer == null) {
                    file3.addFooter(tempFooter.get(i));
                    tempFooter.remove(i);
                    System.out.println("add tempfoot 3");
                    // System.out.println(file3.total);
                } else {
                    // System.out.println("tempFooter error: " + tempFooter.size());
                }
            }

            // Add tempData if possible
            for (int i = 0; i < tempData.size(); i++) {
                if (tempData.get(i)[1] == file1.getID()) {
                    file1.addData(tempData.get(i));
                    tempData.remove(i);
                    // System.out.println("add tempdata 1");
                } else if (tempData.get(i)[1] == file2.getID()) {
                    file2.addData(tempData.get(i));
                    tempData.remove(i);
                    // System.out.println("add tempdata 2");
                } else if (tempData.get(i)[1] == file3.getID()) {
                    file3.addData(tempData.get(i));
                    tempData.remove(i);
                    // System.out.println("add tempdata 3");
                } else {
                    // System.out.println("tempData error - invalid ID or header is not found yet");
                }
            }

            // System.out.println("Here again");
        }

        System.out.println("Done");

        file1.sortData();
        file2.sortData();
        file3.sortData();

        try {
            FileOutputStream fos = new FileOutputStream(file1.name);
            for (int i = 0; i < file1.datapacket.size(); i++) {
                fos.write(file1.datapacket.get(i));
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file2.name);
            for (int i = 0; i < file2.datapacket.size(); i++) {
                fos.write(file2.datapacket.get(i));
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(file3.name);
            for (int i = 0; i < file3.datapacket.size(); i++) {
                fos.write(file3.datapacket.get(i));
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        System.out.println(file1.name);
        System.out.println(file2.name);
        System.out.println(file3.name);
        */
    }

}
