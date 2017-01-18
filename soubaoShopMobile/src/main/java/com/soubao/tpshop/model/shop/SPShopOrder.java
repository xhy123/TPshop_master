package com.soubao.tpshop.model.shop;

import com.soubao.tpshop.model.SPModel;

/**
 * Created by admin on 2016/6/18.
 */
public class SPShopOrder implements SPModel {
   /**
    *  排序字段
    */
    private String sortName;
    private String sortAsc;    //升序或降序
    private  String defaultHref;//默认排序URL
    private String saleSumHref;//销量排序
    private String priceHref;  //价格排序URL
    private String commentCountHref;//评论数量
    private String newsHref;    //新品数量

    @Override
    public String[] replaceKeyFromPropertyName() {
        return new String[]{
            "sortName","sort",
            "sortAsc","sort_asc",
            "defaultHref","orderby_default",
            "saleSumHref","orderby_sales_sum",
            "priceHref","orderby_price",
            "commentCountHref","orderby_comment_count",
            "newsHref","orderby_is_new"
        };
    }


    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortAsc() {
        return sortAsc;
    }

    public void setSortAsc(String sortAsc) {
        this.sortAsc = sortAsc;
    }

    public String getDefaultHref() {
        return defaultHref;
    }

    public void setDefaultHref(String defaultHref) {
        this.defaultHref = defaultHref;
    }

    public String getSaleSumHref() {
        return saleSumHref;
    }

    public void setSaleSumHref(String saleSumHref) {
        this.saleSumHref = saleSumHref;
    }

    public String getPriceHref() {
        return priceHref;
    }

    public void setPriceHref(String priceHref) {
        this.priceHref = priceHref;
    }

    public String getCommentCountHref() {
        return commentCountHref;
    }

    public void setCommentCountHref(String commentCountHref) {
        this.commentCountHref = commentCountHref;
    }

    public String getNewsHref() {
        return newsHref;
    }

    public void setNewsHref(String newsHref) {
        this.newsHref = newsHref;
    }
}
