public class QuestionNode {
    public String data;
    public QuestionNode yes;
    public QuestionNode no;
    
    public QuestionNode(String data) {
      this.data = data;
      
    }
    
    public QuestionNode() {
      this(null);
    }

    
}
