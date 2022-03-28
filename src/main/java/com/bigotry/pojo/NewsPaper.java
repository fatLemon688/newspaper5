package com.bigotry.pojo;

import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

/**
 * @author ：JJHu
 * @date ：Created in 2020/12/2 16:33
 * @description：
 * @modified By：
 * @version: $
 */
@Component
public class NewsPaper {
    //报刊代号
    private String codeName;
    //报刊名称
    private String paperName;
    //出版报社
    private String pubOffice;
    //出版周期
    private Integer pubPeriod;
    //季度报价
    private Integer ssonPrice;
    //内容介绍
    private String introduction;
    //分类编号
    private String sortNo;

    private TbOrder tbOrder;
    public NewsPaper() {
    }

    public NewsPaper(String codeName, String paperName, String pubOffice, Integer pubPeriod, Integer ssonPrice, String introduction, String sortNo, TbOrder tbOrder) {
        this.codeName = codeName;
        this.paperName = paperName;
        this.pubOffice = pubOffice;
        this.pubPeriod = pubPeriod;
        this.ssonPrice = ssonPrice;
        this.introduction = introduction;
        this.sortNo = sortNo;
        this.tbOrder = tbOrder;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPubOffice() {
        return pubOffice;
    }

    public void setPubOffice(String pubOffice) {
        this.pubOffice = pubOffice;
    }

    public Integer getPubPeriod() {
        return pubPeriod;
    }

    public void setPubPeriod(Integer pubPeriod) {
        this.pubPeriod = pubPeriod;
    }

    public Integer getSsonPrice() {
        return ssonPrice;
    }

    public void setSsonPrice(Integer ssonPrice) {
        this.ssonPrice = ssonPrice;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public TbOrder getTbOrder() {
        return tbOrder;
    }

    public void setTbOrder(TbOrder tbOrder) {
        this.tbOrder = tbOrder;
    }

    @Override
    public String toString() {
        return "NewsPaper{" +
                "codeName='" + codeName + '\'' +
                ", paperName='" + paperName + '\'' +
                ", pubOffice='" + pubOffice + '\'' +
                ", pubPeriod=" + pubPeriod +
                ", ssonPrice=" + ssonPrice +
                ", introduction='" + introduction + '\'' +
                ", sortNo='" + sortNo + '\'' +
                ", tbOrder=" + tbOrder +
                '}';
    }
}
