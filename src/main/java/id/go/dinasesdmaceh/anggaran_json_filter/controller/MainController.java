package id.go.dinasesdmaceh.anggaran_json_filter.controller;

import id.go.dinasesdmaceh.anggaran_json_filter.dto.AnggaranDTO;
import id.go.dinasesdmaceh.anggaran_json_filter.model.request.UploadRequest;
import id.go.dinasesdmaceh.anggaran_json_filter.model.response.FileResponse;
import id.go.dinasesdmaceh.anggaran_json_filter.model.response.WebResponse;
import id.go.dinasesdmaceh.anggaran_json_filter.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class MainController {
    @Autowired
    FilterService filterService;

    @PostMapping(
            path = "api/v1/upload"
    )
    public WebResponse<String> upload(
            @RequestParam("fileAnggaran") MultipartFile fileAnggaran){
        UploadRequest request = new UploadRequest();
        request.setFile(fileAnggaran);

        filterService.upload(request);
        return WebResponse.<String>builder()
                .data("Ok")
                .build();
    }

    @GetMapping(path = "api/v1/anggarans")
    public WebResponse<List<FileResponse>> getAnggarans() {
        List<FileResponse> fileResponses = filterService.getAllFiles();
        return WebResponse.<List<FileResponse>>builder()
                .data(fileResponses)
                .build();
    }

    @GetMapping(
            path = "api/v1/anggarans/{fileName}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AnggaranDTO>> getFilteredAnggarans(
            @PathVariable(value = "fileName") String fileName,
            @RequestParam(value = "dinasName") String dinasName,
            @RequestParam(value = "year") String year
    ) {

        List<AnggaranDTO> response = filterService.filter(fileName, dinasName, year);
        return WebResponse.<List<AnggaranDTO>>builder()
                .data(response)
                .build();
    }

    @DeleteMapping(path = "api/v1/anggarans")
    public WebResponse<String> deleteAnggaran(
            @RequestParam(value = "fileName") String fileName
    ){
        filterService.delete(fileName);
        return WebResponse.<String>builder()
                .data("Ok")
                .build();
    }
}
