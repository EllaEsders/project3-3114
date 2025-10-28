import java.nio.*;
import java.nio.channels.FileChannel;
import java.io.*;

// The Radix Sort implementation
// -------------------------------------------------------------------------
/**
 *
 * @author ellae and Madelync05
 * @version 1
 */
public class Radix {
    private byte[] memoryPool = new byte[900000];
    /**
     * creates a byte buffer object to act as the memory pool?
     */
    private ByteBuffer buffer = ByteBuffer.wrap(memoryPool);
    /**
     * the file to be sorted
     */
    private RandomAccessFile file;
    /**
     * the print writer to write in the stats file
     * (maybe change name to stats or something)
     */
    private PrintWriter writer;
    /**
     * maybe allowed maybe not ints
     */
// private int sizeOfBytes=0;
// private int sizeOfBlocks=0;
    private int numberOfBlocks = 0;
    private int diskReads = 0;
    private int diskWrites = 0;
    
    private static final int SIZE_OF_BLOCKS = 4096;
    private static final int SIZE_OF_RECORD = 8;
    private static final int NUM_OF_RECS_PER_BLOCK = SIZE_OF_BLOCKS/SIZE_OF_RECORD;

// private int timeTook=0; //no clue how to implement this
    /**
     * Create a new Radix object.
     * 
     * @param theFile
     *            The RandomAccessFile to be sorted
     * @param s
     *            The stats PrintWriter
     *
     * @throws IOException
     */
    public Radix(RandomAccessFile theFile, PrintWriter s) throws IOException {
        file = theFile;
        writer = s;
        radixSort();
    }


    private void radixSort() throws IOException {
        long fileSize = file.length();
        int totalInts = (int)(fileSize / 4); //long
        int numPairs = totalInts / 2;

        int[] keys = new int[numPairs];
        int[] values = new int[numPairs];
        diskReads++;
        file.seek(0);
        for (int i = 0; i < numPairs; i++) {
            keys[i] = file.readInt();
            values[i] = file.readInt();
        }
        radixSortKeys(keys, values);
        file.seek(0);
        diskWrites++;
        for (int i = 0; i < numPairs; i++) {
            file.writeInt(keys[i]);
            file.writeInt(values[i]);
        }

        writer.println("Memory Blocks: " + numPairs);
        writer.println("Disk reads: " + diskReads);
        writer.println("Disk writes: " + diskWrites);
        writer.flush();
    }


    private void radixSortKeys(int[] keys, int[] values) {
        int[] outputKeys = new int[keys.length];
        int[] outputValues = new int[keys.length];
        int max = getMax(keys);
        for (int x = 1; max / x > 0; x *= 10) {
            countSort(keys, values, outputKeys, outputValues, x);
        }
    }


    private void countSort(
        int[] keys,
        int[] values,
        int[] outputKeys,
        int[] outputValues,
        int x) {
        int n = keys.length;
        int[] count = new int[10];

        for (int i = 0; i < n; i++)
            count[(keys[i] / x) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {
            int j = (keys[i] / x) % 10;
            outputKeys[count[j] - 1] = keys[i];
            outputValues[count[j] - 1] = values[i];
            count[j]--;
        }

        System.arraycopy(outputKeys, 0, keys, 0, n);
        System.arraycopy(outputValues, 0, values, 0, n);
    }


    private int getMax(int[] arr) {
        int max = arr[0];
        for (int val : arr) {
            if (val > max) {
                max = val;
            }
        }
        return max;
    }
    
    /**
     * Helper method to read one block into the memoryPool
     */
    private int readBlock(RandomAccessFile f, long position) throws IOException
    {
        f.seek(position);
        int bytesRead = f.read(memoryPool, 0, SIZE_OF_BLOCKS);
        if (bytesRead > 0)
        {
            diskReads++;
        }
        return bytesRead;
    }
    
    /**
     * Helper method that writes one block into file from memoryPool
     */
    private void writeBlock(RandomAccessFile f, long position) throws IOException
    {
        f.seek(position);
        f.write(memoryPool, 0, SIZE_OF_BLOCKS);
        diskWrites++;
    }
    
    /**
     * Get the key at the index of the record in the memoryPool
     */
    private int getKey(int recordIndex)
    {
        return buffer.getInt(recordIndex * SIZE_OF_RECORD);
    }
    
    /**
     * Gets the value at the index of the record in the memoryPool
     */
    private int getValue(int recordIndex)
    {
        return buffer.getInt(recordIndex * SIZE_OF_RECORD + 4);
    }
    
    /**
     * Set a record of the key and value pair at the index in the memoryPool
     */
    private void setRecord(int recordIndex, int key, int value)
    {
        buffer.putInt(recordIndex * SIZE_OF_RECORD, key);
        buffer.putInt(recordIndex * SIZE_OF_RECORD + 4, value);
    }
}
