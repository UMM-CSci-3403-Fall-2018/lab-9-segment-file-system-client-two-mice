package segmentedfilesystem;

public class DataPacket {
    byte status;
    byte id;
    int packetNumber;
    byte[] data;

    public DataPacket(byte[] packet, int length) {
        this.status = packet[0];
        this.id = packet[1];
        this.data = new byte[length - 4];
        this.packetNumber = (packet[2] << 8 | packet[3] & 0xFF);

        for (int i = 4; i < length; i++) {
            data[i - 4] = packet[i];
        }
    }

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