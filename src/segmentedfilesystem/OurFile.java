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
            total = footer[2];
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
        if (datapacket.size() == total) {
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
            int pna = a[2] | a[3];
            int pnb = b[2] | b[3];

            return pna - pnb;
        }
    }
}
