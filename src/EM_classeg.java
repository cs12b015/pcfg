import java.util.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EM_classeg {
	
	 public static HashMap<Integer, HashMap<String , Double>> inputtreemaps ; 
	 public static HashMap<Integer, Double> score = new  HashMap<Integer, Double>();
	 public static HashMap<String,Integer> mappy ;
	 public static HashMap<String,Integer> totalmappy ;
	 public static double maxdelta;
	 public static HashMap<Integer, HashMap<String , Double>> previnputtreemaps ;
	 
	 
	 
	 public static void main(String[] args) throws IOException{		
		 PrintWriter writer = new PrintWriter("checkfile.txt", "UTF-8");
		 
		 List<String> trees = Files.readAllLines(Paths.get("inputtrees.txt"), StandardCharsets.UTF_8);
		 inputtreemaps = new HashMap<Integer, HashMap<String , Double>>();
		 previnputtreemaps = new HashMap<Integer, HashMap<String , Double>>();
		 
		 List<String> ruleprob = Files.readAllLines(Paths.get("newrulefile.txt"), StandardCharsets.UTF_8);
		 mappy = new  HashMap<String,Integer>();
		 for (int i=0;i<ruleprob.size();i++){
			 String key = ruleprob.get(i).split("--->")[0].trim();
			 int value  = Integer.parseInt(ruleprob.get(i).split("--->")[1].trim());
			 mappy.put(key, value);
		 }
		 
		 writer.println(mappy.keySet());
		 writer.close();
		 totalmappy = new  HashMap<String,Integer>();
		 for (int i=0;i<ruleprob.size();i++){
			 String key = ruleprob.get(i).split("-->")[0].trim();			 
			 int value  = Integer.parseInt(ruleprob.get(i).split("-->")[2].trim());
			 if(!totalmappy.containsKey(key)){
				 totalmappy.put(key, value);
			 }else{
				 totalmappy.put(key, totalmappy.get(key)+value);
			 }
		 }
		 
		 
		 int index=-1;
		 for(int i=0;i<trees.size();i++){
			 if(trees.get(i).equals("0")){
				 index++;
				 HashMap<String , Double> neew = new HashMap<String , Double>();
				 HashMap<String , Double> neeew = new HashMap<String , Double>();
				 inputtreemaps.put(index,neew );
				 previnputtreemaps.put(index, neeew);
			 }else{
				 inputtreemaps.get(index).put(trees.get(i), (double)0);
				 previnputtreemaps.get(index).put(trees.get(i), (double)0);
			 }
		 }
		 //updating initial prob
		 HashMap<String , ArrayList<String>> inputmap = new HashMap<String , ArrayList<String>>();
		 for(int i=0;i<inputtreemaps.size();i++){
			 ArrayList<String> lines = new ArrayList<String>(inputtreemaps.get(i).keySet());			 
			 for(int j=0;j<lines.size();j++){
				 String key = lines.get(j).split("-->")[0].trim();
				 String value = lines.get(j).split("-->")[1].trim();
				 if(!inputmap.containsKey(key)){
					 ArrayList<String> array = new ArrayList<String>();
					 array.add(value);
					 
					 inputmap.put(key, array);
				 }else{
					 if(! inputmap.get(key).contains(value))
					 inputmap.get(key).add(value);
				 }
			 }			 	 
		 }
		 for(int i=0;i<inputtreemaps.size();i++){
			 ArrayList<String> lines = new ArrayList<String>(inputtreemaps.get(i).keySet());
			 for(int j=0;j<lines.size();j++){
				 String key = lines.get(j).split("-->")[0].trim();
				 double prob = (double)1/inputmap.get(key).size();
				 inputtreemaps.get(i).put(lines.get(j),prob);
			 }	
		 }
		 
		
		 assign_maxdiff();
		// System.out.println(maxdelta);
		 int ab=0;
		 while(maxdelta > 0.000000001 ){
			 update_with_EM();
			 ab++;
			 System.out.println(inputtreemaps);
		 }
		 System.out.println("no of iterations: "+ab);
		// System.out.println(inputtreemaps);

		 
		 
		 
	 }
	 
	 public static void assign_maxdiff(){
		 double max=0;
		 for(int i=0;i<inputtreemaps.size();i++){
			 ArrayList<String> lines = new ArrayList<String>(inputtreemaps.get(i).keySet());
			 max=0;
			 for(int j=0;j<lines.size();j++){
				 String key = lines.get(j).split("-->")[0].trim();
				 double tempmax=0;
				 double prob = inputtreemaps.get(i).get(lines.get(j));
				 double prevprob = previnputtreemaps.get(i).get(lines.get(j));
				 if(prob>prevprob){
					 tempmax = prob-prevprob;
				 }else{
					 tempmax = prevprob-prob;
				 }
				 if(max<tempmax){
					 max=tempmax;
				 }
				// System.out.println("tempmax   "+tempmax);
				 previnputtreemaps.get(i).put(lines.get(j), prob);
			 }			 
		 }	
		 
		maxdelta=max;
		
	 }
	 
	 
	 
	 
	 public static void update_with_EM() throws IOException{
		 for(int i=0;i<inputtreemaps.size();i++){
			 
			 ArrayList<Double> valuearray = new ArrayList<Double>(inputtreemaps.get(i).values());
			 double value = 1;
			 for(int j=0;j<valuearray.size();j++){
				 value = value * valuearray.get(j);
			 }
			 score.put(i, value);
		 }
		 int count =100;
		 
		 for(int i=0;i<inputtreemaps.size();i++){
			 ArrayList<String> lines = new ArrayList<String>(inputtreemaps.get(i).keySet());
			 for(int j=0;j<lines.size();j++){
				 String totalkey = lines.get(j);
				 String key = lines.get(j).split("-->")[0].trim();
				 double totalscore=0;
				 for(int k = 0;k<score.size();k++){
					 totalscore= totalscore+score.get(k);
				 }
				 double prob = (double)((score.get(i)*count/totalscore)+((double)getfromdatabase(totalkey)))/((double)((score.get(i)*count/totalscore))+(double)gettotalfromdatabase(key));
				 inputtreemaps.get(i).put(lines.get(j),prob);
			 }
		 }	 
		 assign_maxdiff();
	 }
	 
	 public static int getfromdatabase(String rule) throws IOException{
		 if(mappy.containsKey(rule))
			 return mappy.get(rule);
		 else {
			 //System.out.println("not exist");
			 return 0;
		 }
	 }
	 
	 public static int gettotalfromdatabase(String rule) throws IOException{
		 String nonterminal = rule.split("-->")[0].trim();
		 if(totalmappy.containsKey(nonterminal)){
			 return totalmappy.get(nonterminal);
		 }else{
			 return 0;
		 }
		 
	 }
	 
	 
	 
}