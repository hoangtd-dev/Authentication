package com.backend10.ms_news;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.backend10.ms_news.entities.News;
import com.backend10.ms_news.repositories.NewsRepository;

@Component
public class DataSeeder implements CommandLineRunner {
	private final NewsRepository newsRepository;

	public DataSeeder(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	@Override
	public void run(String... args) {
		if (newsRepository.count() == 0) {
			newsRepository.save(new News("First news", "Hello microservice"));
			newsRepository.save(new News("Second news", "JWT protected"));
		}
	}
}
