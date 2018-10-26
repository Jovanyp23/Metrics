import java.util.ArrayList;

public class bucket {
    boolean wcParams;
    boolean wasRead;
    ArrayList<String> allArgs=new ArrayList<String>();
   public bucket(boolean wcParams,boolean wasRead, ArrayList<String> allArgs){
       this.wcParams=wcParams;
       this.wasRead=wasRead;
       this.allArgs=allArgs;
   }
   boolean getwcParams(){
       return wcParams;
   }

    public boolean getWasRead() {
        return wasRead;
    }
    ArrayList<String> getAllArgs(){
       return allArgs;
    }
}
