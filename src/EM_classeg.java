import java.util.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EM_classeg {
	 public static void main(String[] args) throws IOException{
		 
		 List<String> trees = Files.readAllLines(Paths.get("inputtrees.txt"), StandardCharsets.UTF_8);
		 HashMap<Integer, HashMap<String , Double>> inputtreemaps = new HashMap<Integer, HashMap<String , Double>>();
		 
		 int index=-1;
		 for(int i=0;i<trees.size();i++){
			 if(trees.get(i).equals("0")){
				 index++;
				 HashMap<String , Double> neew = new HashMap<String , Double>();
				 inputtreemaps.put(index,neew );
			 }else{
				 inputtreemaps.get(index).put(trees.get(i), (double)0);
				 
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
		 
		 System.out.println(inputtreemaps);
	 }
}