import java.util.ArrayList;
// author Jovany Pena 8/29/18


public class MyQueue{

   ArrayList<String> list = new ArrayList<String>(20);

   public void enqueue(String s){
      list.add(s);
   }
   public void dequeue(){
      list.remove(0);
   }
   public boolean isEmpty(){
      if (list.isEmpty()){
         System.out.println("Empty");
         return true;
     }
      System.out.println("Not Empty");
      return false; 
   } 
   public void printQueue(){
      for( int i=0; i< list.size(); i++)
      {
         System.out.println(list.get(i));
      }
   }
   public static void main(String[] args){
   
   MyQueue newQueue= new MyQueue();
      
   newQueue.enqueue("a");
   newQueue.enqueue("b");
   newQueue.printQueue();
   newQueue.dequeue();
   newQueue.printQueue();
   newQueue.isEmpty();
   newQueue.dequeue();
  
  }
}