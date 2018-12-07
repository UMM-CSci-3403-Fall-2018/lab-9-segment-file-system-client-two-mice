package segmentedfilesystem;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

public class File {
    String name;

    ArrayList<DataPacket> packets = new ArrayList<DataPacket>();

    int total = -100;
    int counter = 0;
    byte id;

    public void addData(DataPacket packet) {
        // Packet is a footer
        if(packet.status % 4 == 3) {
            this.total = packet.getPacketNumber() + 1;
        }

        packets.add(packet);
        counter++;

        // Counter visual
        if (total == -100) {
            System.out.println(name + " " + counter + "/ ?");
        } else {
            System.out.println(name + " " + counter + "/" + total);
        }
    }

    public File(String name, byte id) {
        this.name = name;
        this.id = id;
    }

    public File() {
        this.name = "empty";
        this.id = 00000000;
    }

    public boolean isComplete() {
        if (counter == total) {
            return true;
        } else {
            return false;
        }
    }

    public int getId() {
        return this.id;
    }

    public void sortPackets(){
        Collections.sort(packets, new SortPackets());
    }
}

// Used for sortPackets
class SortPackets implements Comparator<DataPacket> {
    public int compare(DataPacket a, DataPacket b) {
        int pna = a.getPacketNumber();
        int pnb = b.getPacketNumber();
        return pna - pnb;
    }
}


