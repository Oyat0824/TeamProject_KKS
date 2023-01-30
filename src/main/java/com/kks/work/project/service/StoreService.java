package com.kks.work.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.StoreRepository;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Category;
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
	public Store getStoreByMemberId(int memberId) {
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
		attrService.remove("member", loginedMemberId, "extra", "storeModifyAuthKey");
	}
	
	// 스토어 개수
	public int getStoresCount(String searchKeyword) {
		return storeRepository.getStoresCount(searchKeyword);
	}
	
	// 스토어 목록
	public List<Store> getStores(String searchKeyword, int itemsInAPage, int page) {
		int limitStart = (page - 1) * itemsInAPage;
		
		return storeRepository.getStores(searchKeyword, itemsInAPage, limitStart);
	}
	
	
	// 카테고리 가져오기 (id)
	public Category getCategory(int id) {
		return storeRepository.getCategory(id);
	}
	// 카테고리 목록 가져오기 (StoreId)
	public List<Category> getCategorysByStoreId(int storeId) {
		return storeRepository.getCategorysByStoreId(storeId);
	}
	// 카테고리 가져오기 (StoreId, orderNo)
	public Category getCategoryByStoreIdAndOrderNo(int orderNo, int storeId) {
		return storeRepository.getCategoryByStoreIdAndOrderNo(orderNo, storeId);
	}
	// 카테고리 갯수 가져오기
	public int getCategoryCntByStoreId(int storeId) {
		return storeRepository.getCategoryCntByStoreId(storeId);
	}
	// 카테고리 등록
	public void registerCategory(String name, int orderNo, int storeId) {
		storeRepository.registerCategory(name, orderNo, storeId);
	}
	// 카테고리 수정
	public void doCategoryModify(int id, String name, int orderNo, int storeId) {
		Category existCategory = storeRepository.getCategoryByStoreIdAndOrderNo(orderNo, storeId);
		Category category = storeRepository.getCategory(id);
		
		if(category.getOrderNo() == orderNo || existCategory == null) {
			storeRepository.doCategoryModify(id, name, orderNo);
			
			return;
		}

		storeRepository.doCategoryModify(existCategory.getId(), existCategory.getName(), 11);
		storeRepository.doCategoryModify(id, name, orderNo);
		storeRepository.doCategoryModify(existCategory.getId(), existCategory.getName(), category.getOrderNo());
	}
	// 카테고리 삭제
	public void doCategoryDelete(int id) {
		storeRepository.doCategoryDelete(id);
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
		attrService.setValue("member", loginedMemberId, "extra", "storeModifyAuthKey", storeModifyAuthKey, Utility.getDateStrLater(60 * 60));

		return storeModifyAuthKey;
	}

	// 인증키 확인
	public ResultData<?> chkStoreModifyAuthKey(int loginedMemberId, String storeModifyAuthKey) {
		String saved = attrService.getValue("member", loginedMemberId, "extra", "storeModifyAuthKey");
		
		if (saved.equals(storeModifyAuthKey) == false) {
			return ResultData.from("F-1", "일치하지 않거나 만료된 인증코드입니다.");
		}

		return ResultData.from("S-1", "정상 인증코드입니다");
	}

	// 상품 등록시 상점 아이디 공유
	public int getStoreId() {
		return storeRepository.getStoreId();
	}

}
