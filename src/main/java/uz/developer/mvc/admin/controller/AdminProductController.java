package uz.developer.mvc.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.developer.entity.attachment.Attachment;
import uz.developer.entity.product.ProductEntity;
import uz.developer.repository.AttachmentContentRepository;
import uz.developer.repository.AttachmentRepository;
import uz.developer.repository.ProductRepository;
import uz.developer.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/admin/product")
public class AdminProductController {

    private final ProductRepository productRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private final FileService fileService;

    @Autowired
    public AdminProductController(ProductRepository productRepository, AttachmentRepository attachmentRepository, AttachmentContentRepository attachmentContentRepository, FileService fileService) {
        this.productRepository = productRepository;
        this.attachmentRepository = attachmentRepository;
        this.attachmentContentRepository = attachmentContentRepository;
        this.fileService = fileService;

    }



    @GetMapping("")
    public String productView(
            Model model
    ) {
        model.addAttribute("product", new ProductEntity());
        return "admin";
    }

    @PostMapping("/add")
    public String addProduct(
            @ModelAttribute ProductEntity product,
            Model model
    ) {
        try {
            productRepository.save(product);
            model.addAttribute("product", product);
            model.addAttribute("product_id", product.getId());
            model.addAttribute("message", "product qo'shildi");
            return "admin";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "product qoshilmadi");
            return "error";
        }
    }

    @PostMapping("/upload")
    public String uploadProduct(
            @RequestParam("file") MultipartFile file,
            @RequestParam("id") String productId,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        ProductEntity productEntity = productRepository.getOne(Integer.parseInt(productId));
        Attachment attachment = fileService.uploadFile(file);
        List<Attachment> attachments = productEntity.getAttachments();
        attachments.add(attachment);
        productEntity.setAttachments(attachments);
        productRepository.save(productEntity);

//        redirectAttributes.addAttribute("message","attachment biriktirildi");

        return "admin";
    }
}
