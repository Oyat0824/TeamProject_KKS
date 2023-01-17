package com.kks.work.project.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

import com.kks.work.project.exception.GenFileNotFoundException;
import com.kks.work.project.service.GenFileService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.vo.GenFile;

@Controller
public class GenFileController {
	// 파일 생성 경로
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	// 의존성 주입
	@Autowired
	private GenFileService genFileService;

	// POST 요청을 매핑해 파일 업로드 기능을 구현
	@RequestMapping("/common/genFile/doUpload")
	@ResponseBody
	public ResultData<?> doUpload(@RequestParam Map<String, Object> param, MultipartRequest multipartRequest) {
		return genFileService.saveFiles(param, multipartRequest);
	}

	// GET 요청을 매핑해 파일 다운로드 구현
	@GetMapping("/common/genFile/doDownload")
	public ResponseEntity<Resource> downloadFile(int id, HttpServletRequest request) throws IOException {
		GenFile genFile = genFileService.getGenFile(id);

		if (genFile == null) {
			throw new GenFileNotFoundException();
		}

		String filePath = genFile.getFilePath(genFileDirPath);

		Resource resource = new InputStreamResource(new FileInputStream(filePath));

		// Try to determine file's content type
		String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + genFile.getOriginFileName() + "\"")
				.contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

	// GET 요청을 매핑해 파일을 보여주는 기능 구현 
	@GetMapping("/common/genFile/file/{relTypeCode}/{relId}/{typeCode}/{type2Code}/{fileNo}")
	public ResponseEntity<Resource> showFile(HttpServletRequest request, @PathVariable String relTypeCode,
			@PathVariable int relId, @PathVariable String typeCode, @PathVariable String type2Code,
			@PathVariable int fileNo) throws FileNotFoundException {
		GenFile genFile = genFileService.getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);

		if (genFile == null) {
			throw new GenFileNotFoundException();
		}

		String filePath = genFile.getFilePath(genFileDirPath);
		Resource resource = new InputStreamResource(new FileInputStream(filePath));

		// Try to determine file's content type
		String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}
}