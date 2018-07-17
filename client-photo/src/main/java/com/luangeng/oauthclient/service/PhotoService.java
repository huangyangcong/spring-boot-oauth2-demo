package com.luangeng.oauthclient.service;

import java.io.InputStream;
import java.util.List;

public interface PhotoService {

    List<String> getPhotoIds();

    InputStream loadPhoto(String id);

}
