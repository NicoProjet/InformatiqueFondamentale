package Chess;

//import choco.integer.IntVar;
//import choco.Problem;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.*;
import java.lang.Math;

public interface CSP {
	
	static final char[] pieces = {'T','F','C'};
	
	static void independance(int boardSize, int k1, int k2, int k3){
		Model model = new Model("indépendance");
		//Problem problem = new Problem();
		BoolVar[] variables = new BoolVar[boardSize*boardSize];
		
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
	
	
	static void addVariables(int boardSize, Model model, BoolVar[] variables){
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
					variables[(i*boardSize+j)*boardSize*boardSize+k] = model.boolVar("X_"+i+"_"+j+"_"+v);
				}
			}
		}
	}
	
	static void addIndependanceRook(int boardSize, Model model, BoolVar[] variables){
		for (int i = 0; i<boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				for (int l = 0; l<boardSize; l++){
					if (l != j){ // may have to remove
						for (int k = 0; k<boardSize; k++){
							if (k != i){ // may have to remove
								for (int v=0; v<pieces.length; v++){
									// X0 or ( X1 and X2)
									// used model.and to cast from boolvar to constraint type
									model.or(model.and(variables[(i*boardSize+j)*boardSize*boardSize].not()),
											model.and(variables[(i*boardSize+l)*boardSize*boardSize+v].not(),
													variables[(k*boardSize+j)*boardSize*boardSize+v].not()));
								}
							}
						}
					}
				}
			}
		}
	}
	
	static void addIndependanceBishop(int boardSize, Model model, BoolVar[] variables){
		for (int i = 0; i<boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				for (int k = 0; k<boardSize; k++){
					for (int l = 0; l<boardSize; l++){
						if(i != k && j != l && Math.abs(i-k) == Math.abs(j-l)){
							for (int v=0; v<pieces.length; v++){
								model.or(variables[(i*boardSize+j)*boardSize*boardSize+1].not(),
										variables[(k*boardSize+l)*boardSize*boardSize+v]);
							}
						}
					}
				}					
			}
		}
	}
	
	static void addIndependanceKnight(int boardSize, Model model, BoolVar[] variables){
		
	}
	
	
	// NO CAN DO
	static void addIndependance(int boardSize, Model model, BoolVar[] variables){
		for (int i = 0; i<boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				for (int k = 0; k<boardSize; k++){
					for (int l = 0; l<boardSize; l++){
						for (int v=0; v<pieces.length; v++){
							model.or(
									variables[(i*boardSize+j)*boardSize*boardSize].not(), // no piece
									variables[(i*boardSize+j)*boardSize*boardSize].not(), // no threat from rook
									variables[(i*boardSize+j)*boardSize*boardSize].not(), // no threat from bishop
									variables[(i*boardSize+j)*boardSize*boardSize].not() // no threat from knight
									);
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
