package com.kks.work.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "genFile Not Found")
public class GenFileNotFoundException extends RuntimeException {

}
