import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import student.TestCase;

/**
 * This class was designed to test the Radix class by generating a random
 * ascii and binary file, sorting both and then checking each one with a file
 * checker.
 *
 * @author ellae and madelync05
 * @version 1.0
 */
public class RadixProjTest extends TestCase {
    private CheckFile fileChecker;

    /**
     * This method sets up the tests that follow.
     */
    public void setUp() {
        fileChecker = new CheckFile();
    }


    /**
     * Fail a sort
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testFailSort() throws Exception {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.txt", 1, "b");
// String fileName = "input.txt";
// try (DataInputStream dis = new DataInputStream(new BufferedInputStream(
// new FileInputStream(fileName)))) {
//
// int count = 0;
// while (dis.available() >= 4) { // while at least 4 bytes remain
// int value = dis.readInt(); // read one 4-byte integer
//
// // Format as 32-bit binary with leading zeros
// String binaryString = String.format("%32s", Integer
// .toBinaryString(value)).replace(' ', '0');
//
// System.out.println("Value " + (++count) + ": " + binaryString
// + " (" + value + ")");
// }
//
// }
// catch (IOException e) {
// e.printStackTrace();
// }
        assertFalse(fileChecker.checkFile("input.txt"));
        System.out.println("Done testFailSort");
    }


    /**
     * pass a sort
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSort() throws Exception {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.bin", 1, "b");
        String fileName = "input.bin";
        RandomAccessFile myFile = new RandomAccessFile("input.bin", "rw");
        RandomAccessFile testFile = new RandomAccessFile("input.bin", "rw");
        RadixProj.main(new String[] { "input.bin", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.bin"));
        System.out.println("Done testSortPass");
    }


    /**
     * pass a sort type A
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSortTypeA() throws Exception {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.bin", 1, "a");
        RadixProj.main(new String[] { "input.bin", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.bin"));
        System.out.println("Done testPassSortTypeA");
    }


    /**
     * pass a sort type A with lots of vars
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSortTypeB() throws Exception {
        FileGenerator gen = new FileGenerator();
        gen.generateFile("input.bin", 1, "b");
        RadixProj.main(new String[] { "input.bin", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.bin"));
        System.out.println("Done testPassSortTypeb");
    }


    /**
     * pass a sort type A with lots of vars
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSortTypeALong() throws Exception {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.bin", 3, "a");
        RadixProj.main(new String[] { "input.bin", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.bin"));
        System.out.println("Done testPassSortTypeA");
    }


    /**
     * pass a sort
     * 
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSortLong() throws Exception {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.bin", 1, "b");
        RadixProj.main(new String[] { "input.bin", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.bin"));
        System.out.println("Done testSortPass");
    }
    /**
     * pass a sort
     * 
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSortRealLong() throws Exception {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.bin", 100, "b");
        RadixProj.main(new String[] { "input.bin", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.bin"));
        System.out.println("Done testSortPassRealLong");
    }
}
