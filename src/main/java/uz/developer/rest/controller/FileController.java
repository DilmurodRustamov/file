package uz.developer.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.developer.entity.attachment.Attachment;
import uz.developer.entity.attachment.AttachmentContent;
import uz.developer.repository.AttachmentContentRepository;
import uz.developer.repository.AttachmentRepository;
import uz.developer.response.BaseRestResponse;
import uz.developer.response.RestResponse;

import java.util.Optional;

@RestController
@RequestMapping("/api/rest/file")
public class FileController implements RestResponse {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;

    @Autowired
    public FileController(AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentContentRepository = attachmentContentRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<BaseRestResponse> uploadFile(@RequestParam(name = "file") MultipartFile file) {
        try {
            Attachment attachment = new Attachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setSize(file.getSize());
            attachment.setFileContent(file.getContentType());
            attachmentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setFileContent(file.getBytes());
            attachmentContent.setAttachment(attachment);
            attachmentContentRepository.save(attachmentContent);


            return ResponseEntity.status(HttpStatus.CREATED).body(SUCCESS);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR);

        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAttachment(){
        return ResponseEntity.status(HttpStatus.OK).body(attachmentRepository.findAll());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Object> downloadFile(@PathVariable("id") int attachmentId){
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachmentId);
        if (optionalAttachment.isPresent()){
            Attachment attachment = optionalAttachment.get();
            AttachmentContent attachmentContent = attachmentContentRepository.findByAttachment(attachment);
            return ResponseEntity.status(HttpStatus.OK).body(attachmentContent);


        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attachment topilmadi");
    }

    @GetMapping("v2/download/{id}")
    public ResponseEntity<Object> downloadFileFromDb(
            @PathVariable("id") int attachmentId
    ){
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachmentId);
        if (optionalAttachment.isPresent()){
            Attachment attachment = optionalAttachment.get();
            AttachmentContent attachmentContent = attachmentContentRepository.findByAttachment(attachment);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(attachment.getFileContent()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment fileName= \""+attachment.getFileName()+"\"")
                    .body(attachmentContent.getFileContent());
        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attachment topilmadi");
    }
}
