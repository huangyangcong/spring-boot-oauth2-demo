package com.luangeng.resource.controller;

import com.google.gson.Gson;
import com.luangeng.resource.service.PhotoService;
import com.luangeng.resource.vo.PhotoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Collection;

@Controller
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @RequestMapping("/photos/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("photoId") String id) throws IOException {
        InputStream photo = photoService.loadPhoto(id);
        if (photo == null) {
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = photo.read(buffer);
            while (len >= 0) {
                out.write(buffer, 0, len);
                len = photo.read(buffer);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/jpeg");
            return new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/photos", params = "format=json")
    public ResponseEntity<String> getJsonPhotos(Principal principal) {
        Collection<PhotoInfo> photos = photoService.getPhotosForCurrentUser(principal.getName());
        String out = new Gson().toJson(photos);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/javascript");
        return new ResponseEntity<String>(out, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/photos", params = "format=xml")
    public ResponseEntity<String> getXmlPhotos(Principal principal) {
        Collection<PhotoInfo> photos = photoService.getPhotosForCurrentUser(principal.getName());
        StringBuilder out = new StringBuilder();
        out.append("<photos>");
        for (PhotoInfo photo : photos) {
            out.append(String.format("<photo id=\"%s\" name=\"%s\"/>", photo.getId(), photo.getName()));
        }
        out.append("</photos>");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/xml");
        return new ResponseEntity<String>(out.toString(), headers, HttpStatus.OK);
    }

    @RequestMapping("/photos/trusted/message")
    @PreAuthorize("#oauth2.clientHasRole('ROLE_CLIENT')")
    @ResponseBody
    public String getTrustedClientMessage(Principal principal) {
        return "Hello, Trusted Client" + (principal != null ? " " + principal.getName() : "");
    }

}
