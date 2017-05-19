package Chess;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.*;
import java.lang.Math;
import java.util.ArrayList;

public class CSP {
	static final int rookPos = 0, bishopPos = 1, knightPos = 2;
	static final char[] pieces = {'T','F','C'};
	
	static void independance(int boardSize, int k1, int k2, int k3){
		Model model = new Model("indépendance");
		BoolVar[] variables = new BoolVar[boardSize*boardSize*pieces.length];
		
		// add variables
		CSP.addVariables(boardSize, model, variables);
		
		// add constraints
		// can be optimized, we found the method Constraint.merge(..) after finishing the independance
		// we can create a constraint to check if position is empty then us the for loops to merge
		// with possible threatening pieces
		CSP.addIndependanceRook(boardSize, model, variables); // rooks
		CSP.addIndependanceBishop(boardSize, model, variables); // bishops
		CSP.addIndependanceKnight(boardSize, model, variables); // knights
		CSP.addConstraintPiecesCounters(boardSize, k1, k2, k3, variables, model); // right amount of each pieces
		CSP.addUnicityConstraint(boardSize, variables, model); // at most one piece at each position
		
		// solve
		model.getSolver().findSolution();

		// print
		Board board = CSP.toBoard(model, variables, boardSize);
		CSP.print(board);
	}
	
	static void domination(int boardSize, int k1, int k2, int k3){
		System.out.println("domination");
		Model model = new Model("domination");
		BoolVar[] variables = new BoolVar[boardSize*boardSize*pieces.length];
		
		// add variables
		CSP.addVariables(boardSize, model, variables);
		
		// add constraints
		CSP.addDomination(boardSize, model, variables);
		CSP.addUnicityConstraint(boardSize, variables, model);
		CSP.addConstraintPiecesCounters(boardSize, k1, k2, k3, variables, model);
		
		// solve
		model.getSolver().findSolution();
		
		//print
		Board board = CSP.toBoard(model, variables, boardSize);
		CSP.print(board);
	}
	
	static void minimizeKnights(int boardSize){
		System.out.println("minimisation du nombre de cavalier pour obtenir une domination");
		Model model = new Model("minKnights");
		BoolVar[] variables = new BoolVar[boardSize*boardSize];
		
		// add variables
		for (int i=0; i<boardSize; i++){
			for (int j=0; j<boardSize; j++){
				variables[(i*boardSize)+j] = model.boolVar("X_"+i+"_"+j);
			}
		}
		IntVar tot_knights = model.intVar("tot_knights", 0, boardSize*boardSize);
		
		// add constraints
		CSP.addDominationOnlyByKnights(boardSize, model, variables);
		model.sum(variables, "=", tot_knights).post();
		
		// solve
		//model.getSolver().findSolution();
		Solution sol = model.getSolver().findOptimalSolution(tot_knights, Model.MINIMIZE);
		
		Board board = CSP.toBoardKnightsOnly(sol, variables, boardSize);
		CSP.print(board);
	}
	
