package museum;

public class WatchMuseum {

	public static void main(String[] args) {
		Museum museum;
		if (args.length > 0){
			museum = Parser.parse(args[0]);
		}
		else{
			museum = Parser.parse("map.txt");
		}
		System.out.println(museum);
		CSP.minimizeCameras(museum);
	}
}
