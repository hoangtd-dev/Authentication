package com.backend10.ms_news.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend10.ms_news.dto.NewsRequest;
import com.backend10.ms_news.entities.News;
import com.backend10.ms_news.repositories.NewsRepository;

@Service
public class NewsService {
	private final NewsRepository newsRepository;

	public NewsService(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	public List<News> findAll() {
		return newsRepository.findAll();
	}

	@Transactional
	public List<News> createAll(List<NewsRequest> requests) {
		return requests.stream().map(r -> {
			if (r.title() == null || r.title().isBlank()) {
				throw new IllegalArgumentException("Title must not be blank");
			}
			return newsRepository.save(new News(r.title(), r.content()));
		}).toList();
	}
}
