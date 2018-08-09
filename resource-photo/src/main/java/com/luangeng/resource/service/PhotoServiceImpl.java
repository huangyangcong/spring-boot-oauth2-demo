package com.luangeng.resource.service;

import com.luangeng.resource.vo.PhotoInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation for the photo service.
 */
@Service
public class PhotoServiceImpl implements PhotoService {

    private List<PhotoInfo> photos = new ArrayList<>();

    public PhotoServiceImpl() {
        photos.add(new PhotoInfo("1", "admin"));
        photos.add(new PhotoInfo("2", "admin"));
        photos.add(new PhotoInfo("3", "user"));
    }

    @Override
    public List<PhotoInfo> getPhotosForCurrentUser(String username) {
        ArrayList<PhotoInfo> infos = new ArrayList<PhotoInfo>();
        for (PhotoInfo info : photos) {
            if (username.equals(info.getUserId())) {
                infos.add(info);
            }
        }
        return infos;
    }

    @Override
    public InputStream loadPhoto(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails details = (UserDetails) authentication.getPrincipal();
            username = details.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = authentication.getPrincipal().toString();
        } else {
            return null;
        }
        for (PhotoInfo photoInfo : photos) {
            if (id.equals(photoInfo.getId()) && username.equals(photoInfo.getUserId())) {
                URL resourceURL = getClass().getResource(photoInfo.getResourceURL());
                if (resourceURL == null) {
                    continue;
                }
                try {
                    return resourceURL.openStream();
                } catch (IOException e) {
                    // fall through...
                }
            }
        }

        return null;
    }

}
