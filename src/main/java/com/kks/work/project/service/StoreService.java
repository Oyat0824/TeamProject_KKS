package com.kks.work.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.StoreRepository;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Store;

@Service
public class StoreService {
	private StoreRepository storeRepository;

	@Autowired
	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}

// 서비스 메서드
	// 가게 등록
	public ResultData<Integer> registerStore(String storeName, String storeLogo, String storeImg, String storeDesc,
			int memberId) {
		// 가게 중복 신청 금지
		Store existsStore = getStoreByMemberId(memberId);
		if (existsStore != null) {
			return ResultData.from("F-1", "가게 신청은 하나만 가능합니다.");
		}

		// 가게 이름 중복 체크
		existsStore = getStoreByStoreName(storeName);
		if (existsStore != null) {
			return ResultData.from("F-2", Utility.f("이미 사용중인 가게 이름(%s)입니다.", storeName));
		}

		storeRepository.registerStore(storeName, storeLogo, storeImg, storeDesc, memberId);

		int id = storeRepository.getLastInsertId();

		return ResultData.from("S-1", Utility.f("%d번 가게가 생성되었습니다.", id), "id", id);
	}

	// 아이디를 통해 가게 가져오기
	public Store getStoreById(int id) {
		return storeRepository.getStoreById(id);
	}

	// memberId로 가게 정보 가져오기
	private Store getStoreByMemberId(int memberId) {
		return storeRepository.getStoreByMemberId(memberId);
	}

	// 가게 이름으로 가게 정보 가져오기
	public Store getStoreByStoreName(String storeName) {
		return storeRepository.getStoreByStoreName(storeName);
	}

}
