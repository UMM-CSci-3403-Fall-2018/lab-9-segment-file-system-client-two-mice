package segmentedfilesystem;

import java.util.ArrayList;

public class File {
    String name;
    byte[] header;
    ArrayList<byte[]> datapacket = new ArrayList<byte[]>();

    public void dataAdd(byte[] packet) {
        this.datapacket.add(packet);
    }
}
