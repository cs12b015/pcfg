import java.util.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class parsepcfg {

    public static HashMap<String, ArrayList<ArrayList<String>>> list = new HashMap<String, ArrayList<ArrayList<String>>>();
    public static HashMap<String, ArrayList<Integer>> list_frequency = new HashMap<String, ArrayList<Integer>>();

    public static void main(String[] args) throws IOException{

        PrintWriter writer = new PrintWriter("HWfull.txt", "UTF-8");
        ArrayList<String> rulestemparray = new ArrayList<String>();
        ArrayList<String> rulesarray = new ArrayList<String>();
        List<String> lines = Files.readAllLines(Paths.get("../filenamess.txt"), StandardCharsets.UTF_8);

        for(int in=0;in<lines.size();in++){
            BufferedReader br = new BufferedReader(new FileReader("../data/"+lines.get(in)));
            String line= null;
            int lb=0;
            int rb=0;
            String temp="";

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

        }

        for(int i=0;i<rulestemparray.size();i++)
        {
            if(rulestemparray.get(i).trim().equals("\n")){}
            else if(rulestemparray.get(i).equals("")){}
            else if(rulestemparray.get(i).contains("CODE")){}
            else{
                rulesarray.add(rulestemparray.get(i));
                writer.println(rulestemparray.get(i));
            }
        }


        writer.close();

        for (int i = 0; i < rulesarray.size(); i++){
            //for each tree in the array, do the following
            String str = rulesarray.get(i);
            str = str.substring(1,str.length()-1);
            str = str.trim();
            ArrayList<String> keys = new ArrayList<String>();
            ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();

            parser(keys, values, str);
        }
        System.out.println(list);
        System.out.println(list_frequency);

        /*
           String str = rulesarray.get(0);
           str = str.substring(1,str.length()-1);
           str = str.trim();
           ArrayList<String> keys = new ArrayList<String>();
           ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();

           parser(keys, values, str);
           System.out.println(list);
           */
    }

    public static void parser(ArrayList<String> keys, ArrayList<ArrayList<String>> values, String str){

        if (str == ""){
            System.out.println("Finished parsing tree");
        } else {

            //System.out.println("Called function parser");
            // System.out.println("Keys are: ");
            // System.out.println(keys);
            // System.out.println("Values are: ");
            // System.out.println(values);

            if(str.charAt(0) == '('){
                String temp = "";
                int j = 1;
                while (str.charAt(j) != ' '){
                    temp += str.charAt(j);
                    j = j+1;
                }
                if(!keys.isEmpty()){
                    if (values.size() < keys.size()){
                        ArrayList<String> temp_list = new ArrayList<String>();
                        temp_list.add(temp);
                        values.add(temp_list);
                    } else{
                        ArrayList<String> temp_list = values.get(values.size()-1);
                        temp_list.add(temp);
                        values.remove(values.size()-1);
                        values.add(temp_list);
                    }
                }
                keys.add(temp);

                while(str.charAt(j) == ' '){
                    j++;
                }
                str = str.substring(j);
                //System.out.println(str);
                parser(keys,values,str.trim());
            }
            else{
                String temp = "";
                int j = 0;
                int flag = 0;
                while (str.charAt(j) != ' '){
                    if(str.charAt(j) == ')'){
                        flag = 1;
                        break;
                    }
                    temp += str.charAt(j);
                    j = j+1;
                }
                if(flag == 0){
                    if (values.size() < keys.size()){
                        ArrayList<String> temp_list = new ArrayList<String>();
                        temp_list.add(temp);
                        values.add(temp_list);
                    } else{
                        if (values.size() != 0){
                            ArrayList<String> temp_list = values.get(values.size()-1);
                            temp_list.add(temp);
                            values.add(temp_list);
                        }
                    }
                    while(str.charAt(j) == ' '){
                        j++;
                    }
                    str = str.substring(j);
                    parser(keys,values,str.trim());
                }

                if(flag == 1){
                    if (values.size() < keys.size()){
                        ArrayList<String> temp_list = new ArrayList<String>();
                        temp_list.add(temp);
                        values.add(temp_list);
                    } else{
                        if (values.size() != 0){
                            ArrayList<String> temp_list = values.get(values.size()-1);
                            temp_list.add(temp);
                            values.add(temp_list);
                        }
                    }

                    /*
                       System.out.println("Keys are: ");
                       System.out.println(keys);
                       System.out.println("Values are: ");
                       System.out.println(values);
                       */

                    while(str.charAt(j) == ')'){  

                        if(keys.size()==0){	
                            j++;
                            break;
                        }
                        if (list.containsKey(keys.get(keys.size()-1))){
                            ArrayList<ArrayList<String>> temp_list1 = list.get(keys.get(keys.size()-1));
                            ArrayList<Integer> temp_list1_freq = list_frequency.get(keys.get(keys.size()-1));
                            ArrayList<String> temp_list2 = values.get(keys.size()-1);
                            if (temp_list1.contains(temp_list2)){
                                //Increase counter
                                int index = temp_list1.indexOf(temp_list2);
                                int prev = temp_list1_freq.get(index);
                                temp_list1_freq.set(index, prev+1);
                            }else {
                                temp_list1.add(temp_list2);
                                temp_list1_freq.add(1);
                            }
                            list.put(keys.get(keys.size()-1), temp_list1);
                            list_frequency.put(keys.get(keys.size()-1), temp_list1_freq);
                        } else{
                            ArrayList<ArrayList<String>> temp_list1 = new ArrayList<ArrayList<String>>();
                            ArrayList<Integer> temp_list1_freq = new ArrayList<Integer>();
                            temp_list1_freq.add(1);
                            ArrayList<String> temp_list2 = values.get(keys.size()-1);
                            temp_list1.add(temp_list2);
                            list.put(keys.get(keys.size()-1), temp_list1);
                            list_frequency.put(keys.get(keys.size()-1), temp_list1_freq);
                        }
                        //System.out.println("Key-value pair removed are: " + keys.get(keys.size()-1) + " - " + values.get(keys.size()-1));
                        keys.remove(keys.size()-1);
                        values.remove(values.size()-1);
                        j++;

                        if (j > str.length()-1) break;
                    }

                    if (j <= str.length()-1){
                        while(str.charAt(j) == ' '){
                            j++;
                        }
                        str = str.substring(j);
                        //System.out.println(str.trim());
                        parser(keys,values,str.trim());
                    }
                }
            }
        }

    }
}
