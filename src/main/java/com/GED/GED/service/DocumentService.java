package com.GED.GED.service;

import com.GED.GED.entities.Document;
import com.GED.GED.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    public Document saveDocument(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")){
                throw new Exception("filename contain invalid sequence");
            }

            Document document = new Document(fileName,
                    file.getContentType(),
                    file.getBytes()
            );
            return repository.save(document);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new Exception("could not save file" + e.getMessage());
        }
    }

    public Document getDocument(String fileId) throws Exception {
        return repository.findById(fileId).orElseThrow(()-> new Exception("file not found"));
    }
}
