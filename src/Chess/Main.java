package Chess;

public class Main {

	public static void main(String[] args) {
        int[] parameters;

        parameters = parser(args);
        int boardSize = parameters[0];
        int k1 = parameters[1];
        int k2 = parameters[2];
        int k3 = parameters[3];
        int choice = parameters[4];
        switch(choice){
        case 0:
        	System.out.println("T: "+k1+" | F: "+k2+" | C: "+k3);
        	CSP.domination(boardSize, k1, k2, k3);
        	break;
        case 1:
        	CSP.independance(boardSize, k1, k2, k3);
        	break;
        }
	}

	public static int[] parser(String[] parameters) {
        //domination = 0    independance = 1
        int[] result = new int[5]; //0=chessSize, 1=rook, 2=bischop, 3=knight, 4=csp
        int index = 0;

	    while (index < parameters.length) {

            if (parameters[index].equals("-i")) result[4] = 1;
            else if (parameters[index].equals("-d")) result[4] = 0;
            else if (parameters[index].equals("-n")) {
                ++index;
                result[0] = Integer.parseInt(parameters[index]);
            }
            else if (parameters[index].equals("-f")) {
                ++index;
                result[2] = Integer.parseInt(parameters[index]);
            }
            else if (parameters[index].equals("-c")) {
                ++index;
                result[3] = Integer.parseInt(parameters[index]);
            }
            else if (parameters[index].equals("-t")) {
                ++index;
                result[1] = Integer.parseInt(parameters[index]);
            }
            ++index;
        }
        return result;

    }

}
