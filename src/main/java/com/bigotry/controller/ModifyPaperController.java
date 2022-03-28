package com.bigotry.controller;

import com.bigotry.mapper.queryMapper;
import com.bigotry.pojo.NewsPaper;
import com.bigotry.view.ModifyPaperView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：JJHu
 * @date ：Created in 2020/12/12 16:35
 * @description：
 * @modified By：
 * @version: $
 */
@FXMLController
public class ModifyPaperController implements Initializable {
    private Stage stage;
    @Autowired
    NewsPaper paperInfo;
    @Autowired
    ModifyPaperView modifyPaperView;
    @Autowired
    NewsPaper newsPaper;
    @FXML
    private Label labelCodeName;
    @FXML
    private TextArea paperContent;
    @FXML
    private TextField textFieldPaperName;
    @FXML
    private TextField textFieldPubOffice;
    @FXML
    private TextField textFieldPubPeriod;
    @FXML
    private TextField textFieldSsonPrice;
    @FXML
    private TextField textFieldSortNo;
    @Autowired
    queryMapper queryMapper;
    @Autowired
    QueryByPaperController queryByPaperController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //init();
        fillData();
    }
    /**
     * 启动初始化组件
     */
    void init() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent parent = modifyPaperView.getView();
                ModifyPaperController.this.stage = (Stage) parent.getScene().getWindow();
                stage.setWidth(965);
                stage.setHeight(589);
                stage.setResizable(false);
            }
        });
    }

    public void fillData(){

        if(queryByPaperController.getModifyNewsPaper()!=null){
            paperInfo = queryByPaperController.getModifyNewsPaper();
            labelCodeName.setText(paperInfo.getCodeName());
            textFieldPaperName.setText(paperInfo.getPaperName());
            textFieldPubOffice.setText(paperInfo.getPubOffice());
            textFieldPubPeriod.setText(paperInfo.getPubPeriod().toString());
            textFieldSortNo.setText(paperInfo.getSortNo());
            textFieldSsonPrice.setText(paperInfo.getSsonPrice().toString());
            paperContent.setText(paperInfo.getIntroduction());
            paperContent.setWrapText(true);
        }
    }


    @FXML
    public void handleSummit() {
        if(queryByPaperController.getModifyNewsPaper()!=null){
            paperInfo = queryByPaperController.getModifyNewsPaper();

            String codeNameStr = labelCodeName.getText().trim();
            String paperNameStr = textFieldPaperName.getText().trim();
            String pubOfficeStr = textFieldPubOffice.getText().trim();
            String pubPeriodStr = textFieldPubPeriod.getText().trim();
            String sortNoStr = textFieldSortNo.getText().trim();
            String ssonPriceStr = textFieldSsonPrice.getText().trim();
            String paperContentStr = paperContent.getText().trim();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("信息");
            String str = "";
            if ("".equals(codeNameStr)|| "".equals(paperNameStr)
                    ||"".equals(pubOfficeStr) || "".equals(pubPeriodStr)
                    || "".equals(sortNoStr) || "".equals(ssonPriceStr)
                    ||"".equals(paperContentStr)) {
                str = "全部内容都不能为空！";
            }
            //如果提示字符串str为空
            if ("".equals(str)) {
                //报刊代号不是9位，弹出错误信息。
                if(codeNameStr.length()!=9) {
                    String str2 = "报刊代号只能为9位！";
                    alert.setContentText(str2);
                    alert.showAndWait();
                    return;
                }
                //出版周期不是纯数字，弹出错误信息
                if(isNotNumeric(pubPeriodStr)){
                    String str3 = "出版周期只能是数字！";
                    alert.setContentText(str3);
                    alert.showAndWait();
                    return;
                }
                //季度报价不是纯数字，弹出错误信息
                if(isNotNumeric(ssonPriceStr)){
                    String str4 = "季度报价只能是数字！";
                    alert.setContentText(str4);
                    alert.showAndWait();
                    return;
                }
                //分类编号不是纯数字，弹出错误信息
                if(isNotNumeric(sortNoStr)){
                    String str5 = "分类编号只能是数字！";
                    alert.setContentText(str5);
                    alert.showAndWait();
                    return;
                }
                //判断输入的分类编号数据库中是否存在
                boolean sortNoExists = queryMapper.querySortNoIsExist(Integer.valueOf(sortNoStr));
                //如果输入的分类编号不存在
                if(!sortNoExists){
                    String str6 = "输入的分类编号不存在！分类编号为11-17！";
                    alert.setContentText(str6);
                    alert.showAndWait();
                    return;
                }
            }else{
                //如果提示字符串有内容则说明用户填写错误，弹出错误信息并结束。
                alert.setContentText(str);
                alert.showAndWait();
                return;
            }

            //插入数据
            newsPaper.setCodeName(codeNameStr);
            newsPaper.setPaperName(paperNameStr);
            newsPaper.setPubOffice(pubOfficeStr);
            newsPaper.setPubPeriod(Integer.valueOf(pubPeriodStr));
            newsPaper.setSsonPrice(Integer.valueOf(ssonPriceStr));
            newsPaper.setIntroduction(paperContentStr);
            newsPaper.setSortNo(sortNoStr);
            int result = queryMapper.updatePaper(newsPaper);
            if (result > 0) {
                str = "修改报刊信息成功！";
                alert.setContentText(str);
                alert.showAndWait();
            }else{
                str = "修改报刊信息失败，请重新填写！";
                alert.setContentText(str);
                alert.showAndWait();
            }
        }
    }

    public boolean isNotNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( isNum.matches() ){
            return false;
        }
        return true;
    }
}
