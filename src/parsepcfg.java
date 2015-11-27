import java.util.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class parsepcfg {
	public static void main(String[] args) throws IOException{
		PrintWriter writer = new PrintWriter("HWfull.txt", "UTF-8");
		BufferedReader br = new BufferedReader(new FileReader("data/A1.E1-NEW.mrg"));
		String line= null;
		int lb=0;
		int rb=0;
		String temp="";
		ArrayList<String> rulestemparray = new ArrayList<String>();
		ArrayList<String> rulesarray = new ArrayList<String>();
        while((line = br.readLine()) != null){
        	for(int i=0;i<line.length();i++){
        		if(line.charAt(i) == '('){
        			lb++;
        		}else if (line.charAt(i) == ')'){
        			rb++;
        		}
        	}
        	temp=temp+line;
        	if(rb == lb){
        		lb=0;rb=0;
        		rulestemparray.add(temp);
        		temp="";
        	}        
        }
        for(int i=0;i<rulestemparray.size();i++)
        {
        	if(rulestemparray.get(i).trim().equals("\n")){}
        	else if(rulestemparray.get(i).equals(" ")){}
        	else if(rulestemparray.get(i).contains("CODE")){}
        	else{
        		rulesarray.add(rulestemparray.get(i));
        	}
        }
        
        for(int i=0;i<rulesarray.size();i++)
        writer.println(rulesarray.get(i));
        writer.close();
    }
}
