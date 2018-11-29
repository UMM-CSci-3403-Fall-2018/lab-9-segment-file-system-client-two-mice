package segmentedfilesystem;

import java.util.ArrayList;

public class File {
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

    public File(StringBuilder name, byte id) {
        this.name = name.toString();
        this.id = id;
    }

    public File(StringBuilder name) {
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
}
