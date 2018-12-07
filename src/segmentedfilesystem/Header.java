package segmentedfilesystem;

public class Header {
    byte status;
    byte id;
    String name;

    public Header(byte[] packet, int length) {
        this.status = packet[0];
        this.id = packet[1];

        byte[] fileName = java.util.Arrays.copyOfRange(packet, 2, length);

        name = new String(fileName);
    }

    public String getName() {
        return name;
    }

    public byte getId() {
        return id;
    }
}
