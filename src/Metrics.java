import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import static java.util.Arrays.asList;
import picocli.CommandLine;

//current metics program. Needs to be tested again to make sure previous functions still work correctly.
//Untouched currently but needs to be modified to work with our current Metrics group project.
public class Metrics implements Runnable, IMetrics {
    @picocli.CommandLine.Option(names = {"-l", "--lines"})
    static ArrayList<String> lines;
    @picocli.CommandLine.Option(names = {"-w", "--words"})
    static ArrayList<String> words;
    @picocli.CommandLine.Option(names = {"-c", "--characters"})
    static ArrayList<String> characters;
    @picocli.CommandLine.Option(names = {"-s", "--sourcelines"})
    static ArrayList<String> sourcelines;
    @picocli.CommandLine.Option(names = {"-C", "--commentlines"})
    static ArrayList<String> commentlines;
    @picocli.CommandLine.Option(names = {"-h", "--help"})
    static ArrayList<String> help;
    @picocli.CommandLine.Option(names = {"-H", "--Halstead"})
    static ArrayList<String> hal;
    @picocli.CommandLine.Parameters
    static ArrayList<String> positional;


    public void run() {
    }

    @Override
    public boolean setPath(String path) {
        return readCorrect;
    }

    @Override
    public boolean isSource() {
        return jc;
    }

    static int totchar = 0;
    static int totcount = 0;
    static int totword = 0;
    static int totComTrack = 0;
    static int totSourceTrack = 0;
    static ArrayList<String> uniqOperators = new ArrayList<String>();//four main Hal metrics needed
    static int totalOperators = 0;
    static ArrayList<String> uniqOperands = new ArrayList<String>();
    static int totalOperands = 0;
    static boolean jc;
    static boolean readCorrect = true;
    static boolean wcParams = false;
    static boolean wasRead = false;