	static void addVariables(int boardSize, Model model, BoolVar[] variables){
		String v = "";
		for (int i=0; i<boardSize; i++){
			for (int j=0; j<boardSize; j++){
				for (int k=0; k<pieces.length; k++){
					switch(k){
						case 0: v = "T";
							break;
						case 1: v = "F";
							break;
						case 2: v = "C";
							break;
					}
					variables[(i*boardSize*pieces.length)+j*pieces.length+k] = model.boolVar("X_"+i+"_"+j+"_"+v);
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
									// there is repetition
									model.or(model.and(variables[(i*boardSize*pieces.length)+j*pieces.length+rookPos].not()),
											model.and(variables[(i*boardSize*pieces.length)+l*pieces.length+v].not(),
													variables[(k*boardSize*pieces.length)+j*pieces.length+v].not())).post();
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
								model.or(variables[(i*boardSize*pieces.length)+j*pieces.length+bishopPos].not(),
										variables[(k*boardSize*pieces.length)+l*pieces.length+v].not()).post();
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
					if (0<=k && k<boardSize){
						for (int l : lvalues){
							if (0<=l && l<boardSize){
								if (Math.abs(i-k) + Math.abs(j-l) == 3){
									for (int v=0; v<pieces.length; v++){
										model.or(variables[(i*boardSize*pieces.length)+j*pieces.length+knightPos].not(),
												variables[(k*boardSize*pieces.length)+l*pieces.length+v].not()).post();
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	static void addDomination(int boardSize, Model model, BoolVar[] variables){
		// train of thoughts: using model.and() to cast to Constraint type
		// ( not var0 )
		// ( not var0 and not var1)
		// Which equals  ( var0 or var1)
		for (int i = 0; i<boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				ArrayList<BoolVar> terms = new ArrayList<BoolVar>();
				terms.add(variables[i*boardSize*pieces.length+j*pieces.length+rookPos]);
				terms.add(variables[i*boardSize*pieces.length+j*pieces.length+bishopPos]);
				terms.add(variables[i*boardSize*pieces.length+j*pieces.length+knightPos]);
								
				// threatened by rooks
				// same line
				for (int k = 0; k<boardSize; k++){
					if(k != i){
						terms.add(variables[(k*boardSize*pieces.length)+j*pieces.length+rookPos]);
					}
				}
				// same column
				for (int k = 0; k<boardSize; k++){
					if(k != j){
						terms.add(variables[(i*boardSize*pieces.length)+k*pieces.length+rookPos]);
					}
				}
				
				
				// threatened by bishops
				for (int k = 0; k<boardSize; k++){
					for (int l = 0; l<boardSize; l++){
						if(i != k && j != l && Math.abs(i-k) == Math.abs(j-l)){
							terms.add(variables[(k*boardSize*pieces.length)+l*pieces.length+bishopPos]);
						}
					}
				}
				
				
				// threatened by knights
				int [] kvalues = {i-1,i-2,i+1,i+2};
				int [] lvalues = {j-1,j-2,j+1,j+2};
				for (int k : kvalues){
					if (0<=k && k<boardSize){
						for (int l : lvalues){
							if (0<=l && l<boardSize){
								if (Math.abs(i-k) + Math.abs(j-l) == 3){
									terms.add(variables[(k*boardSize*pieces.length)+l*pieces.length+knightPos]);
								}
							}
						}
					}
				}
				
				// cast ArrayList<BoolVar> to BoolVar[]
				BoolVar[] termsArray = new BoolVar[terms.size()];
				for (int index = 0; index < terms.size(); index++){
					termsArray[index] = terms.get(index).not();
				}
				
				// post constraint
				model.and(termsArray).getOpposite().post();
			}
		}
	}
	
	static void addDominationOnlyByKnights(int boardSize, Model model, BoolVar[] variables){
		for (int i = 0; i<boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				ArrayList<BoolVar> terms = new ArrayList<BoolVar>();
				terms.add(variables[i*boardSize+j]);
		
				// threatened by knights
				int [] kvalues = {i-1,i-2,i+1,i+2};
				int [] lvalues = {j-1,j-2,j+1,j+2};
				for (int k : kvalues){
					if (0<=k && k<boardSize){
						for (int l : lvalues){
							if (0<=l && l<boardSize){
								if (Math.abs(i-k) + Math.abs(j-l) == 3){
									terms.add(variables[(k*boardSize)+l]);
								}
							}
						}
					}
				}
				
				// cast ArrayList<BoolVar> to BoolVar[]
				BoolVar[] termsArray = new BoolVar[terms.size()];
				for (int index = 0; index < terms.size(); index++){
					termsArray[index] = terms.get(index).not();
				}
				
				// post constraint
				model.and(termsArray).getOpposite().post();
			}
		}
	}
	
	static void addConstraintPiecesCounters(int boardSize, int k1, int k2, int k3, BoolVar[] variables, Model model){
		BoolVar[] rooks = new BoolVar[boardSize*boardSize], bishops = new BoolVar[boardSize*boardSize], knights = new BoolVar[boardSize*boardSize];
		for (int i=0; i<boardSize; i++){
			for (int j=0; j<boardSize; j++){
				rooks[i*boardSize+j] = variables[(i*boardSize*pieces.length)+j*pieces.length+rookPos];
				bishops[i*boardSize+j] = variables[(i*boardSize*pieces.length)+j*pieces.length+bishopPos];
				knights[i*boardSize+j] = variables[(i*boardSize*pieces.length)+j*pieces.length+knightPos];
			}
		}
		model.sum(rooks, "=", k1).post();
		model.sum(bishops, "=", k2).post();
		model.sum(knights, "=", k3).post();
	}
	
	static void addUnicityConstraint(int boardSize, BoolVar[] variables, Model model){
		BoolVar[] pos;
		for (int i=0; i<boardSize; i++){
			for (int j=0; j<boardSize; j++){
				pos = new BoolVar[pieces.length];
				for (int v=0; v<pieces.length; v++){
					pos[v] = variables[(i*boardSize*pieces.length)+j*pieces.length+v];
				}
				model.sum(pos, "<=", 1).post();
			}
		}
	}
	
	static Board toBoard(Model model, IntVar[] variables, int boardSize){
		Board board = new Board(boardSize);
		for (int i = 0; i < boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				for (int v=0; v<pieces.length; v++){
					if (variables[i*boardSize*pieces.length+j*pieces.length+v].getValue() == 1){
						switch(v){
							case 0:
								board.addPiece(i,j,new Rook(i,j));
								break;
							case 1:
								board.addPiece(i,j,new Bishop(i,j));
								break;
							case 2:
								board.addPiece(i,j,new Knight(i,j));
								break;
						}
					}
				}
			}
		}
		return board;
	}
	
	static Board toBoardKnightsOnly(Solution s, IntVar[] variables, int boardSize){
		Board board = new Board(boardSize);
		for (int i = 0; i < boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				if (s.getIntVal(variables[i*boardSize+j]) == 1){
					board.addPiece(i,j,new Knight(i,j));
				}
			}
		}
		return board;
	}
	
	static IntVar getX(IntVar[] variables, int boardSize, int i, int j, char v){
		int valueIndex = 0;
		for (int k = 1; k<pieces.length; k++){
			if (v == pieces[k]){
				valueIndex = k;
			}
		}
		return variables[(i*boardSize*pieces.length)+j*pieces.length+valueIndex];
	}
	
	static void print(Board board){
		if (!board.isEmpty()){
			System.out.println(board);
		}
		else{
			System.out.println("pas de solution");
		}
	}

}
