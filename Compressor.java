// import statements
import java.io.*;
import java.util.Scanner;
import java.lang.String;
public class Compressor {
    public static void main(String[] args) {
        // initialize variables
        double startTime, endTime, totalTime;
        startTime = System.nanoTime();
        String input = "";
        input = args[0];
        File myFile;
        myFile = new File(args[0]);
        // extensions for filenames
        String extension = ".zzz";
        String log = ".zzz.log";
        int table_size = 233;
        boolean valid = false;
        BufferedReader file;
        Scanner scan = new Scanner(System.in);
        // continue to get filename and try to access until it is a valid filename and opened
        do {
            try {
                file = new BufferedReader(new FileReader(myFile));
                valid = true;
            }
            // catch block
            catch (FileNotFoundException e) {
                System.out.println("Invalid filename. Try Again.");
                System.out.println("Enter the filename.\n");
                String filer = scan.nextLine();
                input = filer;
                myFile = new File(filer);
            }
        } while (!valid);
        // create the hash table chain named table which has String keys and Integer values
        HashTableChain<String, Integer> table = new HashTableChain<>(table_size);
        // try to open the various methods that read and write to and from file
        try {
            // Buffered Reader
            file = new BufferedReader(new FileReader(myFile));
            FileOutputStream fos = new FileOutputStream(input + extension);
            // used for .read() method
            PushbackReader reader = new PushbackReader(new FileReader(myFile));
            // ObjectOutputStream to output to binary file
            ObjectOutputStream dos = new ObjectOutputStream(fos);
            // initialize the hash table to its initial values which are the ascii values 0 to 127
            for (int i = 0; i < 128; i++) {
                table.put(String.valueOf((char) i), i);
            }
            // initialize variables
            String WriteOut = "";
            String track = "";
            char lastVal = ' ';
            int count = 127;
            int check;
            int valueWrite;
            String test;
            // loop until there are no more characters in the file to be read
            while ((check = reader.read()) != -1) {
                // check is an int so cast it to be its relative character value
                char character = (char) check;
                track += character;
                // is in the dictionary
                if ((table.get(track)) != null) {
                    WriteOut = track;
                }
                // not in the dictionary
                else {
                    // increment count for inserting into dict
                    count++;
                    // get last character in track
                    lastVal = track.charAt(track.length() - 1);
                    // get value associated with what needs to be written
                    valueWrite = table.get(WriteOut);
                    // put the new key and value into table
                    table.put(track, count);
                    track = String.valueOf(lastVal);
                    // write to file
                    dos.writeInt(valueWrite);
                    // set WriteOut to character that was just read so that it avoids an issue with
                    // if there are back-to-back unknown strings checked against the table
                    WriteOut = Character.toString(character);
                }
            }
            //close all read and write methods
            file.close();
            dos.close();
            fos.close();
        }
        //catch blocks
        catch(FileNotFoundException e) {
            System.out.println("File Not Found.\n" + e.getMessage());
        }
        catch(IOException e) {
            System.out.println("An error occurred while writing to the file.\n" + e.getMessage());
        }
        //variables
        File newFile = new File(input + extension);
        endTime = System.nanoTime();
        //calculate total time it took to run
        totalTime = (endTime - startTime) / 1000000000.0;
        //write out to the log file the information relating to compress program run
        try {
            FileWriter newWriter = new FileWriter(input + log);
            newWriter.write("Compression of " + input);
            if(myFile.length() < 1000) {
                newWriter.write("\nCompressed to " + myFile.length() + " Bytes from " + newFile.length() + " Bytes.");
            }
            else{
                newWriter.write("\nCompressed to " + myFile.length()/1000 + " Kilobytes from " + newFile.length()/1000 + " Kilobytes.");
            }
            newWriter.write("\nCompression took " + totalTime + " seconds.");
            newWriter.write("\nThe dictionary contains " + table.size() + " total entries.");
            newWriter.write("\nThe table was rehashed " + table.getRehashes() + " times.");
            newWriter.close();
        }
        //catch block
        catch(IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            }
    }

}
