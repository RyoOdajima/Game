package com.example.numberguess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RankingService {

	private static final String RANKING_FILE = "highscores.csv";

	// スコアをファイルに追記保存
	public void saveScore(ScoreRecord record) {
		Path path = Paths.get(RANKING_FILE);
		String line = record.toCsv() + System.lineSeparator();
		try {
			Files.writeString(path, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 上位N件を取得
	public List<ScoreRecord> loadTopScores(int limit) {
		Path path = Paths.get(RANKING_FILE);
		if (!Files.exists(path)) {
			return new ArrayList<>();
		}

		try {
			List<String> lines = Files.readAllLines(path);
			return lines.stream().map(ScoreRecord::fromCsv).filter(r -> r != null)
					.sorted(Comparator.comparingInt(ScoreRecord::getScore).reversed()).limit(limit)
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

}
