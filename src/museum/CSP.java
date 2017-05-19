package museum;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.*;

public class CSP {
	static final char[] possibleObjects = {'O','E','S','W','N'};
	static final int ojectPos = 0, EPos = 1, SPos = 2, WPose = 3, NPos = 4;
	
	static void minimizeCameras(Museum museum){
		Model model = new Model("minimiser les caméras");
		BoolVar[] variables = new BoolVar[museum._length * museum._width * possibleObjects.length];
		
		// add variables
		CSP.addVariables(museum, model, variables);
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
