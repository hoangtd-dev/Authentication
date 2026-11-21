package com.backend10.ms_news.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend10.jwt.RequireJwt;
import com.backend10.ms_news.dto.NewsRequest;
import com.backend10.ms_news.entities.News;
import com.backend10.ms_news.services.NewsService;

@RestController
@RequestMapping("/news")
public class NewsController {
	private final NewsService newsService;

	public NewsController(NewsService newsService) {
		this.newsService = newsService;
	}

	@RequireJwt
	@GetMapping
	public ResponseEntity<List<News>> getNews() {
		return ResponseEntity.ok(newsService.findAll());
	}

	@RequireJwt
	@PostMapping("/batch")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<List<News>> createNews(@RequestBody List<NewsRequest> requests) {
		return ResponseEntity.status(HttpStatus.CREATED).body(newsService.createAll(requests));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleInvalid(IllegalArgumentException e) {
		return e.getMessage();
	}
}