    public static void main(String[] args) {
        boolean headerYes = false;
        jc = false;
        int tic = 0;
        boolean linesbol = false;
        boolean wordsbol = false;
        boolean charbol = false;
        boolean srcbol = false;
        boolean combol = false;
        boolean multiComment = false;
        String fileName = "";
        CommandLine.run(new Metrics(), System.err, args);
        ArrayList<String> allArgs = groupFiles(lines, words, characters, sourcelines, commentlines, positional, hal);
        if (positional != null) {
            wcParams = true;
        }
        if (lines != null || wcParams) {
            linesbol = true;
        }
        if (words != null || wcParams) {
            wordsbol = true;
        }
        if (characters != null || wcParams) {
            charbol = true;
        }
        if (sourcelines != null) {
            srcbol = true;
        }
        if (commentlines != null) {
            combol = true;
        }
        if (args.length == 0) {
            System.out.println("0 0 0 ");
        } else if (help != null) {
            instructions();
        }
        bucket push = new bucket(wcParams,wasRead,allArgs);
        headerPrint(linesbol, wordsbol, charbol, srcbol, combol);
        if (help == null) {
            reader(fileName,allArgs,tic,headerYes,wasRead,multiComment,wcParams);
        }

        printer(wcParams, wasRead, allArgs);
    }
    static void reader(String fileName, ArrayList<String> allArgs, int tic,boolean headerYes,boolean wasRead,boolean multiComment,boolean wcParams){
        hal abc = new hal();
        int charz = 0;
        int count = 0;
        int wordz = 0;
        int comtrack = 0;
        int sourcetrack = 0;
    for(int i = 0; i<allArgs.size();i++)
    {
        try {

            fileName = allArgs.get(tic);
            FileReader reading = new FileReader(fileName);
            BufferedReader buff = new BufferedReader(reading);
            String line;
            while ((line = buff.readLine()) != null) {
                if (line.contains(".java") || line.contains(".c") || line.contains(".h") || line.contains(".cpp") || line.contains(".hpp")) {
                    jc = true;
                }
                if (line.contains("//") || line.contains("/*") || line.contains("*/") || multiComment) {
                    comtrack++;
                    if (line.contains("/*")) {
                        multiComment = true;
                    }
                    if (line.contains("*/")) {
                        multiComment = false;
                    }
                }
                if (!multiComment) {
                    if (line.contains("//") || line.contains("/*")) {
                        if (line.lastIndexOf("//") >= 2 || line.lastIndexOf("/*") >= 2) {
                            if (!line.equals(""))
                                sourcetrack++;
                            abc.exectue(line);
                        }
                    } else {
                        sourcetrack++;
                        abc.exectue(line);  //fixed previous issue
                    }
                }


                charz = charz + line.length();
                wordz = wordz + line.split("\\s+").length;
                if (!line.equals("")) {
                    count++;
                }

                ArrayList<String> temp;
                temp = abc.getOps();
                for (int j = 0; j < temp.size(); j++) {
                    if (!uniqOperators.contains(temp.get(j))) {
                        uniqOperators.add(temp.get(j));
                    }
                }
                temp = abc.getRands();
                for (int k = 0; k < temp.size(); k++) {
                    if (!uniqOperands.contains(temp.get(k))) {
                        uniqOperands.add(temp.get(k));
                    }
                }
                totalOperands += abc.getTrands();
                totalOperators += abc.getTops();

            }
            buff.close();
        } catch (Exception FileNotFoundException) {
            readCorrect = false;
            System.out.println("Not able to read file");
        }
        if (sourcelines != null || commentlines != null) {
            headerYes = true;
        }
        if (positional != null) {
            wcParams = true;
        }
        if (lines != null || wcParams) {
            if (count != 0) {
                wasRead = true;
                System.out.print(count + "      ");
            }
        }
        if (words != null || wcParams) {
            if (wordz != 0) {
                wasRead = true;
                System.out.print(wordz + "      ");
            }
        }
        if (characters != null || wcParams) {
            if (charz != 0) {
                wasRead = true;
                System.out.print(charz + "         ");
            }
        }
        if (sourcelines != null) {
            if (sourcetrack != 0) {
                wasRead = true;
                System.out.print(sourcetrack + "             ");
            }
        }
        if (commentlines != null) {
            if (comtrack != 0) {
                wasRead = true;
                System.out.print(comtrack + "        ");
            }
        }
        if (wasRead) {
            if (count != 0) {
                System.out.print(fileName);
            }
        }
        System.out.println(" ");
        totchar = totchar + charz;
        totcount = totcount + count;
        totword = totword + wordz;
        totComTrack = totComTrack + comtrack;
        totSourceTrack = totSourceTrack + sourcetrack;
        charz = 0;
        count = 0;
        wordz = 0;
        sourcetrack = 0;
        comtrack = 0;
        tic++;
    }

}
    static void printer(boolean wcParams, boolean wasRead,ArrayList<String>allArgs){
        if(lines!=null||wcParams) {
            System.out.print(totcount + "      ");
        }
        if(words!=null||wcParams) {
            System.out.print(totword + "     ");
        }
        if (characters!=null||wcParams) {
            System.out.print(totchar + "        ");
        }
        if (sourcelines!=null) {
            System.out.print(totSourceTrack + "           ");
        }
        if (commentlines!=null) {
            System.out.print(totComTrack + "       ");
        }
        if(wasRead&&allArgs.size()>=1) {
            System.out.print("Total");
        }
        System.out.println("");
        System.out.println("total operators:"+totalOperators);
        System.out.println("total operands:"+totalOperands);
        System.out.println("unique operands:"+uniqOperands.size());
        System.out.println("unique operators:"+uniqOperators.size());
        fundamental testingThis= new fundamental(uniqOperators,uniqOperands,totalOperators,totalOperands);
        System.out.println("program vocabulary: "+testingThis.getProgVocab());
        System.out.println("program length: "+testingThis.getProgLength());
        System.out.println("volume: "+ testingThis.getVolume());
        System.out.println("Difficulty: "+testingThis.getDifficulty());
        System.out.println("effort:"+ testingThis.getEffort());
        System.out.println("time required to program in seconds: "+testingThis.getTimeReq());
        System.out.println("delivered bugs: "+testingThis.getBugs());

    }
    public static void instructions(){
         System.out.println("-h or --help will print out these instructions ");
         System.out.println("-l or --lines before a file name will print the line count of a file");
         System.out.println("-w or --words before a file name will print the word count of a file");
         System.out.println("-c or --Characters before a file name will print the character count of a file");
         System.out.println("-s or --sourcelines before a file name will print the sourceline count of a file");
         System.out.println("-C or --commentlines before a file name will print the commentline count of a file");
         System.out.println("-H or --Halstead before a file name will print the Halstead metrics of a file");


    }

    @Override
    public int getLineCount() {
        return totcount ;
    }

    @Override
    public int getWordCount() {
        return totword;
    }

    @Override
    public int getCharacterCount() {
        return totchar;
    }

    @Override
    public int getSourceLineCount() {
        return totSourceTrack;
    }

    @Override
    public int getCommentLineCount() {
        return totComTrack;
    }

    @Override
    public int getHalsteadn1() {
        return uniqOperators.size();
    }

    @Override
    public int getHalsteadn2() {
        return uniqOperands.size();
    }

    @Override
    public int getHalsteadN1() {
        return totalOperators;
    }

    @Override
    public int getHalsteadN2() {
        return totalOperands;
    }

    @Override
    public int getHalsteadVocabulary() {
        return getHalsteadn1()+getHalsteadn2();
    }

    @Override
    public int getHalsteadProgramLength() {
        return getHalsteadN1()+getHalsteadn2();
    }

    @Override
    public int getHalsteadCalculatedProgramLenght() {
       int a=getHalsteadn1();
      int  b= getHalsteadn2();
        return (int) (a*(Math.log(a)/Math.log(2))+b*(Math.log(b)/Math.log(2)));
    }

    @Override
    public int getHalsteadVolume() {
        return (int) (getHalsteadProgramLength()*(Math.log(getHalsteadVocabulary())/Math.log(2)));
    }

