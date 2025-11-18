package com.example.numberguess;

public class GameResult {

	private final Difficulty difficulty;
	private final int maxTries;
	private final int usedTries;
	private final int usedHints;
	private final boolean success;
	private final int score;

	public GameResult(Difficulty difficulty, int maxTries, int usedTries, int usedHints, boolean success, int score) {
		this.difficulty = difficulty;
		this.maxTries = maxTries;
		this.usedTries = usedTries;
		this.usedHints = usedHints;
		this.success = success;
		this.score = score;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public int getMaxTries() {
		return maxTries;
	}

	public int getUsedHints() {
		return usedHints;
	}

	public boolean isSuccess() {
		return success;
	}

	public int getScore() {
		return score;
	}
}