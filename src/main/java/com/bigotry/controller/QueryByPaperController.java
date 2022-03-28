package com.bigotry.controller;

import com.bigotry.Main;
import com.bigotry.handler.DragWindowHandler;
import com.bigotry.mapper.queryMapper;
import com.bigotry.pojo.NewsPaper;
import com.bigotry.pojo.User;
import com.bigotry.view.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;


import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：JJHu
 * @date ：Created in 2020/12/8 16:16
 * @description：
 * @modified By：
 * @version: $
 */
@FXMLController
public class QueryByPaperController implements Initializable {

    private double x = 0.00;
    private double y = 0.00;
    private double width = 0.00;
    private double height = 0.00;
    private boolean isMax = false;
    private double xOffset = 0, yOffset = 0;//自定义dialog移动横纵坐标
    @FXML
    private Button btnClose;
    @FXML
    private Button btnMin;

    private Stage stage;
    //对第几次打开修改报刊页面计数
    private Integer openModifyPaperPageCount = 0;
    @Autowired
    NewsPaper modifyNewsPaper;
    @Autowired
    QueryByPaperView queryByPaperView;
    @FXML
    private Label adminName;
    @FXML
    private ListView listView;
    @FXML
    private Label labelCodeName;
    @FXML
    private Label labelSsonPrice;
    @FXML
    private Label labelPaperName;
    @Autowired
    User user;
    @FXML
    private Label labelSortNo;
    @FXML
    private Label labelPubPeriod;
    @FXML
    private Label labelPubOffice;
    @FXML
    private TextArea paperContent;
    @FXML
    private Button btnDeletePaper;
    @FXML
    private Button btnModifyPaper;
    @Autowired
    queryMapper queryMapper;
    @Autowired
    NewsPaper newsPaper;
    @Autowired
    ModifyPaperController modifyPaperController;
    @FXML
    private ImageView adminMainImg;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        //加载图片文件
        File adminFile = new File("src/main/resources/images/adminLogin/admin3.jpg");
        setImageInit(adminFile , adminMainImg);
        adminName.setText(AdminLoginController.adminName);
        handleQueryInfoByPaper();
    }
    //设置图片初始化
    private void setImageInit(File file , ImageView imageView){
        String string = file.toURI().toString();
        Image image = new Image(string);
        imageView.setImage(image);
    }
    /**
     * 启动初始化组件
     */
    void init() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent parent = queryByPaperView.getView();
                QueryByPaperController.this.stage = (Stage) parent.getScene().getWindow();
                stage.setWidth(1179);
                stage.setHeight(752);
                stage.setResizable(false);

                //为按钮设置图标
                ImageView imageView = new ImageView(new Image(getClass().getResource( "/images/最小化图标1.png").toExternalForm()));
                btnMin.setGraphic(imageView);
                ImageView imageView1 = new ImageView(new Image(getClass().getResource( "/images/关闭.png").toExternalForm()));
                btnClose.setGraphic(imageView1);

                btnMin.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        stage.setIconified(true);
                    }
                });

                btnClose.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION,"确定要退出？",new ButtonType("取消", ButtonBar.ButtonData.NO),
                                new ButtonType("确定", ButtonBar.ButtonData.YES));
                        //设置窗口的标题
                        _alert.setTitle("确认");
                        //设置对话框的 icon 图标，参数是主窗口的 stage
                        _alert.initOwner(stage);
                        //showAndWait() 将在对话框消失以前不会执行之后的代码
                        Optional<ButtonType> _buttonType = _alert.showAndWait();
                        //根据点击结果返回
                        if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
                            stage.close();
                        }else{
                            Main.showView(QueryByPaperView.class);
                        }
                    }
                });
                stage.xProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (newValue != null && !isMax) {
                            x = newValue.doubleValue();
                        }
                    }
                });
                stage.yProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (newValue != null && !isMax) {
                            y = newValue.doubleValue();
                        }
                    }
                });
                stage.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (newValue != null && !isMax) {
                            width = newValue.doubleValue();
                        }
                    }
                });
                stage.heightProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (newValue != null && !isMax) {
                            height = newValue.doubleValue();
                        }
                    }
                });

                DragWindowHandler handler = new DragWindowHandler(stage);
                // 鼠标按下
                parent.setOnMousePressed(handler);
                // 鼠标拖动
                parent.setOnMouseDragged(handler);

                //鼠标点击获取横纵坐标
                parent.setOnMousePressed(event -> {
                    event.consume();
                    xOffset = event.getSceneX();
                    if (event.getSceneY() > 46) {
                        yOffset = 0;
                    } else {
                        yOffset = event.getSceneY();
                    }
                });
            }
        });
    }

    //退出登录
    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        //切换回登录界面
        stage.setWidth(472);
        stage.setHeight(445);
        stage.setResizable(false);
        Main.showView(UserLoginView.class);
    }

    //录入新用户信息
    @FXML
    public void handleEnterUserInfo(ActionEvent actionEvent) {
        //平面
        AnchorPane ap = new AnchorPane();
        ap.setStyle("-fx-background-color:#ffffff");
        ap.setPrefWidth(371);
        ap.setPrefHeight(504);


        final Label label1 = new Label();
        label1.setText("用户ID：");
        label1.setPrefWidth(88);
        label1.setPrefHeight(32);
        label1.setLayoutX(54);
        label1.setLayoutY(38);

        final TextField textFieldUserId = new TextField();
        textFieldUserId.setPromptText("请输入用户ID...");
        textFieldUserId.setPrefWidth(200);
        textFieldUserId.setPrefHeight(32);
        textFieldUserId.setLayoutX(150);
        textFieldUserId.setLayoutY(38);

        Label label2 = new Label();
        label2.setText("密码：");
        label2.setPrefWidth(88);
        label2.setPrefHeight(32);
        label2.setLayoutX(50);
        label2.setLayoutY(81);

        final PasswordField passwordFieldFirst = new PasswordField();
        passwordFieldFirst.setPromptText("请输入密码...");
        passwordFieldFirst.setPrefWidth(200);
        passwordFieldFirst.setPrefHeight(32);
        passwordFieldFirst.setLayoutX(150);
        passwordFieldFirst.setLayoutY(82);

        Label label3 = new Label();
        label3.setText("确认密码：");
        label3.setPrefWidth(120);
        label3.setPrefHeight(32);
        label3.setLayoutX(30);
        label3.setLayoutY(123);

        final PasswordField passwordFieldSecond = new PasswordField();
        passwordFieldSecond.setPromptText("请再次输入确认密码...");
        passwordFieldSecond.setPrefWidth(200);
        passwordFieldSecond.setPrefHeight(32);
        passwordFieldSecond.setLayoutX(150);
        passwordFieldSecond.setLayoutY(124);

        Label label4 = new Label();
        label4.setText("姓名：");
        label4.setPrefWidth(88);
        label4.setPrefHeight(32);
        label4.setLayoutX(78);
        label4.setLayoutY(165);

        final TextField textFieldName = new TextField();
        textFieldName.setPromptText("请输入姓名...");
        textFieldName.setPrefWidth(200);
        textFieldName.setPrefHeight(32);
        textFieldName.setLayoutX(150);
        textFieldName.setLayoutY(166);

        Label label5 = new Label();
        label5.setText("身份证号：");
        label5.setPrefWidth(120);
        label5.setPrefHeight(32);
        label5.setLayoutX(30);
        label5.setLayoutY(211);

        final TextField  textFieldIDCard = new TextField();
        textFieldIDCard.setPromptText("请输入身份证号...");
        textFieldIDCard.setPrefWidth(200);
        textFieldIDCard.setPrefHeight(32);
        textFieldIDCard.setLayoutX(150);
        textFieldIDCard.setLayoutY(212);

        Label label6 = new Label();
        label6.setText("手机号：");
        label6.setPrefWidth(96);
        label6.setPrefHeight(32);
        label6.setLayoutX(50);
        label6.setLayoutY(252);

        final TextField textFieldPhone = new TextField();
        textFieldPhone.setPromptText("请输入手机号...");
        textFieldPhone.setPrefWidth(200);
        textFieldPhone.setPrefHeight(32);
        textFieldPhone.setLayoutX(150);
        textFieldPhone.setLayoutY(253);

        Label label7 = new Label();
        label7.setText("地址：");
        label7.setPrefWidth(88);
        label7.setPrefHeight(32);
        label7.setLayoutX(74);
        label7.setLayoutY(296);

        final TextField textFieldAddress = new TextField();
        textFieldAddress.setPromptText("请输入地址...");
        textFieldAddress.setPrefWidth(200);
        textFieldAddress.setPrefHeight(32);
        textFieldAddress.setLayoutX(150);
        textFieldAddress.setLayoutY(297);

        Label label8 = new Label();
        label8.setText("部门号：");
        label8.setPrefWidth(96);
        label8.setPrefHeight(32);
        label8.setLayoutX(50);
        label8.setLayoutY(335);

        final TextField textFieldDeptNo = new TextField();
        textFieldDeptNo.setPromptText("请输入部门号...");
        textFieldDeptNo.setPrefWidth(200);
        textFieldDeptNo.setPrefHeight(32);
        textFieldDeptNo.setLayoutX(150);
        textFieldDeptNo.setLayoutY(336);

        Separator separator = new Separator();
        separator.setPrefWidth(339);
        separator.setPrefHeight(6);
        separator.setLayoutX(16);
        separator.setLayoutY(396);

        final Button btnSummit = new Button();
        btnSummit.setText("提交");
        btnSummit.setPrefWidth(286);
        btnSummit.setPrefHeight(30);
        btnSummit.setLayoutX(43);
        btnSummit.setLayoutY(420);

        //把元素加到版上，很重要！！！
        ap.getChildren().addAll(label1,label2,label3,label4,label5,label6,label7,label8,
                textFieldUserId,passwordFieldFirst,passwordFieldSecond,btnSummit,
                textFieldAddress,textFieldDeptNo,textFieldIDCard,textFieldName,textFieldPhone,separator);

        //设置舞台和场景
        Stage s = new Stage();

        ImageView iv = new ImageView();
        s.getIcons().add(new Image(getClass().getResource( "/images/bluesea.gif").toExternalForm()));
        s.setResizable(false);
        Scene sc = new Scene(ap);
        s.setScene(sc);

        //设置所有者
        s.initOwner(stage);
        s.initModality(Modality.WINDOW_MODAL);
        s.setX(MouseInfo.getPointerInfo().getLocation().x);
        s.setY(MouseInfo.getPointerInfo().getLocation().y);
        s.show();

        //用户点击提交后触发
        btnSummit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String userIdStr = textFieldUserId.getText().trim();
                String passwordStrFirst = passwordFieldFirst.getText().trim();
                String passwordStrSecond = passwordFieldSecond.getText().trim();
                String nameStr = textFieldName.getText().trim();
                String IDCardStr = textFieldIDCard.getText().trim();
                String phoneStr = textFieldPhone.getText().trim();
                String addressStr = textFieldAddress.getText().trim();
                String deptStr = textFieldDeptNo.getText().trim();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("信息");
                String str = "";

                if ("".equals(userIdStr)|| "".equals(passwordStrFirst)
                        ||"".equals(nameStr) || "".equals(IDCardStr)
                        || "".equals(deptStr) || "".equals(passwordStrSecond)) {
                    str = "电话和地址可以为空外其他字段不能为空！";
                }
                //如果提示字符串str为空，但身份证不是18位或两次密码输入不同，弹出错误信息。
                if ("".equals(str)) {//如果输入的userId不是纯数字
                    if(isNotNumeric(userIdStr)){
                        String str2 = "用户ID必须是纯数字！";
                        alert.setContentText(str2);
                        alert.showAndWait();
                        return;
                    }
                    if(!passwordStrFirst.equals(passwordStrSecond)) {
                        str = "两次密码输入不相同，请重新输入！";
                        alert.setContentText(str);
                        alert.showAndWait();
                        return;
                    }
                    if(IDCardStr.length()!=18) {
                        String str3 = "身份证号只能为18位！";
                        alert.setContentText(str3);
                        alert.showAndWait();
                        return;
                    }
                    //判断部门号是否纯数字
                    if(isNotNumeric(deptStr)){
                        String str4 = "部门号必须是纯数字！";
                        alert.setContentText(str4);
                        alert.showAndWait();
                        return;
                    }
                    //判断当前输入的userID在数据库中是否已经存在
                    User userExist = queryMapper.queryUserInfoById(userIdStr);
                    if(userExist!=null){
                        str = "当前用户ID已经存在，请重新填写用户ID！";
                        alert.setContentText(str);
                        alert.showAndWait();
                        return;
                    }
                    //判断输入的部门号在数据库中是否存在
                    boolean deptNoIsExist = queryMapper.queryDeptNoIsExist(Integer.valueOf(deptStr));
                    if (!deptNoIsExist){//如果输入的部门号不存在
                        String str5 = "输入的部门号不存在！部门号为21-25！";
                        alert.setContentText(str5);
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
                user.setUserId(userIdStr);
                user.setPassword(passwordStrFirst);
                user.setName(nameStr);
                user.setIDCard(IDCardStr);
                user.setPhone(phoneStr);
                user.setAddress(addressStr);
                user.setDeptNo(Integer.valueOf(deptStr));
                int result = queryMapper.insertIntoUser(user);
                if (result > 0) {
                    str = "录入用户信息成功！";
                    alert.setContentText(str);
                    alert.showAndWait();
                    s.close();
                }else{
                    str = "录入用户信息失败，请重新填写！";
                    alert.setContentText(str);
                    alert.showAndWait();
                }
            }
        });

        btnSummit.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btnSummit.setCursor(Cursor.HAND);
            }
        });
    }

    //录入新报刊
    @FXML
    public void handleEnterPaperInfo(ActionEvent actionEvent) {
        //平面
        AnchorPane ap = new AnchorPane();
        ap.setStyle("-fx-background-color:#ffffff");
        ap.setPrefWidth(965);
        ap.setPrefHeight(589);

        //User userInfo = queryMapper.queryUserInfoById(LoginController.userId);

        final Label label1 = new Label();
        label1.setText("报刊代号：");
        label1.setFont(javafx.scene.text.Font.font(24));
        label1.setPrefWidth(120);
        label1.setPrefHeight(32);
        label1.setLayoutX(30);
        label1.setLayoutY(47);

        final TextField textFieldCodeName = new TextField();
        textFieldCodeName.setPromptText("请输入报刊代号...");
        textFieldCodeName.setPrefWidth(200);
        textFieldCodeName.setPrefHeight(32);
        textFieldCodeName.setLayoutX(147);
        textFieldCodeName.setLayoutY(47);

        final Label label2 = new Label();
        label2.setText("报刊名称：");
        label2.setFont(javafx.scene.text.Font.font(24));
        label2.setPrefWidth(120);
        label2.setPrefHeight(32);
        label2.setLayoutX(30);
        label2.setLayoutY(117);

        final TextField textFieldPaperName = new TextField();
        textFieldPaperName.setPromptText("请输入报刊名称...");
        textFieldPaperName.setPrefWidth(200);
        textFieldPaperName.setPrefHeight(32);
        textFieldPaperName.setLayoutX(147);
        textFieldPaperName.setLayoutY(117);

        Label label3 = new Label();
        label3.setText("出版社：");
        label3.setFont(javafx.scene.text.Font.font(24));
        label3.setPrefWidth(120);
        label3.setPrefHeight(32);
        label3.setLayoutX(58);
        label3.setLayoutY(187);

        final TextField textFieldPubOffice = new TextField();
        textFieldPubOffice.setPromptText("请输入出版社...");
        textFieldPubOffice.setPrefWidth(200);
        textFieldPubOffice.setPrefHeight(32);
        textFieldPubOffice.setLayoutX(147);
        textFieldPubOffice.setLayoutY(187);

        Label label4 = new Label();
        label4.setText("出版周期(天):");
        label4.setFont(javafx.scene.text.Font.font(24));
        label4.setPrefWidth(160);
        label4.setPrefHeight(32);
        label4.setLayoutX(2);
        label4.setLayoutY(257);

        final TextField textFieldPubPeriod = new TextField();
        textFieldPubPeriod.setPromptText("请输入出版周期...");
        textFieldPubPeriod.setPrefWidth(200);
        textFieldPubPeriod.setPrefHeight(32);
        textFieldPubPeriod.setLayoutX(147);
        textFieldPubPeriod.setLayoutY(257);

        Label label5 = new Label();
        label5.setText("季度报价(元):");
        label5.setFont(javafx.scene.text.Font.font(24));
        label5.setPrefWidth(160);
        label5.setPrefHeight(32);
        label5.setLayoutX(2);
        label5.setLayoutY(327);

        final TextField textFieldSsonPrice = new TextField();
        textFieldSsonPrice.setPromptText("请输入季度报价...");
        textFieldSsonPrice.setPrefWidth(200);
        textFieldSsonPrice.setPrefHeight(32);
        textFieldSsonPrice.setLayoutX(147);
        textFieldSsonPrice.setLayoutY(327);

        Label label6 = new Label();
        label6.setText("分类编号：");
        label6.setFont(javafx.scene.text.Font.font(24));
        label6.setPrefWidth(120);
        label6.setPrefHeight(32);
        label6.setLayoutX(30);
        label6.setLayoutY(397);

        final TextField  textFieldSortNo = new TextField();
        textFieldSortNo.setPromptText("请输入分类编号...");
        textFieldSortNo.setPrefWidth(200);
        textFieldSortNo.setPrefHeight(32);
        textFieldSortNo.setLayoutX(147);
        textFieldSortNo.setLayoutY(397);


        Label label7 = new Label();
        label7.setText("内容介绍：");
        label7.setFont(javafx.scene.text.Font.font(24));
        label7.setPrefWidth(120);
        label7.setPrefHeight(32);
        label7.setLayoutX(382);
        label7.setLayoutY(47);

        final TextArea paperContent = new TextArea();
        paperContent.setEditable(true);
        paperContent.setPromptText("请输入报刊简介...");
        paperContent.setPrefWidth(584);
        paperContent.setPrefHeight(414);
        paperContent.setLayoutX(382);
        paperContent.setLayoutY(82);


        Separator separator = new Separator();
        separator.setPrefWidth(891);
        separator.setPrefHeight(6);
        separator.setLayoutX(36);
        separator.setLayoutY(512);

        final Button btnSummit = new Button();
        btnSummit.setText("提交");
        btnSummit.setPrefWidth(286);
        btnSummit.setPrefHeight(30);
        btnSummit.setLayoutX(363);
        btnSummit.setLayoutY(535);

        //把元素加到版上，很重要！！！
        ap.getChildren().addAll(label1,label2,label3,label4,label5,label6,label7,
                textFieldCodeName,textFieldPaperName,textFieldPubOffice,textFieldPubPeriod,
                textFieldSortNo,textFieldSsonPrice,paperContent,btnSummit,separator);

        //设置舞台和场景
        Stage s = new Stage();

        ImageView iv = new ImageView();
        s.getIcons().add(new Image(getClass().getResource( "/images/bluesea.gif").toExternalForm()));
        s.setResizable(false);
        Scene sc = new Scene(ap);
        s.setScene(sc);

        //设置所有者
        s.initOwner(stage);
        s.initModality(Modality.WINDOW_MODAL);
        s.setX(MouseInfo.getPointerInfo().getLocation().x);
        s.setY(MouseInfo.getPointerInfo().getLocation().y);
        s.show();

        //用户点击提交后触发
        btnSummit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String codeNameStr = textFieldCodeName.getText().trim();
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
                    //报刊代号已经存在,弹出错误信息
                    NewsPaper paperInfo = queryMapper.queryPaperByCodeName(codeNameStr);
                    if(paperInfo != null){
                        String str7 = "该报刊代号已经存在！";
                        alert.setContentText(str7);
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
                int result = queryMapper.insertIntoNewsPaper(newsPaper);
                if (result > 0) {
                    str = "录入新报刊成功！";
                    alert.setContentText(str);
                    alert.showAndWait();
                    s.close();
                }else{
                    str = "录入新报刊失败，请重新填写！";
                    alert.setContentText(str);
                    alert.showAndWait();
                }
            }
        });

        btnSummit.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btnSummit.setCursor(Cursor.HAND);
            }
        });

    }
    //按人员查询基本信息
    @FXML
    public void handleQueryInfoByPerson() {
        stage.setWidth(1125);
        stage.setHeight(705);
        stage.setResizable(false);
        Main.showView(QueryByPersonView.class);
    }

    //按报刊查询基本信息
    @FXML
    public void handleQueryInfoByPaper() {
        //定义列表的数据
        ObservableList<String> items = FXCollections.observableArrayList();
        //获取所有报刊
        List<NewsPaper> papers  = queryMapper.queryALLNewsPaper();
        //遍历所有报刊并获取当前报刊的名称，并存入ListView
        for (NewsPaper paper:papers) {
            items.add(paper.getPaperName());
        }
        listView.setItems(items);
        //为ListView的选中项添加监听事件
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            //ListView选中项的监听器类，一旦选中某项，就展示该报刊的内容，价格
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String paperName = (String) listView.getSelectionModel().getSelectedItem();
                NewsPaper paper = queryMapper.queryNewsPaperByName(paperName);
                if(paper!=null){
                    labelCodeName.setText(paper.getCodeName());
                    labelSsonPrice.setText(paper.getSsonPrice().toString());
                    labelPaperName.setText(paperName);
                    labelSortNo.setText(paper.getSortNo());
                    labelPubPeriod.setText(paper.getPubPeriod().toString());
                    labelPubOffice.setText(paper.getPubOffice());
                    //TextArea设置自动换行
                    paperContent.setWrapText(true);
                    paperContent.setText(paper.getIntroduction());
                }
            }
        });
    }

    //按部门查询基本信息
    @FXML
    public void handleQueryInfoByDept(ActionEvent actionEvent) {
        stage.setWidth(727);
        stage.setHeight(760);
        stage.setResizable(false);
        Main.showView(QueryByDeptNoView.class);
    }

    //按用户查询订阅情况
    @FXML
    public void handleQuerySubScriptedByUser(ActionEvent actionEvent) {
        stage.setWidth(999);
        stage.setHeight(760);
        stage.setResizable(false);
        Main.showView(QuerySubScriptedByUserView.class);
    }

    //按报刊查询订阅情况
    @FXML
    public void handleQuerySubScriptedByPaper(ActionEvent actionEvent) {
        stage.setWidth(707);
        stage.setHeight(760);
        stage.setResizable(false);
        Main.showView(QuerySubScriptedByPaperView.class);
    }

    //按部门查询订阅情况
    @FXML
    public void handleQuerySubScriptedByDept(ActionEvent actionEvent) {
        stage.setWidth(898);
        stage.setHeight(760);
        stage.setResizable(false);
        Main.showView(QuerySubScriptedByDeptView.class);
    }


    public boolean isNotNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( isNum.matches() ){
            return false;
        }
        return true;
    }

    //修改报刊
    @FXML
    public void handleModifyPaper(ActionEvent actionEvent) {
        String paperName = (String) listView.getSelectionModel().getSelectedItem();
        modifyNewsPaper=null;
        modifyNewsPaper = queryMapper.queryNewsPaperByName(paperName);
        if(modifyNewsPaper!=null){
            //第一次打开修改报刊的页面，就先初始化
            if(openModifyPaperPageCount<1){
                Main.showView(ModifyPaperView.class,Modality.WINDOW_MODAL);
                modifyPaperController.fillData();
                //打开次数加一
                openModifyPaperPageCount++;
            }else{
                modifyPaperController.fillData();
                Main.showView(ModifyPaperView.class,Modality.WINDOW_MODAL);
            }

        }else{
            String str = "";
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setHeaderText("信息");
            str = "请先选择要修改的报刊！";
            alertInfo.setContentText(str);
            alertInfo.showAndWait();
        }
    }

    //删除报刊
    @FXML
    public void handleDeletePaper(ActionEvent actionEvent) {
        String paperName = (String) listView.getSelectionModel().getSelectedItem();
        NewsPaper paperInfo = queryMapper.queryNewsPaperByName(paperName);
        String str = "";

        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
        alertInfo.setHeaderText("信息");
        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION, "确定要删除该报刊？", new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("确定", ButtonBar.ButtonData.YES));

        if(paperInfo!=null) {
            //设置窗口的标题
            _alert.setTitle("确认");
            //设置对话框的 icon 图标，参数是主窗口的 stage
            _alert.initOwner(stage);
            //showAndWait() 将在对话框消失以前不会执行之后的代码
            Optional<ButtonType> _buttonType = _alert.showAndWait();
            //根据点击结果返回
            if (_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)) {
                try{
                    Integer deleteResult = queryMapper.deletePaperByPaperName(paperName);
                    if (deleteResult != null) {
                        if (deleteResult > 0) {
                            str = "删除报刊成功!";
                            alertInfo.setContentText(str);
                            alertInfo.showAndWait();
                            //返回按报刊查询基本信息的首页
                            flushUI();
                            Main.showView(QueryByPaperView.class);
                        } else {
                            str = "删除报刊失败!请重新操作!";
                            alertInfo.setContentText(str);
                            alertInfo.showAndWait();
                        }
                    }
                }catch (Exception e){
                    str = "该报刊已包含订单，不可删除!";
                    alertInfo.setContentText(str);
                    alertInfo.showAndWait();
                }

            } else {
                Main.showView(QueryByPaperView.class);
            }
        }else{
            str = "请先选择要删除的报刊！";
            alertInfo.setContentText(str);
            alertInfo.showAndWait();
        }




    }

    //刷新界面
    public void flushUI(){
        //定义列表的数据
        ObservableList<String> items = FXCollections.observableArrayList();
        //获取所有报刊
        List<NewsPaper> papers  = queryMapper.queryALLNewsPaper();
        //遍历所有报刊并获取当前报刊的名称，并存入ListView
        for (NewsPaper paper:papers) {
            items.add(paper.getPaperName());
        }
        listView.setItems(items);
        labelCodeName.setText("");
        labelSsonPrice.setText("");
        labelPaperName.setText("");
        labelSortNo.setText("");
        labelPubPeriod.setText("");
        labelPubOffice.setText("");
        paperContent.setText("");
    }

    public NewsPaper getModifyNewsPaper(){
        String paperName = (String) listView.getSelectionModel().getSelectedItem();
        modifyNewsPaper=null;
        modifyNewsPaper = queryMapper.queryNewsPaperByName(paperName);
        return modifyNewsPaper;
    }
}
