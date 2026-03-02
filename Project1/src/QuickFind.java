import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class QuickFind {

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

		long startTime = System.nanoTime();

		scan.close();
		int[] QuickFindArray;

		if (sizeChoice.equals("1k")) {
			QuickFindArray = new int[1000];
			for (int i = 0; i < QuickFindArray.length; i++) {
				QuickFindArray[i] = i;
			}
		} else if (sizeChoice.equals("10k")) {
			QuickFindArray = new int[10000];
			for (int i = 0; i < QuickFindArray.length; i++) {
				QuickFindArray[i] = i;
			}

		} else if (sizeChoice.equals("100k")) {
			QuickFindArray = new int[100000];
			for (int i = 0; i < QuickFindArray.length; i++) {
				QuickFindArray[i] = i;
			}

		} else {
			QuickFindArray = new int[1000000];
			for (int i = 0; i < QuickFindArray.length; i++) {
				QuickFindArray[i] = i;
			}
		}

		try (BufferedReader reader = new BufferedReader(
				new FileReader("datasets/" + sizeChoice + "/" + fileChoice + ".txt"))) { //
			String line;
			String first;
			String second;
			int firstNumber = 0;
			int secondNumber = 0;

			while ((line = reader.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == 32) {
						int spaceIndex = i;
						first = line.substring(0, i);
						second = line.substring(i + 1);
						firstNumber = Integer.parseInt(first);
						secondNumber = Integer.parseInt(second);

						break;
					}
				}
				arrayAccesses += 2;
				if (QuickFindArray[firstNumber] == QuickFindArray[secondNumber]) {
					revisitConnections++;
					continue;
				}

				int temp = QuickFindArray[firstNumber];
				int temp2 = QuickFindArray[secondNumber];
				arrayAccesses += 2;

				for (int j = 0; j < QuickFindArray.length; j++) {
					arrayAccesses++;
					if (QuickFindArray[j] == temp || QuickFindArray[j] == temp2) {
						QuickFindArray[j] = secondNumber;
						arrayAccesses++;
					}
				}

			}

		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
			e.printStackTrace();
		}

		long endTime = System.nanoTime();
		double delta = (endTime - startTime) / 1e6;
		// Display the solution you discovered
		System.out.println("Array Accesses: " + arrayAccesses);
		System.out.println("Re-visits: " + revisitConnections);
		System.out.println("Runtime: " + delta + " ms");

	}

}
