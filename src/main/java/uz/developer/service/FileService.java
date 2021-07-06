package uz.developer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.developer.entity.attachment.Attachment;
import uz.developer.entity.attachment.AttachmentContent;
import uz.developer.entity.product.ProductEntity;
import uz.developer.repository.AttachmentContentRepository;
import uz.developer.repository.AttachmentRepository;
import uz.developer.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private final ProductRepository productRepository;

    @Autowired
    public FileService(AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository, ProductRepository productRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentContentRepository = attachmentContentRepository;
        this.productRepository = productRepository;
    }

    public Attachment uploadFile(MultipartFile file){
        try {
            Attachment attachment = new Attachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileContent(file.getContentType());
            attachment.setSize(file.getSize());
            attachmentRepository.save(attachment);

            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setAttachment(attachment);
            attachmentContent.setFileContent(file.getBytes());

            attachmentContentRepository.save(attachmentContent);
            return attachment;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<AttachmentContent> getAttachments(
            int productId
    ){
        ProductEntity productEntity = productRepository.getOne(productId);
        List<Attachment> attachments = productEntity.getAttachments();

        List<AttachmentContent> attachmentContents = new ArrayList<>();
        attachments.forEach(attachment -> {
            AttachmentContent attachmentContent = attachmentContentRepository.findByAttachment(attachment);
            attachmentContents.add(attachmentContent);
        });
        return attachmentContents;
    }
}
