package com.GED.GED.controller;

import com.GED.GED.dto.ResponseData;
import com.GED.GED.entities.Document;
import com.GED.GED.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class DocumentController {

    @Autowired
    private DocumentService service;

    @PostMapping("/upload")
    public ResponseData upload(@RequestParam("file")MultipartFile file) throws Exception {
        Document document = null;
        String downloadUrl="";
        document = service.saveDocument(file);
        downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(document.getId())
                .toUriString();
        return new ResponseData(document.getFileName(),
                downloadUrl,
                file.getContentType(),
                file.getSize());
    }


    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Document document =null;
        document = service.getDocument(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "document; filename=\"" + document.getFileName()+"\"")
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + document.getFileName()+"\"")
                .body(new ByteArrayResource(document.getData()));
    }
}
