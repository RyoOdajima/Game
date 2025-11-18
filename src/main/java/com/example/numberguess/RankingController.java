package com.example.numberguess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RankingController {

	@Autowired
	private RankingService rankingService;

	@GetMapping("/ranking")
	public String ranking(Model model) {

		// 上位10件を取得
		List<ScoreRecord> scores = rankingService.loadTopScores(10);

		model.addAttribute("scores", scores);

		return "ranking"; // template/ranking.html

	}

}
