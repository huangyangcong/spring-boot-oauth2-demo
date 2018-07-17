package com.luangeng.resource.service;

import com.luangeng.resource.vo.PhotoInfo;

import java.io.InputStream;
import java.util.List;

public interface PhotoService {

    List<PhotoInfo> getPhotosForCurrentUser(String username);

    InputStream loadPhoto(String id);
}
