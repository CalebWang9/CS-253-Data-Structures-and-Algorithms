import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class UnionFindMain {
    public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String sizeChoice;
		String fileChoice;
		long arrayAccesses = 0;
		long revisitConnections = 0;

		boolean invalid = true;

		do {
			System.out.println("Please select which size file you want to read/run: 1k, 10k, 100k, or 1000k");

			sizeChoice = scan.nextLine();

			if (sizeChoice != null && (sizeChoice.equals("1k") || sizeChoice.equals("10k") || sizeChoice.equals("100k")
					|| sizeChoice.equals("1000k"))) {
				invalid = false;
			}

			else {
				System.out.println("Invalid option please pick 1k, 10k, 100k, or 1000k");
			}

		} while (invalid);

		boolean invalid2 = true;

		do {
			System.out.println("Please select which file you want to read/run: a, b, c, d, or e");

			fileChoice = scan.nextLine();

			if (fileChoice != null && (fileChoice.equals("a") || fileChoice.equals("b") || fileChoice.equals("c")
					|| fileChoice.equals("d") || fileChoice.equals("e"))) {
				invalid2 = false;
			}

			else {
				System.out.println("Invalid option please pick a, b, c, d, or e");
			}

		} while (invalid2);


		int n = 0;
		if (sizeChoice.equals("1k")) {
			n = 1000;
		} else if (sizeChoice.equals("10k")) {
			n = 10000;
		} else if (sizeChoice.equals("100k")) {
			n = 100000;
		} else {
			n = 1000000;
		}

		int ufChoice;
		do {
			System.out.println("which UF do you want to run? 1, 2, 3, or 0 to quit");
			ufChoice = Integer.parseInt(scan.nextLine());
			if (ufChoice == 0) {
				break;
			}

			QuickUnion Test1 = null;
			long startTime = System.nanoTime();
			if (ufChoice == 1) {
				Test1 = new QuickUnion(n);
			} else if (ufChoice == 2) {
				Test1 = new WeightedQuickUnion(n);
			} else {
				Test1 = new WQUUFPathCompression(n);
			}

			try (BufferedReader reader = new BufferedReader(
					new FileReader("datasets/" + sizeChoice + "/" + fileChoice + ".txt"))) {
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
					Test1.Union(firstNumber, secondNumber);
				}

			} catch (IOException e) {
				System.out.println("Error reading file: " + e.getMessage());
				e.printStackTrace();
			}

			long endTime = System.nanoTime();
			double delta = (endTime - startTime) / 1e6;

			System.out.println("Max Tree Height: " + Test1.getMaxTreeHeight());
			System.out.println("Array Accesses: " + Test1.arrayAccesses);
			System.out.println("Re-visits: " + Test1.revisitConnections);
			System.out.println("Runtime: " + delta + " ms");
		} while (true);

		scan.close();
	}
}
