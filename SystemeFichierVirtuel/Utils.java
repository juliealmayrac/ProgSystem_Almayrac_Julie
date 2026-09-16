public class Utils {

    public static int writeInt(byte[] memory, int offset, int value) {
        memory[offset]= (byte) (value & 0xFF); // pas de décalage car 1ère valeur 
		memory[offset]= (byte) (value >> 8 & 0xFF); // décalage d'1 octet (8bits)
		memory[offset]= (byte) (value >> 16 & 0xFF); // décalage de 2 octet (16 bits)
		memory[offset]= (byte) (value >> 24 & 0xFF); // décalage de 3 octets (24 octets)
        return 4;
    }

    public static int readInt(byte[] memory, int offset) {
        // TODO: Reconstituer le int sur 4 octets.
        return 0;
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        // TODO: Écrire les 2 octets de 'value'.
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        // TODO: Lire le short sur 2 octets.
        return 0;
    }
}