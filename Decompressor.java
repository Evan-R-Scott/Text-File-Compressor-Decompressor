//import statements
import java.io.*;
import java.util.Scanner;
import java.lang.String;

public class Decompressor {
    public static void main(String[] args) {
        // initialize necessary variables to calculate time
        double startTime, endTime, totalTime;
        startTime = System.nanoTime();
        // initialize file variables put in command line
        String input = "";
        input = args[0];
        File myFile;
        myFile = new File(args[0]);
        // more miscellaneous initializations, including scanner, table size, and .log string
        String log = ".log";
        int table_size = 233;
        boolean valid = false;
        BufferedReader fileBuff;
        Scanner scan = new Scanner(System.in);
        // do while loop that executes until a valid file is provided by user, prompts user to enter in new filename if not valid.
        do {
            try {
                fileBuff = new BufferedReader(new FileReader(myFile));
                valid = true;
            } catch (FileNotFoundException e) {
                System.out.println("Invalid filename. Try Again.");
                System.out.println("Enter the filename.\n");
                String filer = scan.nextLine();
                input = filer;
                myFile = new File(filer);
            }
        } while (!valid);
        // implement HashTable into program set at table size initialized earlier.
        HashTableChain<Integer, String> table = new HashTableChain<>(table_size);
        // loops through 128 characters putting them in the hash table
        for (int i = 0; i < 128; i++) {
            table.put(i, String.valueOf((char) i));
        }

        // cuts off the .zzz of the filename inputted by user
        int dotIndex = input.lastIndexOf(".");
        String filename = input.substring(0, dotIndex);
        try {
            // initializes all stream and writer objects to read in the binary file and write out the decompressed code
            FileInputStream file = new FileInputStream(myFile);
            ObjectInputStream obj = new ObjectInputStream(file);
            FileWriter writer = new FileWriter(filename);
            // initialize variables for hash table additions in while loop below
            String entry = "";
            int chFirst;
            String newQ = "";
            boolean val = true;
            int count = 127;
            // gets the p starting value of file and writes to hash table
            int p = obj.readInt();
            writer.write(table.get(p));
            // iterates through the file until no more bytes are available to be read
            while (obj.available() > 0) {
                // reads each binary value
                chFirst = obj.readInt();
                // first character is in the dict get it and assign it to entry
                if (chFirst < table.size()) {
                    entry = table.get(chFirst);
                    // else, entry is assigned to p and the first character of previous
                } else {
                    entry = table.get(p) + table.get(p).charAt(0);
                }
                // write to the file whatever entry was assigned to
                writer.write(entry);
                // accumulate count variable and insert the new pair found into the table
                count++;
                table.put(count, table.get(p) + entry.charAt(0));
                // reassign p to chFirts
                p = chFirst;
            }
            // close out of streams and writers
            obj.close();
            file.close();
        }
        // all catch blocks for possible throws
        catch (StreamCorruptedException e) {
            System.out.println("Decompression Completed");
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.\n" + e.getMessage());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.\n" + e.getMessage());
        }
        // create Decompression log file
        File newFile = new File(filename + log);
        // calculate time took
        endTime = System.nanoTime();
        totalTime = (endTime - startTime) / 1000000000.0;
        // try block that writes to log file the results
        try {
            FileWriter newWriter = new FileWriter(input + log);
            newWriter.write("Decompression of " + input);
            newWriter.write("\nDecompression took " + totalTime + " seconds.");
            newWriter.write("\nThe table was doubled " + table.getRehashes() + " times.");
            newWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
