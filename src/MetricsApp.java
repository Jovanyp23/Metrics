public class MetricsApp {
   static IMetrics inst = new Metrics();
   static Metrics runner= new Metrics();
    public static void main(String[] args){

        runner.main(args);

    }
}
