//import statements
import java.io.*;
import java.util.Scanner;
import java.lang.String;

public class Decompress {
    public static void main(String[] args) {
        12
        13 // initialize necessary variables to calculate time
        14 double startTime, endTime, totalTime;
        15 startTime = System.nanoTime();
        16
        17 // initialize file variables put in command line
        18 String input = "";
        19 input = args[0];
        20 File myFile;
        21 myFile = new File(args[0]);
        22
        23 // more miscellaneous initializations, including scanner, table size, and .log string
        24 String log = ".log";
        25 int table_size = 233;
        26 boolean valid = false;
        27 BufferedReader fileBuff;
        28 Scanner scan = new Scanner(System.in);
        29
        30 // do while loop that executes until a valid file is provided by user, prompts user to enter in new filename if not valid.
        31 do {
            32 try {
                33 fileBuff = new BufferedReader(new FileReader(myFile));
                34 valid = true;
                35 } catch(FileNotFoundException e) {
                36 System.out.println("Invalid filename. Try Again.");
                37 System.out.println("Enter the filename.\n");
                38 String filer = scan.nextLine();
                39 input = filer;
                40 myFile = new File(filer);
                41 }
            42 } while(!valid);
        43
        44 // implement HashTable into program set at table size initialized earlier.
        45 HashTableChain<Integer, String> table = new HashTableChain<>(table_size);
        46
        47 // loops through 128 characters putting them in the hash table
        48 for(int i = 0; i < 128; i++){
            49 table.put(i, String.valueOf((char) i));
            50 }
        51
        52 // cuts off the .zzz of the filename inputted by user
        53 int dotIndex = input.lastIndexOf(".");
        54 String filename = input.substring(0, dotIndex);
        55
        56 try {
            57 // initializes all stream and writer objects to read in the binary file and write out the decompressed code
            58 FileInputStream file = new FileInputStream(myFile);
            59 ObjectInputStream obj = new ObjectInputStream(file);
            60 FileWriter writer = new FileWriter(filename);
            61
            62 // initialize variables for hash table additions in while loop below
            63 String entry = "";
            64 int chFirst;
            65 String newQ = "";
            66 boolean val = true;
            67 int count = 127;
            68
            69 // gets the p starting value of file and writes to hash table
            70 int p = obj.readInt();
            71 writer.write(table.get(p));
            72
            73 // iterates through the file until no more bytes are available to be read
            74 while(obj.available() > 0){
                75
                76 // reads each binary value
                77 chFirst = obj.readInt();
                78
                79 // first character is in the dict get it and assign it to entry
                80 if(chFirst < table.size()){
                    81 entry = table.get(chFirst);
                    82
                    83 // else, entry is assigned to p and the first character of previous
                    84 } else {
                    85 entry = table.get(p) + table.get(p).charAt(0);
                    86
                    87 }
                88
                89 // write to the file whatever entry was assigned to
                90 writer.write(entry);
                91
                92 // accumulate count variable and insert the new pair found into the table
                93 count++;
                94 table.put(count, table.get(p)+entry.charAt(0));
                95
                96 // reassign p to chFirts
                97 p = chFirst;
                98
                99 }
            100
            101 // close out of streams and writers
            102 obj.close();
            103 file.close();
            104 }
        105
        106 // all catch blocks for possible throws
        107 catch(StreamCorruptedException e){
            108 System.out.println("Decompression Completed");
            109 }
        110
        111 catch(FileNotFoundException e) {
            112 System.out.println("File Not Found.\n" + e.getMessage());
            113 }
        114
        115 catch(IOException e) {
            116 System.out.println("An error occurred while writing to the file.\n" + e.getMessage());
            117 }
        118
        119 // create Decompression log file
        120 File newFile = new File(filename + log);
        121
        122 // calculate time took
        123 endTime = System.nanoTime();
        124 totalTime = (endTime - startTime) / 1000000000.0;
        125
        126 // try block that writes to log file the results
        127 try {
            128 FileWriter newWriter = new FileWriter(input + log);
            129 newWriter.write("Decompression of " + input);
            130 newWriter.write("\nDecompression took " + totalTime + " seconds.");
            131 newWriter.write("\nThe table was doubled " + table.getRehashes() + " times.");
            132 newWriter.close();
            133 } catch(IOException e) {
            134 System.out.println("An error occurred while writing to the file: " + e.getMessage());
            135 }
        136
        137 }
}
