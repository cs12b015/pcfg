import java.util.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class parsepcfg {
	public static void main(String[] args) throws IOException{
		
		List<String> lines = Files.readAllLines(Paths.get("data/A1.E1-NEW.mrg"), StandardCharsets.UTF_8);
	}
}
