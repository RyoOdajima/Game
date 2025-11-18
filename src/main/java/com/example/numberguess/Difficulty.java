package com.example.numberguess;

public enum Difficulty {
	EASY(50, 10), NORMAL(100, 8), HARD(500, 7);

	private final int maxNumber;
	private final int maxTries;

	Difficulty(int maxNumber, int maxTries) {
		this.maxNumber = maxNumber;
		this.maxTries = maxTries;
	}

	public int getMaxNumber() {
		return maxNumber;
	}

	public int getMaxTries() {
		return maxTries;
	}
}