package museum;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.*;

public class CSP {
	static final char[] possibleObjects = {'O','E','S','W','N'};
	static final int EAST_INDEX = 1, WEST_INDEX = 3, NORTH_INDEX = 4, SOUTH_INDEX = 2;
	static final int ojectPos = 0, EPos = 1, SPos = 2, WPos = 3, NPos = 4;
	
	static void minimizeCameras(Museum museum){
		Model model = new Model("minimiser les caméras");
		BoolVar[] variables = new BoolVar[museum._length * museum._width * possibleObjects.length];
		
		// add variables
		CSP.addVariables(museum, model, variables);
		IntVar tot_cameras = model.intVar("tot_cameras",0,museum._length*museum._width);
		
		// add constraints
		System.out.println("add domination");
		CSP.addDominationCameras(museum, model, variables); // all positions have a camera or are watched by a camera		
		System.out.println("add unicity");
		CSP.addUnicityConstraint(museum, model, variables); // only one item per position
		System.out.println("add walls");
		CSP.addWalls(museum, model, variables);
		System.out.println("add somme");
		model.sum(variables, "=", tot_cameras).post(); // tot_cameras = sum(all cameras)
		
		// solve
		System.out.println("solve");
		//Solution sol = model.getSolver().findSolution();
		Solution sol = model.getSolver().findOptimalSolution(tot_cameras, Model.MINIMIZE);
		
		System.out.println("cast");
		museum  = CSP.toMuseum(sol, variables, museum);
		System.out.println("print");
		CSP.print(museum);
	}

	private static void addDominationCameras(Museum museum, Model model, BoolVar[] variables) {
		int D1 = museum._width*possibleObjects.length; // simulated first dimension
		int D2 = possibleObjects.length; // simulated second dimension
		for (int i=0; i<museum._length; i++){
			for (int j=0; j<museum._width; j++){
				ArrayList<Constraint> constraints_OR = new ArrayList<Constraint>();
				// check if object at (i,j)
				for (int v=0; v<possibleObjects.length; v++){
					constraints_OR.add(model.and(variables[i*D1 + j*D2 + v]));
				}
				// check if seen by another camera index i
				int orientation = 0;
				for(int k=0; k<museum._length; k++){
					if (k != i){
						if (k < i){
							ArrayList<BoolVar> terms = new ArrayList<BoolVar>();
							orientation = SOUTH_INDEX;
							terms.add(variables[k*D1 + j*D2 + orientation]); // check if camera right direction = CD
							for (int m=k+1; m<i; m++){ // check if no walls between them = WB
								for (int v = 0; v<possibleObjects.length; v++){
									terms.add(variables[m*D1 + j*D2 + v].not());
								}
							}
							BoolVar[] termsArray = CSP.toArray(terms);
							constraints_OR.add(model.and(termsArray)); // Adds ( CD AND WB ) to constraints_OR_0
						}
						else{
							ArrayList<BoolVar> terms = new ArrayList<BoolVar>();
							orientation = NORTH_INDEX;
							terms.add(variables[k*D1 + j*D2 + orientation]);
							for (int n=k-1; n>i; n--){
								for (int v = 0; v<possibleObjects.length; v++){
									terms.add(variables[n*D1 + j*D2 + v].not());
								}
							}
							BoolVar[] termsArray = CSP.toArray(terms);
							constraints_OR.add(model.and(termsArray));
						}
					}
				}
				// check if seen by another camera index j
				for(int k=0; k<museum._width; k++){
					if (k != j){
						if (k < j){
							ArrayList<BoolVar> terms = new ArrayList<BoolVar>();
							orientation = EAST_INDEX;
							terms.add(variables[i*D1 + k*D2].not());
							for (int m=k+1; m<j; m++){
								for (int v = 0; v<possibleObjects.length; v++){
									terms.add(variables[i*D1 + m*D2 + v].not());
								}
							}
							BoolVar[] termsArray = CSP.toArray(terms);
							constraints_OR.add(model.and(termsArray));
						}
						else{
							ArrayList<BoolVar> terms = new ArrayList<BoolVar>();
							orientation = WEST_INDEX;
							terms.add(variables[i*D1 + k*D2 + orientation]);
							for (int n=k-1; n>j; n--){
								for (int v = 0; v<possibleObjects.length; v++){
									terms.add(variables[i*D1 + n*D2 + v].not());
								}
							}
							BoolVar[] termsArray = CSP.toArray(terms);
							constraints_OR.add(model.and(termsArray));
						}
					}
				}
				/*
				 *  MERGE
				 */
				Constraint[] cs = new Constraint[constraints_OR.size()];
				for (int index = 0; index <constraints_OR.size(); index ++){
					cs[index] = constraints_OR.get(index).getOpposite();
				}
				Constraint c = Constraint.merge("", cs);
				c = c.getOpposite();
				c.post();
			}
		}
	}
	
