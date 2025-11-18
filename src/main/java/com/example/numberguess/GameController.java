package com.example.numberguess;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

	@Autowired
	private RankingService rankingService;

	@Autowired
	HttpSession session;

	// ====================
	// ★start(難易度選択)実装
	// ====================
	@PostMapping("/start")
	public String start(@RequestParam("difficulty") String difficulty, Model model, HttpServletRequest request) {
		Difficulty diff = Difficulty.valueOf(difficulty);

		// ★ 難易度に応じてランダム範囲を変更
		int answer = new Random().nextInt(diff.getMaxNumber()) + 1;

		session.invalidate();
		session = request.getSession(true);

		session.setAttribute("answer", answer);
		session.setAttribute("histories", new ArrayList<History>());
		session.setAttribute("usedHints", 0);
		session.setAttribute("remainingTries", diff.getMaxTries());
		session.setAttribute("currentDifficulty", diff.name());

		model.addAttribute("histories", new ArrayList<History>());
		model.addAttribute("usedHints", 0);
		model.addAttribute("remainingTries", diff.getMaxTries());
		model.addAttribute("currentDifficulty", diff.name());
		model.addAttribute("lastJudgeMessage", null);

		return "game";
	}

	//
	// ====================
	// 初期表示（旧EASY固定）
	// ====================
	@GetMapping("/")
	public String index(Model model, HttpServletRequest request) {

		// セッション初期化
		session.invalidate();
		session = request.getSession(true);

		Difficulty diff = Difficulty.EASY; // EASYを明示

		session.setAttribute("answer", new Random().nextInt(diff.getMaxNumber()) + 1);
		session.setAttribute("histories", new ArrayList<History>());
		session.setAttribute("usedHints", 0);
		session.setAttribute("remainingTries", diff.getMaxTries());
		session.setAttribute("currentDifficulty", diff.name());

		model.addAttribute("histories", new ArrayList<History>());
		model.addAttribute("usedHints", 0);
		model.addAttribute("remainingTries", diff.getMaxTries());
		model.addAttribute("currentDifficulty", diff.name());
		model.addAttribute("lastJudgeMessage", null);

		return "game";
	}

	// 回答処理
	@PostMapping("/challenge")
	public String challenge(@RequestParam("number") int number, Model model) {
		// セッションから答えを取得
		int answer = (Integer) session.getAttribute("answer");

		// ユーザーの回答履歴を取得
		@SuppressWarnings("unchecked")
		List<History> histories = (List<History>) session.getAttribute("histories");

		int usedHints = (Integer) session.getAttribute("usedHints");
		int remainingTries = (Integer) session.getAttribute("remainingTries");
		String difficulty = (String) session.getAttribute("currentDifficulty");

		String message;

		// 回答のif文

		if (number == answer) {
			message = "正解です";

			// ★正解した時だけランキング保存
			String playerName = "Guest";
			int score = 100;// 本当はGameResultのスコア
			String playedAt = LocalDateTime.now().toString();

			ScoreRecord record = new ScoreRecord(playerName, score, Difficulty.valueOf(difficulty), playedAt);
			rankingService.saveScore(record);

		} else if (number < answer) {
			message = "もっと大きいです。";
		} else {
			message = "もっと小さいです";
		}

		histories.add(new History(histories.size() + 1, number, message));

		// セッション側の残り回数を確実に1減らす
		remainingTries--;
		session.setAttribute("remainingTries", remainingTries);
		session.setAttribute("histories", histories);

		// HTMLに渡す
		model.addAttribute("remainingTries", remainingTries);

		model.addAttribute("histories", histories);
		model.addAttribute("usedHints", usedHints);
		model.addAttribute("currentDifficulty", difficulty);
		model.addAttribute("lastJudgeMessage", message);

		return "game";

	}

	// ヒント処理
	@PostMapping("/hint")
	public String hint(Model model) {

		int answer = (Integer) session.getAttribute("answer");

		int usedHints = (Integer) session.getAttribute("usedHints");
		int remainingTries = (Integer) session.getAttribute("remainingTries");
		String difficulty = (String) session.getAttribute("currentDifficulty");

		@SuppressWarnings("unchecked")
		List<History> histories = (List<History>) session.getAttribute("histories");

		String hint;

		if (usedHints >= 2) {
			hint = "これ以上ヒントは使えません";
		} else {
			usedHints++;
			if (usedHints == 1) {
				hint = (answer % 2 == 0) ? "偶数です" : "奇数です";
			} else {
				// 難易度対応した2回目ヒント
				int max = Difficulty.valueOf(difficulty).getMaxNumber();
				hint = (answer <= max / 2) ? "下半分の範囲にあります" : "上半分の範囲にあります";
			}
		}

		session.setAttribute("usedHints", usedHints);

		// HTMLに戻す
		model.addAttribute("histories", histories);
		model.addAttribute("usedHints", usedHints);
		model.addAttribute("remainingTries", remainingTries);
		model.addAttribute("currentDifficulty", difficulty);
		model.addAttribute("lastJudgeMessage", hint);

		return "game";
	}
}