package Chess;

import java.util.Scanner;

public class KnightsToDominate {

	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		System.out.println("Entrez une taille d'échiquier: ");
		int boardSize = reader.nextInt();
		reader.close();
		CSP.minimizeKnights(boardSize);
	}

}
