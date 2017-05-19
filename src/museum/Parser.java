package museum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    static public Museum parse(String file){
    	// read file
        BufferedReader in = null;
        ArrayList<String> map = new ArrayList<String>();
        String currentLine;
        try {
            in = new BufferedReader(new FileReader(file));
            while((currentLine = in.readLine()) != null) map.add(currentLine);
            in.close();
        }catch(IOException e){
            System.err.println(e);
        }
        
        // cast to Museum
        int length = map.size(), width = map.get(0).length();
        Museum museum = new Museum(length, width);
        for (int i=0; i<length; i++){
        	for (int j=0; j<width; j++){
        		museum.addObstacle(i, j, new Obstacle(i,j));
        	}
        }
        return museum;
    }

}
