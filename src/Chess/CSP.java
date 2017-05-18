package Chess;

import choco.integer.IntVar;
import choco.Problem;

public interface CSP {
	
	static void independance(int boardSize, int k1, int k2, int k3){
		Board board = new Board(boardSize);
		Problem problem = new Problem();
		IntVar[] variables = new IntVar[boardSize*boardSize];
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
					variables[(i*boardSize+j)*boardSize*boardSize+k] = problem.makeEnumIntVar("X_"+i+"_"+j+"_"+v, 0,1);
				}
			}
		}
	}
	
	static void domination(int n, int k1, int k2, int k3){
		Board board = new Board(n);
		// create choco solver stuff
	}

}
