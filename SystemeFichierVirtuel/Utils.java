public class Utils {
	
    public static int writeInt(byte[] memory, int offset, int value) {
        memory[offset+3] = (byte) (value & 0xFF); 
		memory[offset+2] = (byte) (value >> 8 & 0xFF); 
		memory[offset+1] = (byte) (value >> 16 & 0xFF); 
		memory[offset] = (byte) (value >> 24 & 0xFF);
        return 4;
    }

    public static int readInt(byte[] memory, int offset) {
        int b0 = (int) (memory[offset+3] & 0xFF);
		int b1 = (int) (memory[offset+2] & 0xFF) << 8;
		int b2 = (int) (memory[offset+1] & 0xFF) << 16;
		int b3 = (int) (memory[offset] & 0xFF) << 24;
        return b0 | b1 | b2 | b3;
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        memory[offset+1] = (byte) (value >> 0* 8 & 0xFF);
		memory[offset + 0] = (byte) (value >> 8 & 0xFF);  
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        short b0 = (short) (memory[offset+1] & 0xFF);
		short b1 = (short) ((memory[offset] & 0xFF) << 8);
        return (short) (b0 | b1);
    }
	
	public static int writeLong(byte[] memory, int offset, long value) {
		for (indice=0; indice<=7; indice++) {
			memory[offset+(7-indice)] = (byte) (value >> (indice*8) & 0xFF);
		}
		return 8;
	}

	public static long readLong(byte[] memory, int offset) {
		long valeurLong = 0;
		for (indice=0; indice<=7; indice++) {
			valeurLong += (long) (memory[offset+(7-indice)] & 0xFF) << (indice*8);
		}
		return valeurLong;
	}

	public static int writeString(byte[] memory, int offset, String str, int maxLength) {
		int taille = Math.min(str.length;maxLength);
		byte[] octets = str.getBytes();
		for (indice=0; indice <= limite; indice++) {
			memory[offset+(taille-indice)] = (byte) (octets[indice]) & 0xFF;
		}
		
		// TODO deuxième boucle si taille < maxLength
		
		return maxLength;
	}

	public static String readString(
			byte[] memory,
			int offset,
			int maxLength) {

		// TODO:
		// Lire jusqu'au premier octet nul
		// ou jusqu'à maxLength.

		return "";
	}
}