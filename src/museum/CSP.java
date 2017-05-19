package museum;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.*;

public class CSP {
	static final char[] possibleObjects = {'O','E','S','W','N'};
	static final char EST = 'E', WEST = 'W', NORTH = 'N', SOUTH = 'S';
	// static final int EST_INDEX = 1, WEST_INDEX = 3, NORTH_INDEX = 4, SOUTH_INDEX = 2;
	static final char[][] orientations = {{'N','S'},{'W','E'}}; 
	static final int ojectPos = 0, EPos = 1, SPos = 2, WPose = 3, NPos = 4;
	
	static void minimizeCameras(Museum museum){
		Model model = new Model("minimiser les caméras");
		BoolVar[] variables = new BoolVar[museum._length * museum._width * possibleObjects.length];
		
		// add variables
		CSP.addVariables(museum, model, variables);
		IntVar tot_cameras = model.intVar("tot_cameras",0,museum._length*museum._width);
		
		// add constraints
		CSP.addDominationCameras(museum, model, variables);
		// unicity
		// somme
	}

	private static void addDominationCameras(Museum museum, Model model, BoolVar[] variables) {
		int D1 = museum._length*possibleObjects.length; // simulated first dimension
		int D2 = possibleObjects.length; // simulated second dimension
		for (int i=0; i<museum._length; i++){
			for (int j=0; j<museum._width; j++){
				ArrayList<BoolVar> terms = new ArrayList<BoolVar>();
				// check if camera at (i,j)
				for (int v=0; v<possibleObjects.length; v++){
					terms.add(variables[i*D1 + j*D2 + v]);
				}
				
				// check if seen by another camera
				for(int k=0; k<museum._length; k++){
					if (k != i){
						for (int l=0; l<museum._width; l++){
							if (l != j){
								// changer le modèle avant
							}
						}
					}
				}
			}
		}
	}

	private static void addVariables(Museum museum, Model model, BoolVar[] variables) {
		int D1 = museum._length*possibleObjects.length; // simulated first dimension
		int D2 = possibleObjects.length; // simulated second dimension
		for (int i=0; i<museum._length; i++){
			for (int j=0; j<museum._width; j++){
				for (int v=0; v<possibleObjects.length; v++){
					variables[i*D1 + j*D2 + v] = model.boolVar("X_"+i+"_"+j+"_"+possibleObjects[v]);
				}
			}
		}
	}

}