	public static void addNotWallsBetween(int x, int x_bound, int y, int y_bound, Museum museum, ArrayList<BoolVar> list, BoolVar[] variables){
		int D1 = museum._width*possibleObjects.length; // simulated first dimension
		int D2 = possibleObjects.length; // simulated second dimension
		if (x > x_bound){
			int tmp = x;
			x = x_bound;
			x_bound = tmp;
		}
		if (y > y_bound){
			int tmp = y;
			y = y_bound;
			y_bound = tmp;
		}
		for (int i = x; i<x_bound; i++){
			for (int j = y; j<y_bound; j++){
				list.add(variables[i*D1 + j*D2].not());
			}
		}
	}
	
	private static void addWalls(Museum museum, Model model, BoolVar[] variables){
		int D1 = museum._width*possibleObjects.length; // simulated first dimension
		int D2 = possibleObjects.length; // simulated second dimension
		for (int i = 0; i<museum._length; i++){
			for (int j = 0; j<museum._width; j++){
				if (museum.getObject(i, j) != null && museum.getValue(i, j) == '*'){
					model.and(variables[i*D1 + j * D2 + ojectPos]).post();
				}
				else{
					model.and(variables[i*D1 + j * D2 + ojectPos].not()).post();
				}
			}
		}
	}

	private static void addVariables(Museum museum, Model model, BoolVar[] variables) {
		int D1 = museum._width*possibleObjects.length; // simulated first dimension
		int D2 = possibleObjects.length; // simulated second dimension
		for (int i=0; i<museum._length; i++){
			for (int j=0; j<museum._width; j++){
				for (int v=0; v<possibleObjects.length; v++){
					variables[i*D1 + j*D2 + v] = model.boolVar("X_"+i+"_"+j+"_"+possibleObjects[v]);
				}
			}
		}
	}
	
	private static void addUnicityConstraint(Museum museum, Model model, BoolVar[] variables){
		int D1 = museum._width*possibleObjects.length; // simulated first dimension
		int D2 = possibleObjects.length; // simulated second dimension
		BoolVar[] pos;
		for (int i=0; i<museum._length; i++){
			for (int j=0; j<museum._width; j++){
				pos = new BoolVar[possibleObjects.length];
				for (int v=0; v<possibleObjects.length; v++){
					pos[v] = variables[(i*D1) + j*D2 + v];
				}
				model.sum(pos, "<=", 1).post();
			}
		}
	}
	
	private static BoolVar[] toArray(ArrayList<BoolVar> arrayList){
		BoolVar[] termsArray = new BoolVar[arrayList.size()];
		for (int index = 0; index < arrayList.size(); index++){
			termsArray[index] = arrayList.get(index);
		}
		return termsArray;
	}
	
	private static Museum toMuseum(Solution s, IntVar[] variables, Museum museum){
		int D1 = museum._width*possibleObjects.length; // simulated first dimension
		int D2 = possibleObjects.length; // simulated second dimension
		Museum m = new Museum(museum._length, museum._width);
		for (int i = 0; i<museum._length; i++){
			for (int j = 0; j<museum._width; j++){
				for (int v = 0; v<possibleObjects.length; v++){
					if (s.getIntVal(variables[i*D1 + j*D2 + v]) == 1){
						if (v == 0){
							m.addObstacle(i, j, new Obstacle(i,j));
						}
						else{
							m.addObstacle(i, j, new Camera(i,j,v-1));
						}
					}
				}
			}
		}
		return m;
	}
	
	static void print(Museum museum){
		if (!museum.isEmpty()){
			System.out.println(museum);
		}
		else{
			System.out.println("pas de solution");
		}
	}

}
