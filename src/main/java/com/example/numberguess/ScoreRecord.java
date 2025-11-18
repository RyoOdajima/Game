package com.example.numberguess;

public class ScoreRecord {

	private final String playerName;
	private final int score;
	private final Difficulty difficulty;
	private final String playedAt; // 日付は文字列で簡略化

	public ScoreRecord(String playerName, int score, Difficulty difficulty, String playedAt) {
		this.playerName = playerName;
		this.score = score;
		this.difficulty = difficulty;
		this.playedAt = playedAt;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getScore() {
		return score;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public String getPlayedAt() {
		return playedAt;
	}

	// CSV1行に変換
	public String toCsv() {
		return playerName + "," + score + "," + difficulty + "," + playedAt;
	}

	// CSVから読み込む用（static + String引数）
	public static ScoreRecord fromCsv(String line) {
		String[] parts = line.split(",");
		if (parts.length < 4) {
			return null;
		}

		String name = parts[0];
		int score = Integer.parseInt(parts[1]);
		Difficulty difficulty = Difficulty.valueOf(parts[2]);
		String playedAt = parts[3];
		return new ScoreRecord(name, score, difficulty, playedAt);

	}

}
