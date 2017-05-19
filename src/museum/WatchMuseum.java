package museum;

import java.util.ArrayList;

public class WatchMuseum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> map;
		if (args.length > 0){
			map = Parser.parse(args[0]);
		}
		else{
			map = Parser.parse("map.txt");
		}

		for(String elem: map) System.out.println(elem);
	}
}
