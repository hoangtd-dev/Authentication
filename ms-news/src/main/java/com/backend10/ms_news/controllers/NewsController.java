package com.backend10.ms_news.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend10.jwt.RequireJwt;
import com.backend10.ms_news.entities.News;
import com.backend10.ms_news.repositories.NewsRepository;

@RestController
@RequestMapping("/news")
public class NewsController {
	private final NewsRepository newsRepository;

	public NewsController(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	@RequireJwt
	@GetMapping
	public List<News> getNews() {
		return newsRepository.findAll();
	}
}
