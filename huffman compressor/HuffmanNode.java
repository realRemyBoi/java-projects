import java.util.*;
public class HuffmanNode implements Comparable<HuffmanNode> {
   public int frequency;
   public int symbol;
   public HuffmanNode one;
   public HuffmanNode zero;
   
   public HuffmanNode(int frequency) {
      this.frequency = frequency;
   }

   public HuffmanNode(int frequency, int symbol) {
      this.frequency = frequency;
      this.symbol = symbol;
   }

   public int compareTo(HuffmanNode other) {
      return Integer.compare(this.frequency, other.frequency);
   }

}
