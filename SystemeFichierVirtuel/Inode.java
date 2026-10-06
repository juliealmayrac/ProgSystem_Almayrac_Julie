public class Inode {

    private MemoryManager memoryManager;
    private int inodeNumber;

    public static final int INODE_SIZE = 128;
    public static final int DIRECT_POINTERS = 10;

    public Inode(MemoryManager memoryManager, int inodeNumber) {
        this.memoryManager = memoryManager;
        this.inodeNumber = inodeNumber;
    }

    public int getInodeOffset() {
        return MemoryManager.INODE_TABLE_OFFSET + (inodeNumber*INODE_SIZE);
    }

    public int getFileType() {
        byte[] memory = memoryManager.getFilesystemMemory();
		int offset = getInodeOffset();
        return Utils.readInt(memory, offset + 4); // int = 4 octets
    }

    public int getFileSize() {
		byte[] memory = memoryManager.getFilesystemMemory();
        return Utils.readInt(memory, getInodeOffset() + 8);
    }

    public int[] getDirectPointers() {

        byte[] memory = memoryManager.getFilesystemMemory();

        int[] pointers = new int[DIRECT_POINTERS];

        for (int indice=0; indice<DIRECT_POINTERS; indice++) {
			pointers[indice] = Utils.readInt(memory, getInodeOffset() + 28 + (indice*4)); // écriture dans un int (4 octets = un int)
			// offset 28 = début des pointeurs, int = 4 octets
		}
        return pointers;
    }
	
	public void writeToMemory(
        int fileType,
        int fileSize,
        long creationTime,
        long modificationTime,
        int[] directPointers,
        int indirectPointer,
        short permissions,
        int linkCount) {

		byte[] memory = memoryManager.getFilesystemMemory();

		int offset = getInodeOffset();

		// 1. Numéro d'inode
		Utils.writeInt(memory, offset, inodeNumber);
		offset += 4;
		
		// 2. Type
		Utils.writeInt(memory, offset, fileType);
		offset += 4;
		
		// 3. Taille
		Utils.writeInt(memory, offset, fileSize);
		offset += 4;
		
		// 4. Création
		Utils.writeLong(memory, offset, creationTime);
		offset += 8;
		
		// 5. Modification
		Utils.writeLong(memory, offset, modificationTime);
		offset += 8;
		
		// 6. 10 pointeurs directs
		for (int indice=0; indice<DIRECT_POINTERS; indice++) {
			Utils.writeInt(memory, offset, directPointers[indice]); // écriture 
			offset += 4;
		}
		
		// 7. Pointeur indirect
		Utils.writeInt(memory, offset, indirectPointer);
		offset += 4;
		
		// 8. Permissions
		Utils.writeShort(memory, offset, permissions);
		offset += 2; // short = 2 octets
		
		// 9. Nombre de liens
		Utils.writeInt(memory, offset, linkCount);
		offset += 4; 
	}
}