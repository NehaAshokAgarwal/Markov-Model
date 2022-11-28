package pa1;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import java.lang.Comparable;

public class BestModel {
    // Instance Variables
    private int order;  // order parameter k
    private String s1;  // name of file to build model1
    private String s2;  // name of file2 to build model2
    private String s3;  // name of the test_file
    private MarkovModel model1;  // MarkovModel1
    private MarkovModel model2;  //MarkovModel2
    private MaxPQ<DiffModel> PQ;  //PriorityQueue to store the DiffModel objects

    public BestModel(int order, String s1, String s2, String s3) {
        // Initialising the Instance Variables
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.order = order;
        this.PQ = new MaxPQ<DiffModel>();

        // Reading the file s1 and turning it to a string object.
        In in1 = new In(s1);
        String string1 = in1.readAll();

        // Reading the file s2 and turning it to a string object.
        In in2 = new In(s2);
        String string2 = in2.readAll();

        // Reading the file s3 and turning it to a string object.
        In in3 = new In(s3);
        String string3 = in3.readAll();

        // Creating the two models - model1 and model2 of MarkovModel data type with the following arguments.
        this.model1 = new MarkovModel(this.order, string1);
        this.model2 = new MarkovModel(this.order, string2);

        // Calling the AverageLikelihood() method to compute the average likelihood of the two respective models with their test file.
        double prob = model1.AverageLikelihood(string3);
        double prob2 = model2.AverageLikelihood(string3);

        // Determining the sign of the absolute value of the difference of the probabilities.
        String sign1 = (prob2 - prob)>0 ? "-" : "+";

        // Printing out the string, average likelihood of model1,average likelihood of model2, and their absolute difference with the sign computed above.
        System.out.printf("%s\t %.4f\t %.4f\t %s%.4f\n", s3, prob, prob2, sign1, Math.abs(prob - prob2));

        // If the string s in the model1...(s is retrieved from the getOne() method which returns the priority queue with the required substrings)
        for(String s : model1.getone()){
            // and if the same string s in model2 as well...
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
            // Determining the sign of the absolute difference of the average likelihood of the respective models.
            String sign2 = (obj.log2 - obj.log1)>0 ? "-" : "+";
            // Escaping the whitespaces to normal spaces while printing.
            String st1 = obj.s.replaceAll("\\s", " ");
            System.out.printf(" \"%s\"\t %.3f\t %.3f\t %s%.3f\n", st1, obj.log1, obj.log2, sign2, obj.abs);
            // Breaking the for loop when 10 string been printed out.
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
        int num = Integer.parseInt(args[0]); // 1st command line arguments (order of the model)
        String file1 = args[1]; // file name to build model1 (2nd command line argument)
        String fil2 = args[2]; // file name to build 3rd model. (3rd command line argument)
        //As long as there is a command line argument,which are the test files, create as many
        // BestModel objects with the following arguments.
        for (int i = 3; i < args.length; i++) {
            BestModel model = new BestModel(num, file1, fil2, args[i]);
        }
    }

    // Implemented private Inner class
    private static class DiffModel implements Comparable {
        // Instance Variables
        private String s; // string s
        private double log1; // log probability of the given string under model1
        private double log2; // log probability of the given string under model2
        private double abs; // difference between the log probabilities.

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
