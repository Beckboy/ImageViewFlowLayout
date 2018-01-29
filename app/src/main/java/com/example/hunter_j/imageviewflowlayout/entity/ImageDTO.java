package com.example.hunter_j.imageviewflowlayout.entity;

import java.io.Serializable;

/**
 * Created by hunter_J on 2017/10/26.
 */

public class ImageDTO implements Serializable{

    private String path;
    private int local_drawable = 0;
    private boolean isSelect = false;
    private boolean isLocal = false;
    private String qiniu_path;
    private int width;
    private int height;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public String getQiniu_path() {
        return qiniu_path;
    }

    public void setQiniu_path(String qiniu_path) {
        this.qiniu_path = qiniu_path;
    }

    public int getLocal_drawable() {
        return local_drawable;
    }

    public void setLocal_drawable(int local_drawable) {
        this.local_drawable = local_drawable;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
