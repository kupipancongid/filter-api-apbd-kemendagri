package id.go.dinasesdmaceh.anggaran_json_filter.controller;

import id.go.dinasesdmaceh.anggaran_json_filter.model.response.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URISyntaxException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<WebResponse<String>> noHandlerFoundException(NoHandlerFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(WebResponse.<String>builder().errors("not found").build());
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<WebResponse<String>> noResourceFoundException(NoResourceFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(WebResponse.<String>builder().errors("not found").build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().errors(exception.getReason()).build());
    }

    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<WebResponse<String>> uriSyntaxException(URISyntaxException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<String>builder().errors("Something went wrong.").build());
    }
}
