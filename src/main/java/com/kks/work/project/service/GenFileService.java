package com.kks.work.project.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.google.common.base.Joiner;
import com.kks.work.project.repository.GenFileRepository;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.GenFile;

@Service
public class GenFileService {
	// 파일 생성 경로
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;
	
	private GenFileRepository genFileRepository;
	
	// 의존성 주입
	@Autowired
	public GenFileService(GenFileRepository genFileRepository) {
		this.genFileRepository = genFileRepository;
	}
	
	public ResultData saveMeta(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo,
			String originFileName, String fileExtTypeCode, String fileExtType2Code, String fileExt, int fileSize,
			String fileDir) {

		Map<String, Object> param = Utility.mapOf("relTypeCode", relTypeCode, "relId", relId, "typeCode", typeCode,
				"type2Code", type2Code, "fileNo", fileNo, "originFileName", originFileName, "fileExtTypeCode",
				fileExtTypeCode, "fileExtType2Code", fileExtType2Code, "fileExt", fileExt, "fileSize", fileSize,
				"fileDir", fileDir);
		genFileRepository.saveMeta(param);

		int id = Utility.getAsInt(param.get("id"), 0);
		return new ResultData("S-1", "성공하였습니다.", "id", id);
	}

	public ResultData save(MultipartFile multipartFile, String relTypeCode, int relId, String typeCode,
			String type2Code, int fileNo) {
		String fileInpUtilityName = multipartFile.getName();
		String[] fileInpUtilityNameBits = fileInpUtilityName.split("__");

		if (fileInpUtilityNameBits[0].equals("file") == false) {
			return new ResultData("F-1", "파라미터 명이 올바르지 않습니다.");
		}

		int fileSize = (int) multipartFile.getSize();

		if (fileSize <= 0) {
			return new ResultData("F-2", "파일이 업로드 되지 않았습니다.");
		}

		String originFileName = multipartFile.getOriginalFilename();
		String fileExtTypeCode = Utility.getFileExtTypeCodeFromFileName(multipartFile.getOriginalFilename());
		String fileExtType2Code = Utility.getFileExtType2CodeFromFileName(multipartFile.getOriginalFilename());
		String fileExt = Utility.getFileExtFromFileName(multipartFile.getOriginalFilename()).toLowerCase();

		if (fileExt.equals("jpeg")) {
			fileExt = "jpg";
		} else if (fileExt.equals("htm")) {
			fileExt = "html";
		}

		String fileDir = Utility.getNowYearMonthDateStr() + "/" + type2Code;

		if (relId > 0) {
			GenFile oldGenFile = getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);

			if (oldGenFile != null) {
				deleteGenFile(oldGenFile);
			}
		}

		ResultData saveMetaRd = saveMeta(relTypeCode, relId, typeCode, type2Code, fileNo, originFileName,
				fileExtTypeCode, fileExtType2Code, fileExt, fileSize, fileDir);
		int newGenFileId = (int) saveMetaRd.getBody().get("id");

		// 새 파일이 저장될 폴더(io파일) 객체 생성
		String targetDirPath = genFileDirPath + "/" + relTypeCode + "/" + fileDir;
		java.io.File targetDir = new java.io.File(targetDirPath);

		// 새 파일이 저장될 폴더가 존재하지 않는다면 생성
		if (targetDir.exists() == false) {
			targetDir.mkdirs();
		}

		String targetFileName = newGenFileId + "." + fileExt;
		String targetFilePath = targetDirPath + "/" + targetFileName;

		// 파일 생성(업로드된 파일을 지정된 경로롤 옮김)
		try {
			multipartFile.transferTo(new File(targetFilePath));
		} catch (IllegalStateException | IOException e) {
			return new ResultData("F-3", "파일저장에 실패하였습니다.");
		}

