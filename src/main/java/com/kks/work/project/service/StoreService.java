package com.kks.work.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.StoreRepository;

@Service
public class StoreService {
	private StoreRepository storeRepository;
	
	@Autowired
	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}
	
// 서비스 메서드
	// 상점 등록
	
}
