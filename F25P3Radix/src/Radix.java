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
    private ByteBuffer byteBuffer = ByteBuffer.allocate(900000);
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
        String fileName = "input.txt";

        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(
            new FileInputStream(fileName)))) {
            int fileSize = dis.available();
            int numInts = fileSize / 4;
            int[] values = new int[numInts];

            for (int i = 0; i < numInts; i++) {
                values[i] = dis.readInt();
            }
            for (int v : values) {
                numberOfBlocks++;
                System.out.println(v);
            }
           /*
            * right now it is not handling the keys and the input numbers correctly
            *       so it is inputed as #, 1 #, 2 and its counting 1 and 2 as numbers when they are not 
            * also it doesnt use the files reading and writing
            *   counting
            */
            for (int i = 1; i < 1000000001; i = i * 10) {
                values = sortDigit(values, numberOfBlocks, i);
            }
            int startIndex = 0;
            file.seek(startIndex * 4L);

            for (int n : values) {
                file.writeInt(n);
            }

        }
    }


    private int[] sortDigit(int[] a, int number, int val) {
        int[] output = new int[number];
        int[] count = new int[10];
        int i, j, rtok;
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
