package com.example.numberguess;

import java.util.Random;
import java.util.Scanner;

public class GameService {

	private final Random random = new Random();

	public GameResult play(Difficulty difficulty) {

		int target = random.nextInt(difficulty.getMaxNumber()) + 1;
		int maxTries = difficulty.getMaxTries();
		int usedTries = 0;
		int usedHints = 0;
		boolean success = false;

		Scanner scanner = new Scanner(System.in);

		while (usedTries < maxTries) {
			System.out.println("数字を入力（ヒント:h）：");
			String input = scanner.nextLine();

			if (input.equalsIgnoreCase("h")) {
				if (usedHints < 2) {
					usedHints++;
					System.out.println(getHint(target, difficulty.getMaxNumber(), usedHints));
				} else {
					System.out.println("これ以上ヒントは使えません。");
				}
				continue;
			}

			int guess;
			try {
				guess = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("数字を入力してください。");
				continue;
			}

			usedTries++;

			if (guess == target) {
				success = true;
				break;
			} else if (guess < target) {
				System.out.println("もっと大きいです。");

			} else {
				System.out.println("もっと小さいです。");
			}
		}

		int score = calculateScore(difficulty, maxTries, usedTries, usedHints, success);

		return new GameResult(difficulty, maxTries, usedTries, usedHints, success, score);
	}

	private String getHint(int target, int maxNumber, int count) {
		switch (count) {
		case 1:
			return target % 2 == 0 ? "偶数です。" : "奇数です。";
		case 2:
			int half = maxNumber / 2;
			return target <= half ? "下半分の範囲にあります。" : "上半分の範囲にあります。";
		default:
			return "";
		}
	}

	private int calculateScore(Difficulty difficulty, int maxTries, int usedTries, int usedHints, boolean success) {
		if (!success)
			return 0;

		double coef = switch (difficulty) {
		case EASY -> 1.0;
		case NORMAL -> 1.5;
		case HARD -> 2.0;
		};

		int base = (maxTries - usedTries + 1) * 100;
		int penalty = usedHints * 50;

		return Math.max(0, (int) (base * coef) - penalty);
	}

}
