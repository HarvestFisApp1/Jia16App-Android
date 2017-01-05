package com.jia16.bean;

/**
 * 首页bunner图的bean
 */
public class Bunner {

    /**
     * title : null
     * url :  recommendPage
     * type : https://www.jia16.com/static/dam/jcr:dcf2316e-1909-49d9-9eda-d33364886316/20161222appbanner.jpg
     * date : null
     * totalPage : null
     */

    private Object title;
    private String url;
    private String type;
    private Object date;
    private Object totalPage;

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public Object getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Object totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "Bunner{" +
                "title=" + title +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", totalPage=" + totalPage +
                '}';
    }
}
