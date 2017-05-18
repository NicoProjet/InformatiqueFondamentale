package Chess;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.variables.*;
import java.lang.Math;
import java.util.ArrayList;

public class CSP {
	static final int rookPos = 0, bishopPos = 1, knightPos = 2;
	static final char[] pieces = {'T','F','C'};
	
	static void independance(int boardSize, int k1, int k2, int k3){
		Model model = new Model("indépendance");
		BoolVar[] variables = new BoolVar[boardSize*boardSize*boardSize];
		
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
		if (!board.isEmpty()){
			System.out.println(board);
		}
		else{
			System.out.println("pas de solution");
		}
	}
	
	static void domination(int boardSize, int k1, int k2, int k3){
		System.out.println("domination");
		Model model = new Model("domination");
		BoolVar[] variables = new BoolVar[boardSize*boardSize*boardSize];
		
		// add variables
		CSP.addVariables(boardSize, model, variables);		
		
		// add constraints
		CSP.addDomination(boardSize, model, variables);
		CSP.addUnicityConstraint(boardSize, variables, model);
		CSP.addConstraintPiecesCounters(boardSize, k1, k2, k3, variables, model);
		
		// solve
		model.getSolver().findSolution();
		
		Board board = CSP.toBoard(model, variables, boardSize);
		if (!board.isEmpty()){
			System.out.println(board);
		}
		else{
			System.out.println("pas de solution");
		}
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
									// there is repetition
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
					if (0<=k && k<boardSize){
						for (int l : lvalues){
							if (0<=l && l<boardSize){
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
	
	static void addDomination(int boardSize, Model model, BoolVar[] variables){
		// train of thoughts: using model.and() to cast to Constraint type
		//Constraint c = model.and(model.boolVar()); // always true
		//c = Constraint.merge("", model.and(variables[0].not())); //  true and ( not var0 )
		//c = Constraint.merge("", model.and(variables[1].not())); //  true and ( not var0 and not var1)
		//c.getOpposite().post(); // post ( not true and ( not var0 and not var1 ) )  | Which equals  ( var0 or var1)
		for (int i = 0; i<boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				ArrayList<BoolVar> terms = new ArrayList<BoolVar>();
				terms.add(variables[i*boardSize*boardSize+j*boardSize+rookPos]);
				terms.add(variables[i*boardSize*boardSize+j*boardSize+bishopPos]);
				terms.add(variables[i*boardSize*boardSize+j*boardSize+knightPos]);
								
				// threatened by rooks
				for (int k = 0; k<boardSize; k++){
					if(k != i){
						terms.add(variables[(k*boardSize*boardSize)+j*boardSize+rookPos]);
						terms.add(variables[(i*boardSize*boardSize)+k*boardSize+rookPos]);
					}
				}
				
				
				// threatened by bishops
				for (int k = 0; k<boardSize; k++){
					for (int l = 0; l<boardSize; l++){
						if(i != k && j != l && Math.abs(i-k) == Math.abs(j-l)){
							terms.add(variables[(i*boardSize*boardSize)+j*boardSize+bishopPos]);
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
									terms.add(variables[(i*boardSize*boardSize)+j*boardSize+knightPos]);
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
	
	/*
	static void addDomination(int boardSize, Model model, BoolVar[] variables){
		// train of thoughts: using model.and() to cast to Constraint type
		//Constraint c = model.and(model.boolVar()); // always true
		//c = Constraint.merge("", model.and(variables[0].not())); //  true and ( not var0 )
		//c = Constraint.merge("", model.and(variables[1].not())); //  true and ( not var0 and not var1)
		//c.getOpposite().post(); // post ( not true and ( not var0 and not var1 ) )  | Which equals  ( var0 or var1)
		for (int i = 0; i<boardSize; i++){
			for (int j = 0; j<boardSize; j++){
				System.out.println("\n\n ==> pos = ("+i+","+j+")");
				// System.out.println("\n->  i: "+i+" | j: "+j);
				//Constraint c = model.and(model.boolVar()); // always true
				// checks if there is a piece (i found it clearer that way than adding a for (i++<3)
				// but adding more different pieces might change that
				System.out.println("OU T en ("+i+","+j+")");
				//c = Constraint.merge("", model.and(variables[i*boardSize*boardSize+j*boardSize+rookPos].not()),c);
				Constraint c = model.and(variables[i*boardSize*boardSize+j*boardSize+rookPos].not());
				System.out.println("OU F en ("+i+","+j+")");
				c = Constraint.merge("", model.and(variables[i*boardSize*boardSize+j*boardSize+bishopPos].not()),c.getOpposite());
				System.out.println("OU C en ("+i+","+j+")");
				c = Constraint.merge("", model.and(variables[i*boardSize*boardSize+j*boardSize+knightPos].not()),c.getOpposite());
				
				// c = Constraint.merge("", model.and(model.boolVar().not()),c.getOpposite()); // test ajout ( or true )
				
				// threatened by rooks
				System.out.println("threatened by rooks?:");
				for (int k = 0; k<boardSize; k++){
					if(k != i){
						System.out.println("OU T en ("+i+","+k+") et ("+k+","+j+")");
						// System.out.println("k: "+k); // correct rooks positions
						c = Constraint.merge("", model.and(variables[(k*boardSize*boardSize)+j*boardSize+rookPos].not()),c.getOpposite());
						c = Constraint.merge("", model.and(variables[(i*boardSize*boardSize)+k*boardSize+rookPos].not()),c.getOpposite());
					}
				}
				
				
				// threatened by bishops
				System.out.println("threatened by bishops?:");
				for (int k = 0; k<boardSize; k++){
					for (int l = 0; l<boardSize; l++){
						if(i != k && j != l && Math.abs(i-k) == Math.abs(j-l)){
							// System.out.println("k: "+k+" | l: "+l); // correct bishops positions
							System.out.println("OU F en ("+k+","+l+")");
							c = Constraint.merge("", model.and(variables[(i*boardSize*boardSize)+j*boardSize+bishopPos].not()),c.getOpposite());
						}
					}
				}
				
				
				// threatened by knights
				System.out.println("threatened by knights?:");
				int [] kvalues = {i-1,i-2,i+1,i+2};
				int [] lvalues = {j-1,j-2,j+1,j+2};
				for (int k : kvalues){
					if (0<=k && k<boardSize){
						for (int l : lvalues){
							if (0<=l && l<boardSize){
								if (Math.abs(i-k) + Math.abs(j-l) == 3){
									// System.out.println("k: "+k+" | l: "+l); // correct knights positions
									System.out.println("OU C en ("+k+","+l+")");
									c = Constraint.merge("", model.and(variables[(i*boardSize*boardSize)+j*boardSize+knightPos].not()),c.getOpposite());
								}
							}
						}
					}
				}
				c.getOpposite().post();
			}
		}
	}*/
	
	static void addConstraintPiecesCounters(int boardSize, int k1, int k2, int k3, BoolVar[] variables, Model model){
		BoolVar[] rooks = new BoolVar[boardSize*boardSize], bishops = new BoolVar[boardSize*boardSize], knights = new BoolVar[boardSize*boardSize];
		for (int i=0; i<boardSize; i++){
			for (int j=0; j<boardSize; j++){
				rooks[i*boardSize+j] = variables[(i*boardSize*boardSize)+j*boardSize+rookPos];
				bishops[i*boardSize+j] = variables[(i*boardSize*boardSize)+j*boardSize+bishopPos];
				knights[i*boardSize+j] = variables[(i*boardSize*boardSize)+j*boardSize+knightPos];
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
					pos[v] = variables[(i*boardSize*boardSize)+j*boardSize+v];
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
					if (variables[i*boardSize*boardSize+j*boardSize+v].getValue() == 1){
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
	
	static IntVar getX(IntVar[] variables, int boardSize, int i, int j, char v){
		int valueIndex = 0;
		for (int k = 1; k<pieces.length; k++){
			if (v == pieces[k]){
				valueIndex = k;
			}
		}
		return variables[(i*boardSize*boardSize)+j*boardSize+valueIndex];
	}

}
