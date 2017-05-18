package Chess;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        int[] parameters;

        parameters = parser(args);


	}

	public static int[] parser(String[] parameters) {
        //domination = 0    independance = 1
        int[] result = new int[5]; //0=chessSize, 1=bischop, 2=knight, 3=rook, 4=csp
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
                result[1] = Integer.parseInt(parameters[index]);
            }
            else if (parameters[index].equals("-c")) {
                ++index;
                result[2] = Integer.parseInt(parameters[index]);
            }
            else if (parameters[index].equals("-t")) {
                ++index;
                result[3] = Integer.parseInt(parameters[index]);
            }
            ++index;
        }
        return result;

    }

}