		return new ResultData("S-1", "파일이 생성되었습니다.", "id", newGenFileId, "fileRealPath", targetFilePath, "fileName",
				targetFileName, "fileInpUtilityName", fileInpUtilityName);
	}

	public ResultData save(MultipartFile multipartFile) {
		String fileInpUtilityName = multipartFile.getName();
		String[] fileInpUtilityNameBits = fileInpUtilityName.split("__");

		String relTypeCode = fileInpUtilityNameBits[1];
		int relId = Integer.parseInt(fileInpUtilityNameBits[2]);
		String typeCode = fileInpUtilityNameBits[3];
		String type2Code = fileInpUtilityNameBits[4];
		int fileNo = Integer.parseInt(fileInpUtilityNameBits[5]);

		return save(multipartFile, relTypeCode, relId, typeCode, type2Code, fileNo);
	}

	public ResultData save(MultipartFile multipartFile, int relId) {
		String fileInpUtilityName = multipartFile.getName();
		String[] fileInpUtilityNameBits = fileInpUtilityName.split("__");

		String relTypeCode = fileInpUtilityNameBits[1];
		String typeCode = fileInpUtilityNameBits[3];
		String type2Code = fileInpUtilityNameBits[4];
		int fileNo = Integer.parseInt(fileInpUtilityNameBits[5]);

		return save(multipartFile, relTypeCode, relId, typeCode, type2Code, fileNo);
	}

	public List<GenFile> getGenFiles(String relTypeCode, int relId, String typeCode, String type2Code) {
		return genFileRepository.getGenFiles(relTypeCode, relId, typeCode, type2Code);
	}

	public GenFile getGenFile(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo) {
		return genFileRepository.getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);
	}

	public ResultData saveFiles(Map<String, Object> param, MultipartRequest multipartRequest) {
		// 업로드 시작
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		Map<String, ResultData> filesResultData = new HashMap<>();
		List<Integer> genFileIds = new ArrayList<>();

		for (String fileInpUtilityName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInpUtilityName);

			if (multipartFile.isEmpty() == false) {
				ResultData fileResultData = save(multipartFile);
				int genFileId = (int) fileResultData.getBody().get("id");
				genFileIds.add(genFileId);

				filesResultData.put(fileInpUtilityName, fileResultData);
			}
		}

		String genFileIdsStr = Joiner.on(",").join(genFileIds);

		// 삭제 시작
		int deleteCount = 0;

		for (String inpUtilityName : param.keySet()) {
			String[] inpUtilityNameBits = inpUtilityName.split("__");

			if (inpUtilityNameBits[0].equals("deleteFile")) {
				String relTypeCode = inpUtilityNameBits[1];
				int relId = Integer.parseInt(inpUtilityNameBits[2]);
				String typeCode = inpUtilityNameBits[3];
				String type2Code = inpUtilityNameBits[4];
				int fileNo = Integer.parseInt(inpUtilityNameBits[5]);

				GenFile oldGenFile = getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);

				if (oldGenFile != null) {
					deleteGenFile(oldGenFile);
					deleteCount++;
				}
			}
		}

		return new ResultData("S-1", "파일을 업로드하였습니다.", "filesResultData", filesResultData, "genFileIdsStr",
				genFileIdsStr, "deleteCount", deleteCount);
	}

	public void changeRelId(int id, int relId) {
		genFileRepository.changeRelId(id, relId);
	}

	public void deleteGenFiles(String relTypeCode, int relId) {
		List<GenFile> genFiles = genFileRepository.getGenFilesByRelTypeCodeAndRelId(relTypeCode, relId);

		for (GenFile genFile : genFiles) {
			deleteGenFile(genFile);
		}
	}

	public ResultData deleteGenFiles(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo) {
		GenFile genFile = genFileRepository.getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);
		
		if(genFile == null) {
			return ResultData.from("F-1", "파일이 존재하지 않습니다.");
		}
		
		deleteGenFile(genFile);
		
		return ResultData.from("S-1", "파일 삭제 성공");
	}

	private void deleteGenFile(GenFile genFile) {
		String filePath = genFile.getFilePath(genFileDirPath);
		Utility.deleteFile(filePath);

		genFileRepository.deleteFile(genFile.getId());
	}

	public GenFile getGenFile(int id) {
		return genFileRepository.getGenFileById(id);
	}
	
	public Map<Integer, Map<String, GenFile>> getFilesMapKeyRelIdAndFileNo(String relTypeCode, List<Integer> relIds,
			String typeCode, String type2Code) {
		List<GenFile> genFiles = genFileRepository.getGenFilesRelTypeCodeAndRelIdsAndTypeCodeAndType2Code(relTypeCode,
				relIds, typeCode, type2Code);
		Map<String, GenFile> map = new HashMap<>();
		Map<Integer, Map<String, GenFile>> rs = new LinkedHashMap<>();

		for (GenFile genFile : genFiles) {
			if (rs.containsKey(genFile.getRelId()) == false) {
				rs.put(genFile.getRelId(), new LinkedHashMap<>());
			}

			rs.get(genFile.getRelId()).put(genFile.getFileNo() + "", genFile);
		}

		return rs;
	}

	public void changeInpUtilityFileRelIds(Map<String, Object> param, int id) {
		String genFileIdsStr = Utility.ifEmpty((String) param.get("genFileIdsStr"), null);

		if (genFileIdsStr != null) {
			List<Integer> genFileIds = Utility.getListDividedBy(genFileIdsStr, ",");

			// 파일이 먼저 생성된 후에, 관련 데이터가 생성되는 경우에는, file의 relId가 일단 0으로 저장된다.
			// 그것을 뒤늦게라도 이렇게 고쳐야 한다.
			for (int genFileId : genFileIds) {
				changeRelId(genFileId, id);
			}
		}
	}
}