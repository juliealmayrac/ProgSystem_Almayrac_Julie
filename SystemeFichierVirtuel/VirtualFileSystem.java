import java.util.*;

public class VirtualFileSystem {

    private MemoryManager memoryManager;

    public VirtualFileSystem() {
        this.memoryManager = new MemoryManager();
    }

    private int allocateInode() {
        byte[] memory = memoryManager.getFilesystemMemory();

        for (int inodeNum=0; inodeNum < MemoryManager.MAX_INODES; inodeNum++) {
			int inodeOffset = MemoryManager.INODE_TABLE_OFFSET + (inodeNum*Inode.INODE_SIZE);
			int fileType = Utils.readInt(memory, inodeOffset+4); // lecture du type de l'inode
			if (fileType==0) {
				return inodeNum;
			}
		}
        return -1;
    }

    public boolean createFile(String directory, String filename) {
        int inodeNum = allocateInode();

        if (inodeNum == -1) {
            return false;
        }
        Inode inode = new Inode(memoryManager, inodeNum);
		int [] directPointers = new int[Inode.DIRECT_POINTERS]; //  création des 10 pointeurs
		long date = System.currentTimeMillis();
		inode.writeToMemory(1, // type fichier
							0, // taille
							date, // création
							date, // modification 
							directPointers, // aucun bloc
							0, // pointeur indirect
							(short) 0, // permissions initiales 
							1); // liens
        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }
	
	public boolean writeFile(
        int inodeNum,
        byte[] data) {

		int blocksNeeded = (data.length + MemoryManager.BLOCK_SIZE - 1)/ MemoryManager.BLOCK_SIZE;

		if (blocksNeeded > Inode.DIRECT_POINTERS) {
			return false;
		}

		int[] blockPointers =
				new int[Inode.DIRECT_POINTERS];

		// Allouer blocksNeeded blocs.
		for (int indice=0; indice < blocksNeeded; indice++) {
			int blockNumber = memoryManager.allocateBlock();
			if (blockNumber == -1) {
				return false;
			}
			blockPointers[indice] = blockNumber;
		}

		byte[] memory = memoryManager.getFilesystemMemory();

		int bytesRemaining = data.length;

		int dataSrcOffset = 0;
		
		for (int indice=0; indice < blocksNeeded; indice++) {
			int blockNumber = blockPointers[indice];
			int blockOffset = blockNumber*MemoryManager.BLOCK_SIZE;
			int bytesACopier = Math.min(bytesRemaining, MemoryManager.BLOCK_SIZE);
			System.arraycopy(data, dataSrcOffset, memory, blockOffset, bytesACopier);
			dataSrcOffset += bytesACopier;
			bytesRemaining -= bytesACopier;
		}

		Inode inode = new Inode(memoryManager, inodeNum);
		long date = System.currentTimeMillis();
		inode.writeToMemory(1,
							data.length,
							date,
							date,
							blockPointers, 
							0,
							(short) 0,
							1);

		return true;
	}
	
	public byte[] readFile(int inodeNum) {
		Inode inode = new Inode(memoryManager, inodeNum);

		int fileSize = inode.getFileSize();

		if (fileSize == 0) {
			return new byte[0];
		}

		byte[] fileData = new byte[fileSize];

		byte[] memory = memoryManager.getFilesystemMemory();

		int[] blockPointers = inode.getDirectPointers();

		int bytesRemaining = fileSize;
		int dataDestOffset = 0;
		for (int indice=0; indice<blockPointers.length; indice++) {
			if (bytesRemaining<=0) {
				break;
			}
			int blockNumber = blockPointers[indice];
			int bytesACopier = Math.min(bytesRemaining, MemoryManager.BLOCK_SIZE);
			int blockOffset = blockNumber * MemoryManager.BLOCK_SIZE;
			System.arraycopy(memory, blockOffset, fileData, dataDestOffset, bytesACopier);
			dataDestOffset += bytesACopier;
			bytesRemaining -= bytesACopier;
		}

		return fileData;
	}
}
