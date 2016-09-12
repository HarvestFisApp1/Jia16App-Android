package com.jia16.bean;

import java.io.Serializable;

/**
 * Created by huangjun on 16/8/29.
 */
public class Version implements Serializable {


    /**
     * type : android
     * versionId : 2016082911220911
     * versionName : v1.1
     * ifUpdate : yes
     * descr : 嘉石榴APP v1.1
     * isUsed : yes
     * publishTime : 20160829120101
     * publisher : admin
     * modifyTime : 20160824112203
     * modifyBy : admin
     * url : http://test2.jia16.com/apk/jia16-0829.apk
     */

    private String type;
    private int versionId;
    private String versionName;
    private String ifUpdate;
    private String descr;
    private String isUsed;
    private String publishTime;
    private String publisher;
    private String modifyTime;
    private String modifyBy;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getIfUpdate() {
        return ifUpdate;
    }

    public void setIfUpdate(String ifUpdate) {
        this.ifUpdate = ifUpdate;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
