package uz.developer.mvc.main.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.developer.entity.attachment.AttachmentContent;
import uz.developer.repository.ProductRepository;
import uz.developer.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final FileService fileService;

    @Autowired
    public ProductController(ProductRepository productRepository, FileService fileService) {
        this.productRepository = productRepository;
        this.fileService = fileService;
    }


    @GetMapping("")
    public String getProductList(
            Model model
    ){
        model.addAttribute("productList", productRepository.findAll());
        return "main/main";
    }

    @GetMapping("/{id}")
    public void showProductImage(
            @PathVariable("id") int productId,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        List<AttachmentContent> attachmentContents = fileService.getAttachments(productId);
        httpServletResponse.setContentType("mage/jpeg");
        InputStream is = new ByteArrayInputStream(attachmentContents.get(0).getFileContent());
        IOUtils.copy(is,httpServletResponse.getOutputStream());
    }

    @GetMapping("/{index}")
    public void getAllAttachments(
            @PathVariable("index") int index,
            HttpServletResponse httpServletResponse
            ){}
}
