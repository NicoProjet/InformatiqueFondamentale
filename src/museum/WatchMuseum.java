package museum;

import java.util.ArrayList;
import java.util.Arrays;

public class WatchMuseum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> map;
		if (args[0] != null){
			map = Parser.parse(args[0]);
		}
		else{
			map = Parser.parse("map.txt");
		}
		
	}

}
