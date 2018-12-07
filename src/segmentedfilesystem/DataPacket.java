package segmentedfilesystem;

public class DataPacket {
    byte status;
    byte id;
    int packetNumber;
    byte[] data;

    // DataPacket constructor
    // Gives packet number, status, id, data
    public DataPacket(byte[] packet, int length) {
        this.status = packet[0];
        this.id = packet[1];
        this.data = new byte[length - 4];
        // Offsets first byte of packet number by one byte then bitwise OR with second byte of packet number * 256 represented by 0xFF (hex)
        this.packetNumber = (packet[2] << 8 | packet[3] & 0xFF);

        // Data
        for (int i = 4; i < length; i++) {
            data[i - 4] = packet[i];
        }
    }

    // Getters
    public byte getId() {
        return id;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public byte[] getData() {
        return data;
    }
}