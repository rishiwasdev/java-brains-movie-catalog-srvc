package com.abc.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abc.entity.CatalogItem;

@Repository
public interface CatalogRepo extends JpaRepository<CatalogItem, Long> {
	List<CatalogItem> getCatalogByUserId(String userId);
}
