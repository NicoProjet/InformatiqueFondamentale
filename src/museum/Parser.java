package museum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    static public ArrayList<String> parse(String file) throws IOException {
        BufferedReader in = null;
        ArrayList<String> map = new ArrayList<String>();
        String currentLine;
        try {
            in = new BufferedReader(new FileReader(file));
            while((currentLine = in.readLine()) != null) map.add(currentLine);
        }finally {
            if(in != null) in.close();

        }
        return map;
    }

}
