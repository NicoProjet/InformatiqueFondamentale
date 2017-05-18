package Chess;

//import choco.integer.IntVar;
//import choco.Problem;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.*;

public interface CSP {
	
	static final char[] pieces = {'T','F','C'};
	
	static void independance(int boardSize, int k1, int k2, int k3){
		Model model = new Model("indépendance");
		//Problem problem = new Problem();
		IntVar[] variables = new IntVar[boardSize*boardSize];
		
		// add variables
		CSP.addVariables(boardSize, model, variables);
		
		// add constraints
		// rooks
		
		// bishops
		
		// knights
		
		
		// print
		//Board board = new Board(boardSize);
	}
	
	static void domination(int n, int k1, int k2, int k3){
		Board board = new Board(n);
		// create choco solver stuff
	}
	
	
	static void addVariables(int boardSize, Model model, IntVar[] variables){
		String v = "";
		for (int i=0; i<boardSize; i++){
			for (int j=0; j<boardSize; j++){
				for (int k=0; k<3; k++){
					switch(k){
						case 0: v = "T";
							break;
						case 1: v = "F";
							break;
						case 2: v = "C";
							break;
					}
					variables[(i*boardSize+j)*boardSize*boardSize+k] = model.intVar("X_"+i+"_"+j+"_"+v, 0,1);
				}
			}
		}
	}
	
	static void addIndependanceRook(int boardSize, Model model, IntVar[] variables){
		for (int i = 0; i<boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				for (int k = 0; k<boardSize; k++){
					for (int l = 0; l<boardSize; l++){
						for (char piece : pieces){
							model.or(variables[])
						}
					}
				}
			}
		}
	}
	
	static void addConstraintPieceCounter(int boardSize, int number, char v){
		int counter = 0;
		for (int i=0; i<boardSize; i++){
			for (int j=0; j<boardSize; j++){
				//
			}
		}
	}
	
	static IntVar getX(IntVar[] variables, int boardSize, int i, int j, char v){
		int valueIndex = 0;
		for (int k = 1; k<pieces.length; k++){
			if (v == pieces[k]){
				valueIndex = k;
			}
		}
		return variables[(i*boardSize+j)*boardSize*boardSize+valueIndex];
	}

}
