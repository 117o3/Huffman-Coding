package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.w3c.dom.html.HTMLDListElement;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);

        ArrayList <CharFreq> sort = new ArrayList<CharFreq>(); 
        double [] freq = new double [128];
        int count = 0;
        while(StdIn.hasNextChar() != false) {
            freq[StdIn.readChar()] += 1;
            count++;
        }

        for (int i = 0; i <freq.length; i++){
        if (freq[i] == 0) {

        } else {
            CharFreq a = new CharFreq((char)i, freq[i]/count);
            sort.add(a); 
        }
        }
        if (sort.size() == 1) {
            CharFreq hld = sort.get(0);
            char x = hld.getCharacter();
            int hldInt = (int) x;
            if (hldInt == 127) {
                hldInt = 0;
            }
            else {
                hldInt += 1;
            }
            sort.add(new CharFreq((char) hldInt, 0));             
        }
        Collections.sort(sort);
        sortedCharFreqList = sort;

        }
    

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     * @return 
     */
   // public TreeNode makeTree() {
   /*     Queue<TreeNode> source = new Queue<TreeNode>();         
        Queue<TreeNode> target = new Queue<TreeNode>(); 
        TreeNode left = new TreeNode();
        TreeNode right = new TreeNode(); 
        int count = 0;

        for (int i= 0; i < sortedCharFreqList.size(); i++) {
            TreeNode node = new TreeNode(sortedCharFreqList.get(i), null, null);
            node.setData(sortedCharFreqList.get(i)); 
            source.enqueue(node); 
        }
        TreeNode root = target.dequeue();
        huffmanRoot = root; 
        return TreeNode; 
    }
    */
    public void makeTree (){
    Queue<TreeNode> source = new Queue<TreeNode>();
    Queue<TreeNode> target = new Queue<TreeNode>();
    TreeNode left = new TreeNode();
    TreeNode right = new TreeNode();
    int count = 0;
    int i = 0;
    while (i < sortedCharFreqList.size()) {
        TreeNode node = new TreeNode(sortedCharFreqList.get(i), null, null);
        node.setData(sortedCharFreqList.get(i));
        source.enqueue(node);
        i++;
    } 

    while(!(source.isEmpty() && target.size() == 1)){
        if (count == 0){
            left = source.dequeue();
            right = source.dequeue();
            count++;
        }
        else if (target.size() != 0 && source.size() != 0){
            TreeNode hld = target.peek();
            TreeNode hld2 = source.peek();
            CharFreq hldi = hld.getData();
            CharFreq hld2i = hld2.getData();
            if (hld2i.getProbOcc() <= hldi.getProbOcc() || hldi.getProbOcc() ==0){
                left = source.dequeue();
            }
            else{
                left = target.dequeue();
            }
            if (target.size() != 0 && source.size() != 0){
                TreeNode qhld = target.peek();
                TreeNode qhld2 = source.peek();
                CharFreq qhldi = qhld.getData();
                CharFreq qhld2i = qhld2.getData();
                if (qhld2i.getProbOcc() <= qhldi.getProbOcc() || qhldi.getProbOcc() == 0 ){
                    right = source.dequeue();
                }
                else{
                    right = target.dequeue();
                }
            }
            else if (target.size() != 0 && source.size() == 0){
                right = target.dequeue();
            }
            else if (target.size() == 0 && source.size() != 0){
                right = source.dequeue();
            }
        }
        else if (source.size() != 0 && target.size() == 0){
            left = source.dequeue();
            right = source.dequeue();
        }
        else if (target.size() != 0 && source.size() == 0){
            left = target.dequeue();
            right = target.dequeue();
        }
        CharFreq leftdata = left.getData();
        CharFreq rightdata = right.getData();
        CharFreq treeInfo = new CharFreq(null, (leftdata.getProbOcc() + rightdata.getProbOcc()));
        TreeNode tree = new TreeNode();
        tree.setData(treeInfo);
        tree.setLeft(left);
        tree.setRight(right);
        target.enqueue(tree);
    }
    TreeNode root = target.dequeue();
    huffmanRoot = root;
}
   

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    private String code (TreeNode root, String a, char b) {
        if (root == null) {
            return null;
        }
        if (root.getData().getCharacter() != null && root.getData().getCharacter() == b) {
            return a; 
        }
        String ptr = code(root.getLeft(), a + "0", b);
        if (ptr !=null) {
            return ptr;
        }
        ptr = code(root.getRight(), a + "1", b); 
        return ptr;
    }

    public void makeEncodings() {
        encodings = new String[128];
        for (int i = 0; i < 128; i++){
            encodings[i] = code(huffmanRoot, "", (char)i);

        }
        
    }
    /*     for (int i = 0; i < 128; i++) {
            if (c[i] != code(huffmanRoot, "", (char)i)) {
                break;  
            }
           else {
                i++; 
            }
            encodings = c; 
        }
    } */

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        String fencoding = "";
        while (StdIn.hasNextChar()) {
            fencoding+=encodings[StdIn.readChar()];
        }
        writeBitString(encodedFile, fencoding); 


    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
        TreeNode ptr = huffmanRoot; 
        String t = readBitString(encodedFile);
        String decodef = ""; 
        char [] fol = t.toCharArray();
        int i = 0; 
        while (i < fol.length){
            if (fol[i] == '0') {
                ptr = ptr.getLeft();
            } 
            else {
                ptr = ptr.getRight();
            } i++; 
          //  Character n = ptr.getData().getCharacter(); 
            if (ptr.getData().getCharacter()!= null) {
                decodef += ptr.getData().getCharacter();
                ptr = huffmanRoot;
            } i++; 
        }
    StdOut.print(decodef); 
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
