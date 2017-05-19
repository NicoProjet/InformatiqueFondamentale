package museum;

public class Camera extends MuseumObject {

	@Override
	public void addToMuseum(Museum museum) {
		museum.addObstacle(_x,_y,this);		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
