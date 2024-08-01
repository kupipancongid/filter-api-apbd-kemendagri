package id.go.dinasesdmaceh.anggaran_json_filter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.go.dinasesdmaceh.anggaran_json_filter.dto.AnggaranDTO;
import id.go.dinasesdmaceh.anggaran_json_filter.model.request.UploadRequest;
import id.go.dinasesdmaceh.anggaran_json_filter.model.response.FileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterService {
    private static final String UPLOAD_DIR = "src/main/resources/static/upload";

    public void upload(UploadRequest request){
        if (request.getFile().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not found.");
        }

        try{
            Path path = Path.of(UPLOAD_DIR);
            byte[] bytes = request.getFile().getBytes();
            File dir = new File(path.toString() + File.separator );
            if (!dir.exists()){
                dir.mkdirs();
            }

            String extension = StringUtils.getFilenameExtension(request.getFile().getOriginalFilename());
            String filename = getFileNameWithoutExtension(request.getFile().getOriginalFilename());
            if (
                    !extension.equals("json")
            ){
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "File format is not allowed");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);


            File serverFile = new File(path.toString() + File.separator + "anggaran_json_" + timestamp + "." + extension);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }
    public List<FileResponse> getAllFiles() {
        File uploadDir = Paths.get(UPLOAD_DIR).toFile();
        List<FileResponse> fileResponses = new ArrayList<>();

        if (uploadDir.exists() && uploadDir.isDirectory()) {
            File[] files = uploadDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileResponses.add(FileResponse.builder()
                                .fileName(file.getName())
                                .path(file.getAbsolutePath())
                                .build());
                    }
                }
            }
        }

        return fileResponses;
    }

    public void delete(String filename) {
        Path path = Paths.get(UPLOAD_DIR, filename);
        File file = path.toFile();
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
    }

    private String getFileNameWithoutExtension(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return fileName.substring(0, lastDotIndex);
        } else {
            return fileName;
        }
    }


    public List<AnggaranDTO> filter(String filename, String dinasName, String year){
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = FilterService.class.getClassLoader().getResourceAsStream("static/upload/"+filename)) {
            if (inputStream == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File not found in resources: upload/"+filename);
            }

            List<AnggaranDTO> dataList = objectMapper.readValue(inputStream, new TypeReference<List<AnggaranDTO>>() {});

            List<AnggaranDTO> dinasFiltered = dataList.stream()
                    .filter(data -> dinasName.equals(data.getNama_skpd()))
                    .filter(data -> year.equals(data.getTahun()))
                    .collect(Collectors.toList());

            return dinasFiltered;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hubungi administrator");
        }
    }
}
