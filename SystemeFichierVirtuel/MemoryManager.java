import java.io.*;

public class MemoryManager {

    public static final int BLOCK_SIZE = 512;
    public static final int TOTAL_MEMORY = 1024 * 1024;
    public static final int NUM_BLOCKS =
            TOTAL_MEMORY / BLOCK_SIZE;

    public static final int SUPERBLOCK_OFFSET = 0;
    public static final int BITMAP_OFFSET = BLOCK_SIZE;
    public static final int INODE_TABLE_OFFSET = 2 * BLOCK_SIZE;
    public static final int DATA_OFFSET = 129 * BLOCK_SIZE;

    public static final int INODE_SIZE = 128;

    public static final int INODE_TABLE_SIZE = DATA_OFFSET - INODE_TABLE_OFFSET;

    public static final int MAX_INODES = INODE_TABLE_SIZE / INODE_SIZE;

    private byte[] memory;

	// tableau mémoire
    public MemoryManager() {
        this.memory = new byte[TOTAL_MEMORY];
        initializeFilesystem();
    }

    private void initializeFilesystem() {
        writeSuperblock();
		for(int iterations=0; iterations<16; iterations++) {
			memory[BITMAP_OFFSET+iterations] |= 0xFF;
		}
    }

    private void writeSuperblock() {

        Utils.writeString(
                memory,
                SUPERBLOCK_OFFSET,
                "MYFS1.0",
                16);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 16,
                BLOCK_SIZE);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 20,
                TOTAL_MEMORY);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 24,
                NUM_BLOCKS);

        Utils.writeInt(
                memory,
                SUPERBLOCK_OFFSET + 28,
                MAX_INODES);
    }

    public byte[] getFilesystemMemory() {
        return memory;
    }
	
	// 5
	
	public boolean setBlockUsed(int blockNumber, boolean used) {
		if (blockNumber < 0 ||
			blockNumber >= NUM_BLOCKS) {
			return false;
		}
		int byteIndex = blockNumber / 8; // 129 / 8
		int bitPosition = blockNumber % 8;
		int offset = BITMAP_OFFSET + byteIndex; // pour le bloc 129 : 512 + 16 = 528
		int mask = 1 << bitPosition;

		if (used) {
			memory[offset] = (byte) (memory[offset] | mask);
		} else {
			memory[offset] = (byte) (memory[offset] & ~mask); // inverse le masque puis force le bit à 0
		}
		return true;
	}

	public int isBlockUsed(int blockNumber) {
		if (blockNumber < 0 ||
			blockNumber >= NUM_BLOCKS) {
			return -1;
		}
		int byteIndex = blockNumber / 8; // n° de l'octet dans le bitmap
		int bitPosition = blockNumber % 8; // position du bit dans l'octet
		int offset = BITMAP_OFFSET + byteIndex; 
		int mask = 1 << bitPosition; // création d'un masque avec juste le bit recherché à 1
		int value = memory[offset] & mask; // bit qui correspond au bloc demandé
		
		if (value != 0) {
			return 1;
		}
		return 0;
	}

	public int allocateBlock() {
		// avant bloc 129 : blocs réservés au système, donc :
		for (int blockNumber = 129; blockNumber < NUM_BLOCKS; blockNumber++) {
			if (isBlockUsed(blockNumber) == 0) {
				setBlockUsed(blockNumber, true);
				return blockNumber;
			}
		}
		return -1; // aucun bloc disponible
	}
}