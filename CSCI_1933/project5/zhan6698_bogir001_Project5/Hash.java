import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Hash<T> {
    private NGen<T>[] table; // hashtable is an array with each index holding a generic node
    private int longestChain = 0;
    private int emptyIndices;

    public Hash(int size) {
        table = new NGen[size];
        emptyIndices = table.length;
        for (int i = 0; i < table.length; i++){
            table[i] = new NGen<T>();
        }
    }

    //gets all tokens from a text file and puts it in an linked list
    //format borrowed from TextScan.java
    private NGen<T> getTokens(String[] args) { // unused, keeping for notes
        Scanner readFile = null;

        System.out.println();
        System.out.println("Attempting to read from file: " + args[0]);
        try {
            readFile = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println("File: " + args[0] + " not found");
            System.exit(1);
        }
        System.out.println("Connection to file: " + args[0] + " successful");
        System.out.println();

        NGen<T> head = new NGen();
        while (readFile.hasNext()) {

            NGen<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            T addData = (T) readFile.next(); // string cast to type T
            NGen<T> newNode = new NGen();
            newNode.setData(addData);
            current.setNext(newNode);
        }

        System.out.println();
        System.out.println("Tokens found");
        System.out.println();

        return head;
    }

    //finds longest chain
    private void findLongestChain() {
        int[] lengths = new int[table.length]; // array of chain lengths
        for (int index = 0; index < table.length; index++) {
            int currentLength = 1;
            NGen<T> current = table[index];
            while (current.getNext() != null) {
                currentLength++;
                current = current.getNext();
            }
            lengths[index] = currentLength;
        }

        int max = lengths[0]; // finding max length of the array of chain lengths
        for (int i = 1; i < lengths.length; i++) {
            if (lengths[i] > max) {
                max = lengths[i];
            }
        }
        longestChain = max;
    }

    //adds items to the hash table
    public void add(T item) {
        int index = hash3(item); // generate index to evenly distribute items--change this to test the different hash functions

        if (table[index].getData() == null) { // if head is null
            table[index].setData(item);
            emptyIndices--;
        } else {
            NGen<T> current = table[index];
            boolean duplicateExists = false;
            while (current.getNext() != null) {
                if (current.getData().equals(item)) {
                    duplicateExists = true;
                }
                current = current.getNext();
            }
            if (duplicateExists == false) {
                NGen<T> addNode = new NGen();
                addNode.setData(item);
                current.setNext(addNode);
            }
        }
    }
    /*
    Hash functions ranked by effectiveness:
    1) hash3
    2) hash1
    3) hash2
     */

    //summing up characters
    private int hash1(T key) {
        //System.out.println("got here");
        char[] keys = key.toString().toCharArray(); // converts items to char array

        int index = 0; // hashed index
        for (int i = 0; i < keys.length; i++) {
            index += keys[i]; // adding integer with ASCII number of itemChar
        }
        //System.out.print("index: ");
        //System.out.println(index);
        return index % table.length;
    }

    /*
    This hash function works by summing up the numerical ASCII values of each index in the key given.
    This hash function is the second most effective hash function because it does a good job of evenly distributing data.
    It also does a good job of minimizing chain length and average
    collision length.
    This hash function is good for both general and specific cases.
     */

    //mid square method (takes the middle ASCI values of the key and squares them)
    //source: https://runestone.academy/runestone/books/published/pythonds/SortSearch/Hashing.html
    private int hash2(T key) {
        char[] keys = key.toString().toCharArray(); //convert all keys to strings and put in char array
        int index = 0; // hashed index
        for (int i = 0; i < keys.length; i++) {
            index += keys[i] * keys[i] * keys[i];
            index /= 3;
        }
        if (index < 0){
            index *= -1;
        }
        return index % keys.length;
    }
    /*
    This hash function works by multiplying the indices of the numerical ASCII values of the key given and then dividing the resulting number
    by the prime number 3.
    This hash function is the least effective because it does not do a good job of evenly distributing data for general or specific cases.
    It also has long chain lengths and a high average collision length.
    This hash function is not good for general or specific cases, compared to the other two. However, it is slightly better for specific cases.
     */
    //folding method
    //source: https://runestone.academy/runestone/books/published/pythonds/SortSearch/Hashing.html
    private int hash3(T key) {
        String itemString = key.toString(); //converts key to string
        int total = 0;
        if (itemString.length() > 1) {
            for (int i = 0; i < itemString.length() - 1; i = i + 2) {
                //check if index i + 1 exists
                total += itemString.charAt(i) * 10;
                total += itemString.charAt(i + 1);
            }
            if (itemString.length() / 2 != 0) { // if odd length
                total += itemString.charAt(itemString.length() - 1);
            }
        } else {
            return key.toString().charAt(0) % table.length;
        }
        //extracting the middle digits
        return total % table.length;
    }
    /*
    This hash function works by grouping each two characters in the string and concatenate the ASCII values in each group and adding their sums together.
    This function is the most effective because it does a good job of evenly distributing data for general case and specific case.
    It also does a good job of minimizing chain length and average collision length.
     */
    public void display() {
        findLongestChain();
        int longestLength = 0;
        int sum = 0;
        int avg = 0;
        int tokens = 0; //find unique tokens
        System.out.println("Hash table: ");
        for (int i = 0; i < table.length; i++) {
            sum = 0;
            System.out.print(i + ": ");
            NGen<T> current = new NGen();
            current = table[i];
            while (current != null && current.getData() != null) {
                sum += 1;
                tokens += 1;
                current = current.getNext();
            }
            System.out.print(sum);
            System.out.println();
        }
        int nonEmptyInt = table.length - emptyIndices;
        System.out.println("average collision length: " + tokens/nonEmptyInt); // how do you get the num of unique tokens
        System.out.println("longest chain: " + longestChain);
        System.out.println("unique tokens: " + tokens);
        System.out.println("empty indices: " + String.valueOf(emptyIndices));
        System.out.println("non-empty indices: " + String.valueOf(nonEmptyInt));
    }

    /*
        Specific hash table analysis:
        Our hash table has can be as small as 152 running hash3 for there to only be one collision.
        When we size the table near the number of keywords (around 150),the longest chain increases.
     */
    public static void main(String args[]) throws FileNotFoundException {
        try {
            File gettysburg = new File("gettysburg.txt");
            Scanner scanner = new Scanner(gettysburg);

            Hash myHashTable = new Hash<String>(141);

            while (scanner.hasNext()) {
                String line = scanner.next();
                myHashTable.add(line); // adds a single token
            }

           myHashTable.display();

        } catch (FileNotFoundException e) {
            System.out.println("Could not find file");
        }

        //specific case
        try {
            File keywords = new File("keywords.txt");
            Scanner scanner = new Scanner(keywords);


            Hash<String> hashSpecific = new Hash<String>(152);
            int numLines = 0;
            while (scanner.hasNext()) {
                numLines++;
                scanner.next();
            }
            String[][] tokens = new String[numLines][300];
            int numLinesCounter = 0;
            scanner = new Scanner(keywords);
            while (scanner.hasNext()) {
                String line = scanner.next();
                hashSpecific.add(line);
            }
            hashSpecific.display();

        } catch (Exception e) {
            System.out.println("File is not found");

        }

    }
}


