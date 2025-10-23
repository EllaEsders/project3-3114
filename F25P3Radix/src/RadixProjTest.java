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
 * @version {Put Something Here}
 */
public class RadixProjTest extends TestCase
{
    private CheckFile fileChecker;

    /**
     * This method sets up the tests that follow.
     */
    public void setUp()
    {
        fileChecker = new CheckFile();
    }


    /**
     * Fail a sort
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testFailSort()
        throws Exception
    {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.txt", 1, "b");
//        String fileName = "input.txt";
//        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(
//            new FileInputStream(fileName)))) {
//
//            int count = 0;
//            while (dis.available() >= 4) { // while at least 4 bytes remain
//                int value = dis.readInt(); // read one 4-byte integer
//
//                // Format as 32-bit binary with leading zeros
//                String binaryString = String.format("%32s", Integer
//                    .toBinaryString(value)).replace(' ', '0');
//
//                System.out.println("Value " + (++count) + ": " + binaryString
//                    + " (" + value + ")");
//            }
//
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        assertFalse(fileChecker.checkFile("input.txt"));
        System.out.println("Done testFailSort");
    }
    /**
     * pass a sort
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSort()
        throws Exception
    {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.txt", 1, "b");
      String fileName = "input.txt";
      try (DataInputStream dis = new DataInputStream(new BufferedInputStream(
          new FileInputStream(fileName)))) {

          int count = 0;
          while (dis.available() >= 4) { // while at least 4 bytes remain
              int value = dis.readInt(); // read one 4-byte integer

              // Format as 32-bit binary with leading zeros
              String binaryString = String.format("%32s", Integer
                  .toBinaryString(value)).replace(' ', '0');
              FileWriter myWriter = new FileWriter("Hello.txt");
              myWriter.write("Value " + (++count) + ": " + binaryString
                  + " (" + value + ")");
              System.out.println("Value " + (++count) + ": " + binaryString
                  + " (" + value + ")");
          }

      }
      catch (IOException e) {
          e.printStackTrace();
      }
        RadixProj.main(new String[] { "input.txt", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.txt"));
        System.out.println("Done testSortPass");
    }
    /**
     * pass a sort type A
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSortTypeA()
        throws Exception
    {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.txt", 1, "a");
        RadixProj.main(new String[] { "input.txt", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.txt"));
        System.out.println("Done testPassSortTypeA");
    }
    /**
     * pass a sort type A with lots of vars
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSortTypeALong()
        throws Exception
    {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.txt", 3, "a");
        RadixProj.main(new String[] { "input.txt", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.txt"));
        System.out.println("Done testPassSortTypeA");
    }
    /**
     * pass a sort
     * 
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testPassSortLong()
        throws Exception
    {
        FileGenerator it = new FileGenerator();
        it.generateFile("input.txt", 1, "b");
        RadixProj.main(new String[] { "input.txt", "statsFile.txt" });
        assertTrue(fileChecker.checkFile("input.txt"));
        System.out.println("Done testSortPass");
    }
    
    
}
