package segmentedfilesystem;

public class Header {
    byte id;
    String name;

    // Header constructor
    // Gives name, id
    public Header(byte[] packet, int length) {
        this.id = packet[1];

        byte[] fileName = java.util.Arrays.copyOfRange(packet, 2, length);

        name = new String(fileName);
    }

    // Getters
    public String getName() {
        return name;
    }

    public byte getId() {
        return id;
    }
}
