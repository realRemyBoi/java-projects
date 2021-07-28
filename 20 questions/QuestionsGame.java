/* The QuestionGame class has the ability to create a new questions game,
*  where the computer will ask the user a pre-determined set of yes/no questions about
*  what object the user is thinking of. Each question will be based on the answer to 
*  the previous question, and if the computer fails to get the object correct, it will ask
*  the user for their answer, a question that makes that answer unique, and if the answer is
*  a 'yes' or a 'no' to that question. The computer will write this game's q's and a's to its
*  database (a file) that it can read in future games, making it 'smarter' in the process.
*  Isaac Remy
*/
import java.util.*;
import java.io.*;

public class QuestionsGame {
   private Scanner console;
   private QuestionNode rootQuestionNode;
    
   // Constructs a new QuestionsGame, initializing the set of q's and a's
   // with a single answer ('computer').
   public QuestionsGame() {
      console = new Scanner(System.in);
      rootQuestionNode = new QuestionNode("computer");
   }

   // Responsible for taking an already existing file of questions
   // and answers and creating a new tree of the given questions and
   // answers that will determine how the computer asks its questions
   // and finds the user's object.
   public void read(Scanner input) {
      rootQuestionNode = readHelper(input);
   }
    
   // Private recursive read-helper method that returns the whole
   // new tree of q's and a's when it's finished reading.
   private QuestionNode readHelper(Scanner input) {
      String qOrA = input.nextLine();
      String string = input.nextLine();
      QuestionNode root = new QuestionNode(string);
      if (input.hasNext()) {
         if (qOrA.equals("Q:")) {
            root.yes = readHelper(input);
            root.no = readHelper(input);
         }
      }
      return root;
   }
    
   // The write method is passed the file location to write the now-finished
   // game set of questions to. It will write all q's and a's, including any
   // new ones that the user has given the computer.
   public void write(PrintStream output) {
      write(output, rootQuestionNode);
   }
    
   // Private recursive write-helper method.
   private void write(PrintStream output, QuestionNode root) {
      if (root != null) {
         if (root.no == null && root.yes == null) {
            output.println("A:");
            output.println(root.data);
         } else {
            output.println("Q:");
            output.println(root.data);
         }
         write(output, root.yes);
         write(output, root.no);
      }
   }
    
   // Responsible for actually asking the user the questions based on what's
   // already been read. Once out of questions, it will start guessing the user's
   // object. If its answer is incorrect, it will add the user's new answer and question
   // for that answer to the already existing set of q's and a's.
   public void askQuestions() {
      askQuestions(rootQuestionNode, rootQuestionNode);
   }
    
   // Private recursive askQuestions helper method. 
   private void askQuestions(QuestionNode root, QuestionNode parent) {
      String data = root.data;
      if (root.yes == null && root.no == null) {
         data = "Would your object happen to be " + data + "?";
      }
      boolean prompt = yesTo(data);
      if (prompt && root.yes == null && root.no == null) {
         System.out.println("Great, I got it right!");
      } else if (prompt && root.yes != null) {
         askQuestions(root.yes, root);
      } else if (!prompt && root.no != null) {
         askQuestions(root.no, root);
      } else {
         System.out.print("What is the name of your object? ");
         String object = console.nextLine().trim();
         System.out.println("Please give me a yes/no question that");
         System.out.println("distinguishes between your object");
         System.out.print("and mine--> ");
         String question = console.nextLine().trim();
         root = newNode(object, question, root, parent); 
      }
      rootQuestionNode = root;
   }
    
   // Private helper method adds and replaces the last answer node with a new node that
   // represents the question that the user gave to their new answer. It shifts the other
   // answer node to be an answer to this new question accordingly.
   private QuestionNode newNode(String object, String question, QuestionNode root, QuestionNode parent) {
      QuestionNode newNode = new QuestionNode(question);
      if (yesTo("And what is the answer for your object?")) {
         newNode.yes = new QuestionNode(object);
         newNode.no = root;
         parent = changeParent(parent, root, newNode);
         return newNode;
      } else {
         newNode.no = new QuestionNode(object);
         newNode.yes = root;
         parent = changeParent(parent, root, newNode);
         return newNode;
      }
   }

   // Private helper method that helps shift around the parent QuestionNode based on
   // the already existing parent, the root/child, and the new QuestionNode.
   private QuestionNode changeParent(QuestionNode parent, QuestionNode root, QuestionNode newNode) {
      if (root == parent.yes) {
         parent.yes = newNode;
      } else if (root == parent.no) {
         parent.no = newNode;
      } else if (root == parent) {
         parent = newNode;
      }
      return parent;
   }
    
   // Do not modify this method in any way
   // post: asks the user a question, forcing an answer of "y" or "n";
   //       returns true if the answer was yes, returns false otherwise
   public boolean yesTo(String prompt) {
      System.out.print(prompt + " (y/n)? ");
      String response = console.nextLine().trim().toLowerCase();
      while (!response.equals("y") && !response.equals("n")) {
         System.out.println("Please answer y or n.");
         System.out.print(prompt + " (y/n)? ");
         response = console.nextLine().trim().toLowerCase();
      }
      return response.equals("y");
   }
}