package museum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    static public ArrayList<String> parse(String file){
        System.out.println(file);
        BufferedReader in = null;
        ArrayList<String> map = new ArrayList<String>();
        String currentLine;
        try {
            in = new BufferedReader(new FileReader(file));
            while((currentLine = in.readLine()) != null) map.add(currentLine);
        }catch(IOException e){
            System.err.println(e);
        }
        return map;
    }

}
