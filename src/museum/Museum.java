package museum;

public class Museum {
	public final int _length;
	public final int _width;
	private MuseumObject[][] _museum;
	private int _numberOfObjects;
	
	public Museum(int x, int y){
		_length = Math.max(x, y);
		_width = Math.min(x, y);
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
	
	public String toString(){
		String s = "";
		for (int i=0; i<_length; i++){
			for (int j=0; j<_width; j++){
				if(_museum[i][j] != null){
					s+=_museum[i][j]+" ";
				}
				else{
					s+="  ";
				}
			}
			s += "\n";
		}
		return s;
	}
	
	public MuseumObject getObject(int x, int y){
		return _museum[x][y];
	}
	
	public char getValue(int x, int y){
		return _museum[x][y].getValue();
	}

}
