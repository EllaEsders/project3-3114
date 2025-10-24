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
    /**
     * creates a byte buffer object to act as the memory pool?
     */

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
    private int numberOfBlocks = 0;
// private int sizeOfBlocks=0;
    private int diskReads = 0;
    private int diskWrites = 0;

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


    /**
     * Do a Radix sort
     *
     * @throws IOException
     */
    private void radixSort() throws IOException {
        /*
         * 1. figure out how to read in file into a buffer?
         * 2. sort by each digit
         * for each digit
         * 1. have a count array from 0-9
         * 2. go through each number adding to the array pos that last digit
         * ends with
         * 3. take the total and subtract by count[i] and place at count at i
         * 4. put records into bins from left to right
         * find last digit of number
         * find count[digit]
         * place in the output[count[digit]]
         * add one to count[digit]
         * 3. return stats file some how
         * may need
         * probably not tho
         * 1. count in for amount and size of memory pool
         * 2. count int for read
         * 3. count for write
         * 4. somehow determine time took?
         * this may be done in radix proj
         */

        /*
         * need to redo this without arrays (maybe not for first milestone
         * though)
         * somewhere it is printing the numbers in the stats file
         */
        // byte[] memoryPool = new byte[4096];
        /*
         * makes the byte buffer and int buffer objects
         */
// ByteBuffer byteBuffer = ByteBuffer.allocate(900000);
// IntBuffer intBuffer = byteBuffer.asIntBuffer();
        String fileName = "input.bin";

// try (DataInputStream dis = new DataInputStream(new BufferedInputStream(
// new FileInputStream(fileName)))) {
        int fileSize = (int)file.length();
        int numInts = fileSize / 8;
        int[] values = new int[numInts];
        int[] other = new int[numInts * 2];
        int j = 0;
        for (int i = 0; i < numInts; i++) {
            values[i] = file.readInt();
            other[j] = values[i];
            j++;
            other[j] = file.readInt();
            j++;
        }
        for (int v : values) {
            numberOfBlocks++;
            System.out.println(v + " print 1");
        }
        diskReads++;
        writer.println("Memory Blocks: " + numInts + " of size " + fileSize);
        /*
         * right now it is not handling the keys and
         * the input numbers correctly
         * so it is inputed as #, 1 #, 2 and its counting
         * 1 and 2 as numbers
         * when they are not (which we might not need to change)
         * also it doesnt use the files reading and writing
         * so we arent counting the file reads and writes
         * also it is not the most effiecent, its
         * going through every diget
         * its also not prepared to handle large data
         */
        writer.println("Disk reads: " + diskReads);
        writer.println("Disk writes: " + diskWrites);
        values = sortDigit(values, numberOfBlocks, 1);
        file = new RandomAccessFile("input.bin", "rw");
        int key = 0;
        for (int v : values) {
            key = find(v, other);
            file.writeInt(v);
            file.writeInt(key);
            writer.println("Key: " + v + " Value: " + key);

            System.out.println(key + " key was value is " + v + " print 2");
        }
        file = new RandomAccessFile("input.bin", "r");
        while (file.getFilePointer() < file.length()) {
            int k = file.readInt();
            int value = file.readInt();
            System.out.println("Key: " + k + ", Value: " + value);
        }
        file.close();
        writer.flush();
    }


    /**
     * finds where a value is in the array
     * 
     * @param val
     *            the value to be found
     * @param arr
     *            the array to be searched
     * @return the int representing the key of the val
     */
    private int find(int val, int[] arr) {
        int key = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                System.out.println(arr[i] + "here" + arr[i + 1]);
                return arr[i + 1];
            }
        }
        return key;
    }


    /**
     * sorts array
     * 
     * @param a
     *            the array to be sorted
     * @param number
     *            the number of ints in the array
     * @param val
     *            the diget to start on (1)
     * @return an array of sorted digets
     */
    private int[] sortDigit(int[] a, int number, int val) {
        diskWrites++;
        int[] output = new int[number];
        int[] count = new int[10];
        int i;
        int j;
        int rtok;
        for (i = 0, rtok = 1; i < 10; i++, rtok *= 10) {
            for (j = 0; j < 10; j++) {
                count[j] = 0;
            }
            for (j = 0; j < a.length; j++) {
                count[(a[j] / rtok) % 10]++;
            }
            int total = number;
            for (j = 9; j >= 0; j--) {
                total -= count[j];
                count[j] = total;
            }
            for (j = 0; j < a.length; j++) {
                output[count[(a[j] / rtok) % 10]] = a[j];
                count[(a[j] / rtok) % 10] = count[(a[j] / rtok) % 10] + 1;
            }
            for (j = 0; j < a.length; j++) {
                a[j] = output[j];
            }
        }
        return output;

    }
}
