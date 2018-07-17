package com.luangeng.resource.vo;

public class PhotoInfo {

    private String id;
    private String resourceURL;
    private String name;
    private String userId;

    public PhotoInfo(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.name = "photo" + id + ".jpg";
        //jar包中的资源路径分隔符各操作系统都为/
        this.resourceURL = "/" + "img" + "/" + name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
