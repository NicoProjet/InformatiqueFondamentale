package museum;

public class Museum {
	private int _length;
	private int _width;
	private MuseumObject[][] _museum;
	private int _numberOfObjects;
	
	public Museum(int length, int width){
		_length = length;
		_width = width;
		_museum = new MuseumObject[_length][_width];
		_numberOfObjects = 0;
	}

	public void addObstacle(int x, int y, MuseumObject object) {
		_museum[x][y] = object;
		_numberOfObjects++;		
	}
	
	public int getNumberOfObjects(){
		return _numberOfObjects;
	}
	
	public boolean isEmpty(){
		return _numberOfObjects == 0;
	}
	
	public String toStrin(){
		String s = "";
		for (int i=0; i<_length; i++){
			for (int j=0; j<_width; j++){
				if(_museum[i][j] != null){
					s+=_museum[i][j]+" ";
				}
				else{
					s+=" ";
				}
			}
			s += "\n";
		}
		return s;
	}

}