    @Override
    public int getHalsteadDifficulty() {
        int a=getHalsteadn1();
        int b=getHalsteadn2();
        int d=getHalsteadN2();
        return (a/2)*(d/b);
    }

    @Override
    public int getHalsteadEffort() {
        return getHalsteadDifficulty()*getHalsteadVolume();
    }

    @Override
    public int getHalsteadTime() {
        return getHalsteadEffort()/18;
    }

    @Override
    public int getHalsteadBugs() {
        return getHalsteadVolume()/3000;
    }


    public static void headerPrint(boolean l, boolean w, boolean c, boolean s, boolean cm){//this is actually never called yet
         if(s||cm) {
             if (l) {
                 System.out.print("Lines   ");
             }
             if (w) {
                 System.out.print("Words  ");
             }
             if (c) {
                 System.out.print("Characters   ");
             }
         }
            if(s){
                System.out.print("SourceLines   ");
                if(!cm){
                    System.out.println("");
                }
            }
            if(cm){
                System.out.print("Comments   ");
                System.out.println("");

        }
   }
   public static ArrayList<String> groupFiles(ArrayList<String> l,ArrayList<String> w,ArrayList<String> c,ArrayList<String> s,ArrayList<String> cm,ArrayList<String> positional, ArrayList<String> h){
         ArrayList<String> ret = new ArrayList<String>();
         if (l!=null) {
             ret.addAll(l);
         }
        if (w!=null) {
           ret.addAll(w);
        }
        if (c!=null) {
           ret.addAll(c);
        }
        if (s!=null) {
           ret.addAll(s);
        }
        if (cm!=null) {
           ret.addAll(cm);
        }
       if (h!=null) {
           ret.addAll(cm);
       }
      return ret;
     }

 }
class operatorz{
    ArrayList<String> notOps = new ArrayList<String>(asList("a", "A", "b", "B", "c", "C", "d", "D", "e", "E", "f", "F", "g", "G", "h", "H", "i", "I", "j", "J", "k", "K", "l", "L", "m", "M", "n", "N", "o", "O", "p", "P", "q", "Q", "r", "R", "s", "S", "t", "T", "u", "U", "v", "V", "w", "W", "x", "X", "y", "Y", "z", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
    ArrayList<String> retOps=new ArrayList<String>();
    StringTokenizer st;


    public operatorz() {
    }
    ArrayList<String> cycle(String s){//the issue here is that cycle is called when st has no value in it. Due to the previous declaration of a string array
        st= new StringTokenizer(s,"");
        while(st.hasMoreTokens()){//the fix would be to have a string array, with a method that fills it, and that goes through a cycle but for string arrays
            String temp=st.nextToken();//or just change the string array
            if(!notOps.contains(temp)){
                retOps.add(temp);
            }
        }
        return retOps;
    }

 }
 class operandz{
    ArrayList<String> keywords= new ArrayList<String>();
    operatorz mask =new operatorz();
    ArrayList<String> retRand=new ArrayList<String>(asList("-","+",".","(",")","int","String","if","and","for"));   //this is where I would add all the key words, exact same method as above
     public operandz()
     {
     }

     ArrayList<String> cycle(String s){
         String[] words = s.split("\\W+");
         mask.cycle(s);
         for(int i=0; i<words.length;i++){
             if(!keywords.contains(words[i])&&!mask.cycle(s).contains(words[i])){
                 retRand.add(words[i]);
             }
         }
         return retRand;
    }
 }
 class hal{
    ArrayList<String> ops;
    ArrayList<String> rands;
    int tops;
    int trands;
    operatorz a= new operatorz();
    operandz b= new operandz();

    public hal(){
     }
     void exectue(String s){
        ops=a.cycle(s);
        rands=b.cycle(s);
        tops=ops.size();
        trands=rands.size();
     }
     ArrayList<String> getOps(){

        return ops;
     }
     ArrayList<String> getRands(){

        return rands;
     }
     int getTops(){

        return tops;
     }
     int getTrands(){

        return trands;
     }
 }
 class fundamental{
    int progVocab;
    int progLength;
    double calcProgLength;
    double volume;
    int difficulty;
    double effort;
    double timeReq;
    double bugs;
    public fundamental(ArrayList<String> a, ArrayList<String> b, int c, int d){
        progVocab=a.size()+b.size();
        progLength=c+d;
        calcProgLength=a.size()*(Math.log(a.size())/Math.log(2))+b.size()*(Math.log(b.size())/Math.log(2));
        volume=progLength*(Math.log(progVocab)/Math.log(2));
        difficulty=(a.size()/2)*(d/b.size());
        effort=difficulty*volume;
        timeReq=effort/18;
        bugs=volume/3000;
    }

     public int getProgVocab() {
         return progVocab;
     }

     public int getProgLength() {
         return progLength;
     }

     public double getCalcProgLength() {
         return calcProgLength;
     }

     public double getVolume() {
         return volume;
     }

     public int getDifficulty() {
         return difficulty;
     }

     public double getEffort() {
         return effort;
     }

     public double getTimeReq() {
         return timeReq;
     }

     public double getBugs() {
         return bugs;
     }
 }


