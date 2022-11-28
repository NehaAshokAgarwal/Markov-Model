package pa1;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import java.lang.Comparable;

public class BestModel {
    // Instance Variables
    private int order;  
    private String s1;  
    private String s2;  
    private String s3;
    private MarkovModel model1;  
    private MarkovModel model2;  
    private MaxPQ<DiffModel> PQ;  

    public BestModel(int order, String s1, String s2, String s3) {
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.order = order;
        this.PQ = new MaxPQ<DiffModel>();
        In in1 = new In(s1);
        String string1 = in1.readAll();
        In in2 = new In(s2);
        String string2 = in2.readAll();
        In in3 = new In(s3);
        String string3 = in3.readAll();
        this.model1 = new MarkovModel(this.order, string1);
        this.model2 = new MarkovModel(this.order, string2);
        double prob = model1.AverageLikelihood(string3);
        double prob2 = model2.AverageLikelihood(string3);
        String sign1 = (prob2 - prob)>0 ? "-" : "+";
        System.out.printf("%s\t %.4f\t %.4f\t %s%.4f\n", s3, prob, prob2, sign1, Math.abs(prob - prob2));
        for(String s : model1.getone()){
            if(model2.getone().contains(s)){
                // Then create a DiffModel object called temp with the following arguments and insert it into the Max PQ.
                DiffModel temp = new DiffModel(s, model1.likelihood(s), model2.likelihood(s), Math.abs(model1.likelihood(s) - model2.likelihood(s)));
                PQ.insert(temp);
            }
        }
        // Printing out the 10 strings which has the greatest absolute difference between the average likelihood under model and model2.
        int i = 0;
        for (DiffModel model : PQ) {
            i++;
            DiffModel obj = model;
            String sign2 = (obj.log2 - obj.log1)>0 ? "-" : "+";
            String st1 = obj.s.replaceAll("\\s", " ");
            System.out.printf(" \"%s\"\t %.3f\t %.3f\t %s%.3f\n", st1, obj.log1, obj.log2, sign2, obj.abs);
            if(i == 10){
                System.out.println();
                break;
            }
        }
    }

    // Returning the model1
    public MarkovModel getModel1() {
        return this.model1;
    }

    // Returning the model2
    public MarkovModel getModel2() {
        return this.model2;
    }

    // Main function
    public static void main(String[] args) {
        int num = Integer.parseInt(args[0]); 
        String file1 = args[1]; 
        String fil2 = args[2]; 
        for (int i = 3; i < args.length; i++) {
            BestModel model = new BestModel(num, file1, fil2, args[i]);
        }
    }

    // Implemented private Inner class
    private static class DiffModel implements Comparable {
        // Instance Variables
        private String s; 
        private double log1; 
        private double log2; 
        private double abs; 

        // Constructor
        public DiffModel(String s, double log1, double log2, double abs) {
            // Initialising the Instance Variables
            this.s = s;
            this.log1 = log1;
            this.log2 = log2;
            this.abs = abs;
        }

        // Overriding Equals Method
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other == this) {
                return true;
            }
            if (other.getClass() == this.getClass()) {
                return true;
            }
            // Casting the other object to the DiffModel type.
            DiffModel o = (DiffModel) other;
            if (!o.s.equals(this.s)) return false;
            if (o.log1 != o.log1) return false;
            if (o.log2 != o.log2) return false;
            return o.abs == this.abs;
        }

        // Overiding the compareTo method which return 0 when this.abs equals to o.abs,
        // 1 when this,abs is greater than the o.abs, and -1 when this.abs is smaller than the o.abs.
        @Override
        public int compareTo(Object o) {
            return Double.compare(this.abs, ((DiffModel) o).abs);
        }

    }
}
