import java.nio.*;
import java.nio.channels.FileChannel;
import java.io.*;

// The Radix Sort implementation
// -------------------------------------------------------------------------
/**
 * This class implements Radix Sort on a file using a 900,000-byte memory pool
 * to sort records. Each record in the file consists of a key and a value.
 * 
 * @author ellae and Madelync05
 * @version 1
 */
public class Radix
{
    /**
     * The memory pool is a temporary working memory where blocks of data are
     * loaded onto and many records can be sorted instead of processing them one
     * at a time.
     */
    private byte[] memoryPool = new byte[900000];
    /**
     * Reads a large block of bytes in the memory pool
     */
    private ByteBuffer buffer = ByteBuffer.wrap(memoryPool);

    private RandomAccessFile file;
    private RandomAccessFile fileA;
    private RandomAccessFile fileB;

    private PrintWriter writer;

    private int diskReads = 0;
    private int diskWrites = 0;

    /**
     * Create a new Radix object.
     * 
     * @param theFile
     *            The RandomAccessFile to be sorted
     * @param s
     *            The stats PrintWriter
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
     * Controls the overall radix sort process by creating a temp file, calling
     * the passes, and swapping the temp and main files.
     * 
     * @throws IOException
     */
    private void radixSort()
        throws IOException
    {
        File tempFile = new File("tempfile.bin");

        fileA = file;
        fileB = new RandomAccessFile(tempFile, "rw");

        int radix = 256;

        for (int pass = 0; pass < 4; pass++)
        {
            countingPass(fileA, fileB, pass, radix);
            swapFiles();
            fileB.setLength(0);
        }

        if (fileA != file)
        {
            copyFile(fileA, file);
        }

        writer.println("Block size: " + 4096);
        writer.println("Disk reads: " + diskReads);
        writer.println("Disk writes: " + diskWrites);
        writer.flush();

        fileB.close();
        tempFile.delete();
    }


    /**
     * Swaps two files by creating a temporary file to house data while
     * swapping.
     */
    private void swapFiles()
    {
        RandomAccessFile temp = fileA;
        fileA = fileB;
        fileB = temp;
    }


    /**
     * Performs one pass of the sort process.
     * 
     * @param input
     *            original source file
     * @param output
     *            output file where sorted data goes
     * @param passIndex
     *            which pass we are currently on
     * @param radix
     *            256
     * @throws IOException
     */
    private void countingPass(
        RandomAccessFile input,
        RandomAccessFile output,
        int passIndex,
        int radix)
        throws IOException
    {
        int[] count = new int[radix];

        input.seek(0);
        int bytesRead;
        while ((bytesRead = input.read(memoryPool)) != -1)
        {
            diskReads++;
            buffer.clear();
            buffer.limit(bytesRead);
            while (buffer.remaining() >= 8)
            {
                int key = buffer.getInt();
                buffer.getInt();
                int digit = (key >>> (passIndex * 8)) & 0xFF;
                count[digit]++;
            }
        }

        long[] bytePos = new long[radix];
        long total = 0;
        for (int i = 0; i < radix; i++)
        {
            bytePos[i] = total;
            total += (long)count[i] * 8;
        }

        input.seek(0);
        output.setLength(0);

        int binBufferSize = 8192;
        byte[][] binBuf = new byte[radix][binBufferSize];
        int[] fill = new int[radix];

        while ((bytesRead = input.read(memoryPool)) != -1)
        {
            diskReads++;
            buffer.clear();
            buffer.limit(bytesRead);
            while (buffer.remaining() >= 8)
            {
                int key = buffer.getInt();
                int val = buffer.getInt();
                int digit = (key >>> (passIndex * 8)) & 0xFF;

                ByteBuffer bin = ByteBuffer.wrap(binBuf[digit]);
                bin.position(fill[digit]);
                bin.putInt(key);
                bin.putInt(val);
                fill[digit] += 8;

                if (fill[digit] >= binBufferSize)
                {
                    output.seek(bytePos[digit]);
                    output.write(binBuf[digit], 0, fill[digit]);
                    diskWrites++;
                    bytePos[digit] += fill[digit];
                    fill[digit] = 0;
                }
            }
        }

        for (int d = 0; d < radix; d++)
        {
            if (fill[d] > 0)
            {
                output.seek(bytePos[d]);
                output.write(binBuf[d], 0, fill[d]);
                diskWrites++;
            }
        }
    }


    /**
     * Writes the data from the source file into the destination file.
     * 
     * @throws IOException
     */
    private void copyFile(RandomAccessFile source, RandomAccessFile destination)
        throws IOException
    {
        source.seek(0);
        destination.setLength(0);
        int bytesRead;

        while ((bytesRead = source.read(memoryPool)) != -1)
        {
            diskReads++;
            destination.write(memoryPool, 0, bytesRead);
            diskWrites++;
        }
    }

}
