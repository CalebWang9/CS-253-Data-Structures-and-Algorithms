import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphMain {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String sizeChoice;
		String fileChoice;

		boolean invalid = true;

		do {
			System.out.println("Please select which size file you want to read/run: small, medium, or large");

			sizeChoice = scan.nextLine();

			if (sizeChoice != null
					&& (sizeChoice.equals("small") || sizeChoice.equals("medium") || sizeChoice.equals("large"))) {
				invalid = false;
			} else {
				System.out.println("Invalid option please pick small, medium, or large");
			}

		} while (invalid);

		boolean invalid2 = true;

		do {
			System.out.println("Please select which dataset you want to read/run: A or B");

			fileChoice = scan.nextLine();

			if (fileChoice != null && (fileChoice.equals("A") || fileChoice.equals("B"))) {
				invalid2 = false;
			} else {
				System.out.println("Invalid option please pick A or B");
			}

		} while (invalid2);

		long startTime = System.nanoTime();

		scan.close();

		DirectedGraph Test1 = new DirectedGraph(100);

		try (BufferedReader reader = new BufferedReader(
				new FileReader("Project2/dataset/dataset " + fileChoice + "/" + sizeChoice + ".txt"))) {
			String line;
			String first;
			String second;
			int firstNumber = 0;
			int secondNumber = 0;

			while ((line = reader.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == 32) {
						first = line.substring(0, i);
						second = line.substring(i + 1);
						firstNumber = Integer.parseInt(first);
						secondNumber = Integer.parseInt(second);

						break;
					}
				}

				Test1.addEdge(Test1.new Node(firstNumber, null), Test1.new Node(secondNumber, null));
			}

		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			e.printStackTrace();
		}

		ArrayList<DirectedGraph.Node> temp = Test1.getAllVertices();
		for (int i = 0; i < temp.size(); i++) {
			DirectedGraph.Node temp2 = temp.get(i);
			while (temp2 != null) {
				System.out.print(temp2.getValue() + " ");
				temp2 = temp2.getNext();
			}
			System.out.println();
		}

		long endTime = System.nanoTime();
		double delta = (endTime - startTime) / 1e6;
		System.out.println("Runtime: " + delta + " ms");

		pageRank pr = new pageRank(Test1);

		for (int i = 0; i < pr.pr2.length; i++) {
			System.out.println("Page Rank of node " + i + " is " + pr.pr2[i]);
		}

		System.out.println("number of iterations: "+pr.iterationsOfPR);

		pr.getTopten();
	}
}
