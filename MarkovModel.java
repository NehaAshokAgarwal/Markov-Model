package pa1;
import java.util.*;
public class MarkovModel {

    // Instance Variables
    private int k;  // the order
    private String s;  // the original string
    private HashMap<String, Integer> map; // HashMap to map strings with their respective frequencies
    private PriorityQueue <String> list1; // PriorityQueue to store the strings generated to compute likelihood for BestModel


    //constructor
    public MarkovModel(int k, String s){
        this.k = k;  //Initialising k
        this.s = s;  // Initialising s
        this.map = new HashMap<String,Integer>(); // Initialising map
        list1 = new PriorityQueue<>(); // Initialising PQ

        // Generating the frequencies for the (k) of the original string by calling generate(int i, int num, String string)
        for(int i = 0; i < this.s.length(); i++){
            String string = generate(i, k, this.s);
            if(map.containsKey(string)){
                map.put(string, map.get(string)+1); // given ST is a logarithmic performance.
            }
            else map.put(string, 1);

            //  Generating the frequencies for the (k+1) of the original string by calling generate(int i, int num, String string)
            // function and mapping it to their frequencies in the map.
            int num = k+1;
            String string2 = generate(i, num, this.s);
            if(map.containsKey(string2)){
                map.put(string2, map.get(string2)+1);
            }
            else{
                map.put(string2, 1);
            }
        }
    }

    // Computing the laplace of the string by retrieving the frequencies from the instance variable map.
    public double laplace(String s){
        double num = 1;
        double den = this.getS(); // linear method
        String Subs = s.substring(0, (this.k));
        if(map.containsKey(s)){
            num += map.get(s);
        }
        if(map.containsKey(Subs)){
            den += map.get(Subs);
        }
        return num/den;
    }

    // generating the substring of the original string s using the substring function and the relation
    // between the loop variable i and the order (k).
    public String generate(int i, int num, String string){
        int length = string.length();
        String s = "";
        // circular string
        if(i+num > string.length()){
            s = string.substring(i,length);
            s += string.substring(0, num-s.length());
        }
        else{
            s = string.substring(i,i+num);
        }
        return s;
    }

    // Returning the order
    public int getK(){
        return this.k;
    }

    // Returning the alphabet size of the original string
    public int getS() {
        Set<Character> set = new HashSet<>();
        int length = this.s.length();
        for(int i = 0; i < length; i++){
            char x = s.charAt(i);
            set.add(s.charAt(i));
        }
        return set.size();
    }
    // Computing the log probability of the given string s by calling the laplace method.
    public double likelihood(String string){
        double lapl = laplace(string);
        double likelihood = Math.log(lapl);
        return likelihood;
    }

    // Computing the log likelihood of the string given by calling the likelihood() and generate() method
    // defined above.
    public double totalLikelihood(String newS){
        int num = this.k+1;
        double totallikelihood = 0.0;
        for(int i = 0; i < newS.length(); i++){
            String s = generate(i, num, newS);
            list1.add(s); // add operation in a priority queue is log(n)
            double likelihood = likelihood(s);
            totallikelihood += likelihood;
        }
        return totallikelihood;
    }

    // Computing the Average Likelihood by simply diving the log likelihood computed by called the
    //totalLikelihood() method defined above and diving the number by the string's length.
    public double AverageLikelihood(String newS){
        return totalLikelihood(newS)/newS.length();
    }

    // string representation of the MarkovModel object
    public String toString(){
        StringBuilder temp = new StringBuilder("S = " + this.getS() + "\n");
        for(String s : map.keySet()){
            temp.append("\"").append(s).append("\"").append("\t").append(map.get(s)).append("\n");
        }
        return temp.toString();
    }

    // Returning the priority queue which has the strings generated of the given string.
    public PriorityQueue<String> getone(){
        return list1;
    }
    // Main function
    public static void main(String[] args) {
        MarkovModel model = new MarkovModel(1, "aabcabaacaac");
        System.out.println(model);
        System.out.println(model.laplace("aa"));
        System.out.println(model.laplace("ab"));
        System.out.println(model.laplace("bc"));
        //System.out.println(model.likelihood("aabca"));
        //System.out.println(model.AverageLikelihood("aabca"));
    }
}
