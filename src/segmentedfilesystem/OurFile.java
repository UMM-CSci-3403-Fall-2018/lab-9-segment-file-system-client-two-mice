package segmentedfilesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OurFile {
    String name;
    byte[] header;
    byte[] footer;
    ArrayList<byte[]> datapacket = new ArrayList<byte[]>();
    int total = 0;
    byte id;

    public void addHeader(byte[] header) {
        this.header = header;
    }

    public void addFooter(byte[] footer) {
        byte f1 = footer[2];
        byte f2 = footer[3];

        while (f1 < 0) {
            f1 += 256;
        }

        while (f2 < 0) {
            f2 += 256;
        }

        total = 256 * f1 + f2;
        System.out.println(footer[0] + " " + footer[1] + " " + f1 + " " + f2 + " " + total);
        this.footer = footer;
    }
    public void addData(byte[] packet) {
        if(!datapacket.contains(packet)){
            this.datapacket.add(packet);
        }
    }

    public OurFile(String name, byte id) {
        //this.name = name.toString();
        this.name = name;
        this.id = id;
    }

    public OurFile(StringBuilder name) {
        this.name = name.toString();
    }

    public boolean isComplete() {
        if (total == 0) {
            return false;
        }
        if (datapacket.size() == total + 1) {
            return true;
        } else {
            return false;
        }
    }

    public int getID() {
        return this.id;
    }

    public void sortData() {
        Collections.sort(datapacket, new sortPackets());
    }

    class sortPackets implements Comparator<byte[]> {
        public int compare(byte[] a, byte[] b) {
            byte pna1 = a[2];
            byte pna2 = a[3];
            byte pnb1 = b[2];
            byte pnb2 = b[3];

            System.out.println(pna1);
            System.out.println(pna2);
            System.out.println(pnb1);
            System.out.println(pnb2);

            if (pna1 < 0) {
                pna1 += 256;
            }

            if (pna2 < 0) {
                pna2 += 256;
            }

            if (pnb1 < 0) {
                pnb1 += 256;
            }

            if (pnb2 < 0) {
                pnb2 += 256;
            }
            int pna = 256 * pna1 + pna2;
            int pnb = 256 * pnb1 + pnb2;

            return pna - pnb;
        }
    }
}
