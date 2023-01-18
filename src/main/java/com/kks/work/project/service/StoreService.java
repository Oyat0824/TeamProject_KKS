package com.kks.work.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.StoreRepository;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Store;

@Service
public class StoreService {
	private AttrService attrService;
	
	private StoreRepository storeRepository;
	
	@Autowired
	public StoreService(StoreRepository storeRepository, AttrService attrService) {
		this.storeRepository = storeRepository;
		this.attrService = attrService;
	}
	
// 서비스 메서드
	// 스토어 등록
	public ResultData<Integer> registerStore(String storeName, String storeDesc, int memberId) {
		// 스토어 중복 신청 금지
		Store existsStore = getStoreByMemberId(memberId);
		if (existsStore != null) {
			return ResultData.from("F-1", "스토어 신청은 하나만 가능합니다.");
		}
		
		// 스토어 이름 중복 체크
		existsStore = getStoreByStoreName(storeName);
		if (existsStore != null) {
			return ResultData.from("F-2", Utility.f("이미 사용중인 스토어 이름(%s)입니다.", storeName));
		}
		
		storeRepository.registerStore(storeName, storeDesc, memberId);
		storeRepository.storeStateChange(memberId);
		
		int id = storeRepository.getLastInsertId();
		
		return ResultData.from("S-1", Utility.f("%d번 스토어가 생성되었습니다.", id), "id", id);
	}
	
	// 아이디를 통해 스토어 가져오기
	public Store getStoreById(int id) {
		return storeRepository.getStoreById(id);
	}
	
	// memberId로 스토어 정보 가져오기
	private Store getStoreByMemberId(int memberId) {
		return storeRepository.getStoreByMemberId(memberId);
	}
	
	// storeName으로 스토어 정보 가져오기
	public Store getStoreByStoreName(String storeName) {
		return storeRepository.getStoreByStoreName(storeName);
	}

	// 스토어 상세보기
	public Store getForPrintStoreById(int loginedMemberId, int id) {
		Store store = storeRepository.getForPrintStoreById(id);
		
		actorCanChangeData(loginedMemberId, store);
		
		return store;
	}
	
	// 스토어 정보 수정
	public void doModify(int id, int loginedMemberId, String storeDesc) {
		storeRepository.doModify(id, loginedMemberId, storeDesc);
		attrService.remove("store", loginedMemberId, "extra", "storeModifyAuthKey");
	}
	
	
	// 검증
	public ResultData<?> actorCanMD(int loginedMemberId, Store store) {
		if (store == null) {
			return ResultData.from("F-1", "해당 스토어는 존재하지 않습니다.");
		}
		
		if (store.getMemberId() != loginedMemberId) {
			return ResultData.from("F-A", "해당 스토어에 대한 권한이 없습니다.");
		}
		
		return ResultData.from("S-1", "가능");
	}
	
	private void actorCanChangeData(int loginedMemberId, Store store) {
		if(store == null) {
			return;
		}
		
		ResultData<?> actorCanChangeDataRd = actorCanMD(loginedMemberId, store);
		
		store.setActorCanChangeData(actorCanChangeDataRd.isSuccess());
		
	}

	// 인증키 생성
	public String genStoreModifyAuthKey(int loginedMemberId) {
		String storeModifyAuthKey = Utility.getTempPassword(10);
		attrService.setValue("store", loginedMemberId, "extra", "storeModifyAuthKey", storeModifyAuthKey,
				Utility.getDateStrLater(60 * 5));

		return storeModifyAuthKey;
	}

	// 인증키 확인
	public ResultData<?> chkStoreModifyAuthKey(int loginedMemberId, String storeModifyAuthKey) {
		String saved = attrService.getValue("store", loginedMemberId, "extra", "storeModifyAuthKey");
		
		if (saved.equals(storeModifyAuthKey) == false) {
			return ResultData.from("F-1", "일치하지 않거나 만료된 인증코드입니다.");
		}

		return ResultData.from("S-1", "정상 인증코드입니다");
	}
}
