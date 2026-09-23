public class Utils {

	public void main(String[] args) {
		testStep2();
	}
	
    public static int writeInt(byte[] memory, int offset, int value) {
        memory[offset+3] = (byte) (value & 0xFF); // pas de décalage car 1ère valeur 
		memory[offset+2] = (byte) (value >> 8 & 0xFF); // décalage d'1 octet (8bits)
		memory[offset+1] = (byte) (value >> 16 & 0xFF); // décalage de 2 octet (16 bits)
		memory[offset] = (byte) (value >> 24 & 0xFF); // décalage de 3 octets (24 octets)
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
        memory[offset+1] = (byte) (value & 0xFF);
		memory[offset] = (byte) (value >> 8 & 0xFF);  
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        short b0 = (short) (memory[offset+1] & 0xFF);
		short b1 = (short) ((memory[offset] & 0xFF) << 8);
        return (short) (b0 | b1);
    }
	
	// Tests
	public static void testStep2() {
		System.out.println("=== TEST ÉTAPE 2 : Utils Entiers ===");

		byte[] buffer = new byte[32];

		int value = 0xF0A1B2E3;
		int written = Utils.writeInt(buffer, 3, value);

		assert written == 4 : "writeInt doit retourner 4";

		assert (buffer[3]  & 0xFF) == 0xF0 : "Octet 0 incorrect";
		assert (buffer[4]  & 0xFF) == 0xA1 : "Octet 1 incorrect";
		assert (buffer[5]  & 0xFF) == 0xB2 : "Octet 2 incorrect";
		assert (buffer[6]  & 0xFF) == 0xE3 : "Octet 3 incorrect";

		assert Utils.readInt(buffer, 3) == value :
				"Erreur writeInt / readInt";

		short shortValue = (short) 0xF0A1;
		int shortWritten = Utils.writeShort(buffer, 20, shortValue);

		assert shortWritten == 2 : "writeShort doit retourner 2";

		assert (buffer[20] & 0xFF) == 0xF0 :
				"Premier octet du short incorrect";

		assert (buffer[21] & 0xFF) == 0xA1 :
				"Deuxième octet du short incorrect";

		assert Utils.readShort(buffer, 20) == shortValue :
				"Erreur writeShort / readShort";

		System.out.println("[OK] Étape 2 validée !");
	}
}