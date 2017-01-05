package com.jia16.bean;

/**
 * 公告的bean
 */
public class Advices {
    /**
     * title :  “嘉石榴”第六十五期理财产品到期兑付公告（2016.12.5）
     * url : https://www.jia16.com/static/press/notices/notice-20161205.3.html?mgnlChannel=smartphone
     * type : 公告
     * date : 2016-12-5
     * totalPage : 25
     */

    private String title;
    private String url;
    private String type;
    private String date;
    private String totalPage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "Advices{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", totalPage='" + totalPage + '\'' +
                '}';
    }
}
