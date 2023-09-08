// import statements
import java.io.*;
import java.util.Scanner;
import java.lang.String;
public class Compress {
    public static void main(String[] args) {
        13 // initialize variables
        14 double startTime, endTime, totalTime;
        15 startTime = System.nanoTime();
        16 String input = "";
        17 input = args[0];
        18 File myFile;
        19 myFile = new File(args[0]);
        20 long fileSizeKB = 0;
        21 // extensions for filenames
        22 String extension = ".zzz";
        23 String log = ".zzz.log";
        24 int table_size = 233;
        25 boolean valid = false;
        26 BufferedReader file;
        27 Scanner scan = new Scanner(System.in);
        28 // continue to get filename and try to access until it is a valid filename and opened
        29 do {
            30 try {
                31 file = new BufferedReader(new FileReader(myFile));
                32 valid = true;
                33 }
            34 // catch block
            35 catch (FileNotFoundException e) {
                36 System.out.println("Invalid filename. Try Again.");
                37 System.out.println("Enter the filename.\n");
                38 String filer = scan.nextLine();
                39 input = filer;
                40 myFile = new File(filer);
                41 }
            42 } while (!valid);
        43
        44 // create the hash table chain named table which has String keys and Integer values
        45 HashTableChain<String, Integer> table = new HashTableChain<>(table_size);
        46
        47 // try to open the various methods that read and write to and from file
        48 try {
            49 // Buffered Reader
            50 file = new BufferedReader(new FileReader(myFile));
            51
            52 FileOutputStream fos = new FileOutputStream(input + extension);
            53
            54 // used for .read() method
            55 PushbackReader reader = new PushbackReader(new FileReader(myFile));
            56
            57 // ObjectOutputStream to output to binary file
            58 ObjectOutputStream dos = new ObjectOutputStream(fos);
            59
            60 // initialize the hash table to its initial values which are the ascii values 0 to 127
            61 for (int i = 0; i < 128; i++) {
                62 table.put(String.valueOf((char) i), i);
                63 }
            64
            65 // initialize variables
            66 String WriteOut = "";
            67 String track = "";
            68 char lastVal = ' ';
            69 int count = 127;
            70 fileSizeKB = myFile.length() / 1024;
            71 int check;
            72 int valueWrite;
            73 String test;
            74
            75 // loop until there are no more characters in the file to be read
            76 while ((check = reader.read()) != -1) {
                77 // check is an int so cast it to be its relative character value
                78 char character = (char) check;
                79 track += character;
                80
                81 // is in the dictionary
                82 if ((table.get(track)) != null) {
                    83 WriteOut = track;
                    84 }
                85 // not in the dictionary
                86 else {
                    87 // increment count for inserting into dict
                    88 count++;
                    89 // get last character in track
                    90 lastVal = track.charAt(track.length() - 1);
                    91 // get value associated with what needs to be written
                    92 valueWrite = table.get(WriteOut);
                    93 // put the new key and value into table
                    94 table.put(track, count);
                    95 track = String.valueOf(lastVal);
                    96 // write to file
                    97 dos.writeInt(valueWrite);
                    98 // set WriteOut to character that was just read so that it avoids an issue with
                    99 // if there are back-to-back unknown strings checked against the table
                    100 WriteOut = Character.toString(character);
                    101 }
                102 }
            103
            104 //close all read and write methods
            105 file.close();
            106 dos.close();
            107 fos.close();
            108 }
        109 //catch blocks
        110 catch(FileNotFoundException e) {
            111 System.out.println("File Not Found.\n" + e.getMessage());
            112 }
        113 catch(IOException e) {
            114 System.out.println("An error occurred while writing to the file.\n" + e.getMessage());
            115 }
        116 //variables
        117 File newFile = new File(input + extension);
        118 long newFileSizeKB = newFile.length() / 1024;
        119 endTime = System.nanoTime();
        120
        121 //calculate total time it took to run
        122 totalTime = (endTime - startTime) / 1000000000.0;
        123
        124 //write out to the log file the information relating to compress program run
        125 try {
            126 FileWriter newWriter = new FileWriter(input + log);
            127 newWriter.write("Compression of " + input);
            128 newWriter.write("\nCompressed from " + fileSizeKB + " Kilobytes to " + newFileSizeKB + " Kilobytes.");
            129 newWriter.write("\nCompression took " + totalTime + " seconds.");
            130 newWriter.write("\nThe dictionary contains " + table.size() + " total entries.");
            131 newWriter.write("\nThe table was rehashed " + table.getRehashes() + " times.");
            132 newWriter.close();
            133 }
        134 //catch block
        135 catch(IOException e) {
            136 System.out.println("An error occurred while writing to the file: " + e.getMessage());
            137 }
        138 }

}
