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
    ByteBuffer byteBuffer = ByteBuffer.allocate(900000);
    /**
     * the file to be sorted
     */
    RandomAccessFile file;
    /**
     * the print writer to write in the stats file
     * (maybe change name to stats or something)
     */
    PrintWriter writer;
    /**
     * maybe allowed maybe not ints
     */
    int sizeOfBytes=0;
    int numberOfBlocks=0;
    int sizeOfBlocks=0;
    int diskReads=0;
    int diskWrites = 0; 
    int timeTook=0; //no clue how to implement this
    /**
     * Create a new Radix object.
     * @param theFile The RandomAccessFile to be sorted
     * @param s The stats PrintWriter
     *
     * @throws IOException
     */
    public Radix(RandomAccessFile theFile, PrintWriter s)
        throws IOException
    {
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
         *      for each digit
         *      1. have a count array from 0-9
         *      2. go through each number adding to the array pos that last digit ends with
         *      3. take the total and subtract by count[i] and place at count at i
         *      4. put records into bins from left to right
         *              find last digit of number
         *              find count[digit]
         *              place in the output[count[digit]]
         *              add one to count[digit]
         * 3. return stats file some how
         *      may need
         *      1. count in for amount and size of memory pool
         *      2. count int for read
         *      3. count for write
         *      4. somehow determine time took?
         */
        String fileName = "input.txt";

       try (DataInputStream dis = new DataInputStream(
               new BufferedInputStream(new FileInputStream(fileName)))) {
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
           /* 2. sort by each digit
           *      for each digit
           *      1. have a count array from 0-9
           *      2. go through each number adding to the array pos that last digit ends with
           *      3. take the total and subtract by count[i] and place at count at i
           *      4. put records into bins from left to right
           *              find last digit of number
           *              find count[digit]
           *              place in the output[count[digit]]
           *              add one to count[digit]
        */
           for(int i =1; i<1000000000; i=i*10) {
               values=sortDigit(values, numberOfBlocks, i);
               for (int v : values) {
                   System.out.println(v+" values at: "+i);
               }
           }
           int startIndex = 0; // overwrite starting at 3rd int (0-based)
           file.seek(startIndex * 4L); // 4 bytes per int

           for (int n : values) {
               file.writeInt(n);
           }
           
        
        
        
 }
    }
       private int[] sortDigit(int[] a, int number, int val) {
           int[] output = new int[number];
           int[] count = new int[10];
           int i, j, rtok;
           for (i=0, rtok=1; i<10; i++, rtok*=10) { // For k digits
               for (j=0; j<10; j++) { count[j] = 0; }    // Initialize count

               // Count the number of records for each bin on this pass
               for (j=0; j<a.length; j++) { count[(a[j]/rtok)%10]++; }

               // After processing, count[j] will be index in B for first slot of bin j.
               int total = number;
               for (j=9; j>=0; j--) { total -= count[j]; count[j] = total; }

               // Put records into bins, working from left to right
               for (j=0; j<a.length; j++) {
                 output[count[(a[j]/rtok)%10]] = a[j];
                 count[(a[j]/rtok)%10] = count[(a[j]/rtok)%10] + 1;
               }
               for (j=0; j<a.length; j++) { a[j] = output[j]; } // Copy B back
             }
           return output;
       
    }
}
    
