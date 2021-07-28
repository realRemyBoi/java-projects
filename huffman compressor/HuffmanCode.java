/* The HuffmanCode class is responsible for utilizing the Huffman encoding algorithm
*  to compress text files to be smaller, as well as decompress text files to their original
*  form. It can be given the frequency of all characters used in a file to construct the
*  Huffman code, which it will be used to compress the data into a smaller group of bits,
*  or it can be given an already existing Huffman code for a compressed file and decompress
*  that by reversing the Huffman encoding algorithm, and can then print the original contents
*  of that text file. 
*  Isaac Remy
*/
import java.util.*;
import java.io.*;
 
public class HuffmanCode {
   private HuffmanNode overallNode;
   
   // This constructor is passed the frequencies of all
   // characters used in a text file, and uses the frequencies to
   // initalize a Huffman code tree with the Huffman algorithm
   // so the file can be compressed into its smaller binary form.
   public HuffmanCode(int[] frequencies) {
      Queue<HuffmanNode> nodes = new PriorityQueue<>();
      for (int i = 0; i < frequencies.length; i++) {
         if (frequencies[i] != 0) {
            HuffmanNode charFrequency = new HuffmanNode(frequencies[i], i);
            nodes.add(charFrequency);
         }
      }
      while (nodes.size() > 1) {
         HuffmanNode zero = nodes.remove();
         HuffmanNode one = nodes.remove();
         HuffmanNode newNode = new HuffmanNode(zero.frequency + one.frequency);
         newNode.zero = zero;
         newNode.one = one;
         nodes.add(newNode);
      }
      overallNode = nodes.remove();
   }
   
   /* This constructor is passed a file to scan of an already
   *  existing Huffman code structure, which should be in pre-order/standard format. 
   *  It reads the file and re-creates a tree of that specific Huffman code, 
   *  which will be used to decompress the corresponding .short (new binary) file. 
   *  The tree is in the standard/pre-order format as well.
   */
   public HuffmanCode(Scanner input) {
      overallNode = new HuffmanNode(0);
      while (input.hasNext()) {
         int asciiValue = Integer.parseInt(input.nextLine());
         String code = input.nextLine();    
         overallNode = readCode(asciiValue, code, overallNode);
      }
   }
    
   // private recursive helper method for reading .code file
   private HuffmanNode readCode(int asciiValue, String code, HuffmanNode root) {
      if (code.length() == 0) {
         root = new HuffmanNode(0, asciiValue);
      } else {
         if (root == null) {
            root = new HuffmanNode(0);
         }
         if (code.charAt(0) == '0') {
            root.zero = readCode(asciiValue, code.substring(1), root.zero);
         } else {
            root.one = readCode(asciiValue, code.substring(1), root.one);
         }
      }
      return root;
   }
   
   // Takes the already-created Huffman code tree and prints out the
   // code to the passed file, which will be used to compress the file
   // into its smaller binary form. It prints the tree's contents
   // in the standard/pre-order format.
   public void save(PrintStream output) {
      save(output, overallNode, "");
   }
   
   // Private recursive save helper method.
   private void save(PrintStream output, HuffmanNode root, String bits) {
      if (root.zero == null && root.one == null) {
         output.println(root.symbol);
         output.println(bits);
      } else {
         save(output, root.zero, bits + "0");
         save(output, root.one, bits + "1");
      }
   }
   
   // Decompresses a file by reading the binary of the compressed file
   // and checking that against the re-created Huffman code. Using that code
   // as a key, it will print out each character to a passed file in the original
   // text form.
   public void translate(BitInputStream input, PrintStream output) {
      while (input.hasNextBit()) {
         translateHelper(input, overallNode, output);
      }
   }

   // Private recursive translate helper method.
   private void translateHelper(BitInputStream input, HuffmanNode root, PrintStream output) {
      if (root.zero == null && root.one == null) {
         output.write(root.symbol);
      } else {
         if (input.nextBit() == 0) {
            translateHelper(input, root.zero, output);
         } else {
            translateHelper(input, root.one, output);
         }
      }
   }
}