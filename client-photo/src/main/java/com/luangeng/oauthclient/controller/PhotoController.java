package com.luangeng.oauthclient.controller;

import com.luangeng.oauthclient.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @RequestMapping
    public String photos(Model model) throws Exception {
        model.addAttribute("photoIds", photoService.getPhotoIds());
        return "photos";
    }

    @RequestMapping("/{id}")
    public ResponseEntity<BufferedImage> photo(@PathVariable String id, HttpServletRequest request) throws Exception {
        InputStream photo = photoService.loadPhoto(id);
        if (photo == null) {
            throw new UnavailableException("The requested photo does not exist");
        }
        BufferedImage body;
        MediaType contentType = MediaType.IMAGE_JPEG;
        Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType(contentType.toString());
        if (imageReaders.hasNext()) {
            ImageReader imageReader = imageReaders.next();
            ImageReadParam irp = imageReader.getDefaultReadParam();
            imageReader.setInput(new MemoryCacheImageInputStream(photo), true);
            body = imageReader.read(0, irp);
        } else {
            throw new HttpMessageNotReadableException("Could not find javax.imageio.ImageReader for Content-Type ["
                    + contentType + "]");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE,
                Collections.singleton(MediaType.IMAGE_JPEG));
        return new ResponseEntity<BufferedImage>(body, headers, HttpStatus.OK);
    }

}
