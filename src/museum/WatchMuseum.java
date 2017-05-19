package museum;

public class WatchMuseum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Museum museum;
		if (args.length > 0){
			museum = Parser.parse(args[0]);
		}
		else{
			museum = Parser.parse("map.txt");
		}
		CSP.minimizeCameras(museum);
	}
}
