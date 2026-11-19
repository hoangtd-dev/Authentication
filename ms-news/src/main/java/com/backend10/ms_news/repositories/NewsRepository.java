package com.backend10.ms_news.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend10.ms_news.entities.News;

public interface NewsRepository extends JpaRepository<News, Integer> {
}
