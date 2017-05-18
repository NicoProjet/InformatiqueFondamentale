package Chess;

//import choco.integer.IntVar;
//import choco.Problem;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.*;
import java.lang.Math;

public interface CSP {
	static final int rookPos = 0, bishopPos = 1, knightPos = 2;
	static final char[] pieces = {'T','F','C'};
	
	static void independance(int boardSize, int k1, int k2, int k3){
		Model model = new Model("indépendance");
		//Problem problem = new Problem();
		BoolVar[] variables = new BoolVar[boardSize*boardSize*boardSize];
		
		// add variables
		CSP.addVariables(boardSize, model, variables);
		
		// add constraints
		// rooks
		CSP.addIndependanceRook(boardSize, model, variables);
		// bishops
		CSP.addIndependanceBishop(boardSize, model, variables);
		// knights
		CSP.addIndependanceKnight(boardSize, model, variables);
		CSP.addConstraintPiecesCounters(boardSize, k1, k2, k3, variables, model);
		
		// solve
		Solution solution = model.getSolver().findSolution();
		if(solution != null){
			System.out.println(solution.toString());
		}
		
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
					variables[(i*boardSize*boardSize)+j*boardSize+k] = model.boolVar("X_"+i+"_"+j+"_"+v);
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
									model.or(model.and(variables[(i*boardSize*boardSize)+j*boardSize+rookPos].not()),
											model.and(variables[(i*boardSize*boardSize)+l*boardSize+v].not(),
													variables[(k*boardSize*boardSize)+j*boardSize+v].not())).post();
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
								model.or(variables[(i*boardSize*boardSize)+j*boardSize+bishopPos].not(),
										variables[(k*boardSize*boardSize)+l*boardSize+v].not()).post();
							}
						}
					}
				}					
			}
		}
	}
	
	static void addIndependanceKnight(int boardSize, Model model, BoolVar[] variables){
		for (int i = 0; i<boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				int [] kvalues = {i-1,i-2,i+1,i+2};
				int [] lvalues = {j-1,j-2,j+1,j+2};
				for (int k : kvalues){
					if (0<k && k<boardSize){
						for (int l : lvalues){
							if (0<l && l<boardSize){
								if (Math.abs(i-k) + Math.abs(j-l) == 3){
									for (int v=0; v<pieces.length; v++){
										model.or(variables[(i*boardSize*boardSize)+j*boardSize+knightPos].not(),
												variables[(k*boardSize*boardSize)+l*boardSize+v].not()).post();
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	static void addConstraintPiecesCounters(int boardSize, int k1, int k2, int k3, BoolVar[] variables, Model model){
		BoolVar[] rooks = new BoolVar[boardSize*boardSize], bishops = new BoolVar[boardSize*boardSize], knights = new BoolVar[boardSize*boardSize];
		int counter = 0;
		for (int i=0; i<boardSize; i++){
			for (int j=0; j<boardSize; j++){
				rooks[i*boardSize+j] = variables[(i*boardSize*boardSize)+j*boardSize+rookPos];
				bishops[i*boardSize+j] = variables[(i*boardSize*boardSize)+j*boardSize+bishopPos];
				knights[i*boardSize+j] = variables[(i*boardSize*boardSize)+j*boardSize+knightPos];
			}
		}
		model.sum(rooks, "==", k1);
		model.sum(bishops, "==", k2);
		model.sum(knights, "==", k3);
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
