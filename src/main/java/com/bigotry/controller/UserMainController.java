package com.bigotry.controller;

import com.bigotry.Main;
import com.bigotry.handler.DragWindowHandler;
import com.bigotry.mapper.queryMapper;
import com.bigotry.pojo.NewsPaper;
import com.bigotry.pojo.TbOrder;
import com.bigotry.pojo.User;
import com.bigotry.view.OrderManageView;
import com.bigotry.view.UserLoginView;
import com.bigotry.view.UserMainView;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
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
 * @date ：Created in 2020/11/29 15:56
 * @description：
 * @modified By：
 * @version: $
 */
@FXMLController
public class UserMainController implements Initializable {

    private static Integer openOrderManageCount = 0;

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

    private   Stage stage;
    @FXML
    private Label userName;
    @FXML
    private MenuItem itemModifyProfile;
    @FXML
    private MenuItem itemLogout;
    @FXML
    private MenuItem itemSubscriptPaper;
    @FXML
    private MenuItem itemQuerySubScripted;
    @FXML
    private  TextArea paperContent;
    @FXML
    private  ListView paperListView;
    @FXML
    private  Label labelSsonPrice;
    @Autowired
    TbOrder order;
    @Autowired
    User user;
    @Autowired
    private UserMainView userMainView;
    @Autowired
    OrderManageController orderManageController;
    @Autowired
    queryMapper queryMapper;
    @Autowired
    LoginController loginController;
    @FXML
    private ImageView userMainImg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        //加载图片文件
        File userMainFile = new File("src/main/resources/images/userMain/userMain.jpg");
        setImageInit(userMainFile , userMainImg);
        userName.setText(loginController.getLabelUserName());
        handleShowPaper();
    }

    //设置图片初始化
    private void setImageInit(File file , ImageView imageView){
        String string = file.toURI().toString();
        Image image = new Image(string);
        imageView.setImage(image);
    }
    /*修改个人信息*/
    @FXML
    private void handleModifyProfile() {
        modifyProfile();
    }
    /*退出登录*/
    @FXML
    private void handleLogout(ActionEvent actionEvent) {
        //切换回登录界面
        stage.setWidth(472);
        stage.setHeight(445);
        stage.setResizable(false);
        Main.showView(UserLoginView.class);

    }
    /*展示所有报刊*/
    public  void handleShowPaper() {
        //定义列表的数据
        ObservableList<String> items = FXCollections.observableArrayList();
        //获取所有报刊
        List<NewsPaper> papers  = queryMapper.queryALLNewsPaper();
        //遍历所有报刊并获取当前报刊的名称，并存入ListView
        for (NewsPaper paper:papers) {
            items.add(paper.getPaperName());
        }
        paperListView.setItems(items);
        //为ListView的选中项添加监听事件
        paperListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            //ListView选中项的监听器类，一旦选中某项，就展示该报刊的内容，价格
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String paperName = (String) paperListView.getSelectionModel().getSelectedItem();
                if(!"".equals(paperName)){
                    String content = queryMapper.queryNewsPaperByName(paperName).getIntroduction();
                    Integer price = queryMapper.queryNewsPaperByName(paperName).getSsonPrice();
                    //TextArea设置自动换行
                    paperContent.setWrapText(true);
                    paperContent.setText(content);
                    labelSsonPrice.setText(price.toString());
                }
            }
        });

    }


    //订阅报刊，按钮事件：启动子窗口，填写订单
    @FXML
    public void handleSubscriptPaper(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("信息");

        String str = "";
        if(paperListView.getSelectionModel().getSelectedItem()!=null){
            OnBtnShowChild();
        }else{
            str="请先选择报刊！";
            alert.setContentText(str);
            alert.showAndWait();
        }


    }
    /*查询已订阅的报刊*/
    @FXML
    private void handleQuerySubScripted(ActionEvent actionEvent) {
        stage.setWidth(728);
        stage.setHeight(596);
        stage.setResizable(false);
        //如果是第一次打开订单管理界面，就先初始化
        if(openOrderManageCount<1){
            Main.showView(OrderManageView.class);
            openOrderManageCount++;
        }else{
            //如果已经初始化了
            //刷新数据
            orderManageController.flushUI();
            Main.showView(OrderManageView.class);
        }

    }
    /**
     * 启动初始化组件
     */
    void init() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent parent = userMainView.getView();
                UserMainController.this.stage = (Stage) parent.getScene().getWindow();
                stage.setWidth(1280);
                stage.setHeight(760);
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
                            Main.showView(UserMainView.class);
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



    //子窗口：填写订单
    public void OnBtnShowChild() {
        AnchorPane ap = new AnchorPane();
        ap.setStyle("-fx-background-color:#ffffff");
        ap.setPrefWidth(421);
        ap.setPrefHeight(335);

        Label label1 = new Label();
        label1.setText("报刊名称");
        label1.setPrefWidth(156);
        label1.setPrefHeight(44);
        label1.setLayoutX(50);
        label1.setLayoutY(30);

        Label labelPaperName = new Label();
        final String paperName = (String) paperListView.getSelectionModel().getSelectedItem();
        //设置报刊名称
        labelPaperName.setText(paperName);
        labelPaperName.setPrefWidth(216);
        labelPaperName.setPrefHeight(44);
        labelPaperName.setLayoutX(206);
        labelPaperName.setLayoutY(30);

        Label label2 = new Label();
        label2.setText("季度报价");
        label2.setPrefWidth(156);
        label2.setPrefHeight(44);
        label2.setLayoutX(50);
        label2.setLayoutY(80);

        final Label labelSsonPrice = new Label();
        if(!"".equals(paperName) && paperName!=null){
            Integer price = queryMapper.queryNewsPaperByName(paperName).getSsonPrice();
            //设置季度报价
            labelSsonPrice.setText(price.toString());
        }
        labelSsonPrice.setPrefWidth(66);
        labelSsonPrice.setPrefHeight(44);
        labelSsonPrice.setLayoutX(206);
        labelSsonPrice.setLayoutY(80);

        Label label3 = new Label();
        label3.setText("元");
        label3.setPrefWidth(56);
        label3.setPrefHeight(44);
        label3.setLayoutX(270);
        label3.setLayoutY(80);

        Label label4 = new Label();
        label4.setText("订阅份数");
        label4.setPrefWidth(156);
        label4.setPrefHeight(44);
        label4.setLayoutX(50);
        label4.setLayoutY(134);

        //订阅份数的输入框
        final TextField textSubscriptNo = new TextField();
        textSubscriptNo.setPrefWidth(175);
        textSubscriptNo.setPrefHeight(35);
        textSubscriptNo.setLayoutX(197);
        textSubscriptNo.setLayoutY(143);

        Label label5 = new Label();
        label5.setText("订阅月数");
        label5.setPrefWidth(138);
        label5.setPrefHeight(44);
        label5.setLayoutX(50);
        label5.setLayoutY(191);

        Label label6 = new Label();
        label6.setText("所需金额(元)");
        label6.setPrefWidth(138);
        label6.setPrefHeight(44);
        label6.setLayoutX(50);
        label6.setLayoutY(248);

        //订阅月数的输入框
        final TextField textSubscriptMonth = new TextField();
        textSubscriptMonth.setPrefWidth(175);
        textSubscriptMonth.setPrefHeight(35);
        textSubscriptMonth.setLayoutX(199);
        textSubscriptMonth.setLayoutY(196);

        final Label labelTotalPrice = new Label();
        //labelTotalPrice.setText("");
        labelTotalPrice.setPrefWidth(175);
        labelTotalPrice.setPrefHeight(35);
        labelTotalPrice.setLayoutX(199);
        labelTotalPrice.setLayoutY(249);
        textSubscriptNo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //输入的订阅份数和订阅月数都不为空
                if(!"".equals(textSubscriptNo.getText())&&!"".equals(textSubscriptMonth.getText())){
                    //输入的订阅份数和订阅月数都为纯数字
                    if(!isNotNumeric(textSubscriptNo.getText()) && !isNotNumeric(textSubscriptMonth.getText())){
                        Integer totalPrice = Integer.valueOf(labelSsonPrice.getText())*Integer.valueOf(textSubscriptNo.getText())*Integer.valueOf(textSubscriptMonth.getText());
                        labelTotalPrice.setText(totalPrice.toString());
                    }
                }
            }
        });
        textSubscriptMonth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //输入的订阅份数和订阅月数都不为空
                if(!"".equals(textSubscriptNo.getText())&&!"".equals(textSubscriptMonth.getText())){
                    //输入的订阅份数和订阅月数都为纯数字
                    if(!isNotNumeric(textSubscriptNo.getText()) && !isNotNumeric(textSubscriptMonth.getText())){
                        Integer totalPrice = Integer.valueOf(labelSsonPrice.getText())*Integer.valueOf(textSubscriptNo.getText())*Integer.valueOf(textSubscriptMonth.getText());
                        labelTotalPrice.setText(totalPrice.toString());
                    }
                }
            }
        });

        //点击订阅的按钮
        final Button btnSubscript = new Button();
        btnSubscript.setText("点击订阅");
        btnSubscript.setPrefWidth(146);
        btnSubscript.setPrefHeight(43);
        btnSubscript.setLayoutX(152);
        btnSubscript.setLayoutY(292);
        //把元素加到版上，很重要！！！
        ap.getChildren().addAll(label1,label2,label3,label4,label5,label6,labelTotalPrice,labelPaperName,labelSsonPrice,btnSubscript,textSubscriptMonth,textSubscriptNo);

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

        //用户点击订阅按钮后触发
        btnSubscript.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String subscriptNoStr = textSubscriptNo.getText().trim();
                String subscriptMonthStr = textSubscriptMonth.getText().trim();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("信息");

                String str = "";
                if ("".equals(subscriptNoStr) && "".equals(subscriptMonthStr)) {
                    str = "订阅份数和订阅月数不能为空！";
                } else if ("".equals(subscriptNoStr)) {
                    str = "订阅份数不能为空！";
                } else if ("".equals(subscriptMonthStr)) {
                    str = "订阅月数不能为空！";
                }
                if ("".equals(str.trim())) {
                    //订阅份数和订阅月数都不为空时插入数据
                    String userId = loginController.getLabelUserId();
                    String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                    String paperName = (String) paperListView.getSelectionModel().getSelectedItem();
                    String codeName  = queryMapper.queryNewsPaperByName(paperName).getCodeName();
                    //判断输入的订阅份数和订阅月数是否为纯数字
                    if(isNotNumeric(subscriptNoStr)||isNotNumeric(subscriptMonthStr)){
                        //如果其中有一个不是纯数字，则弹出错误信息
                        String str2 = "订阅份数和订阅月数必须都为纯数字！";
                        alert.setContentText(str2);
                        alert.showAndWait();
                        labelTotalPrice.setText("");
                        return;
                    }
                    order.setOrderNo( currentTimeMillis + userId);
                    order.setUserId(userId);
                    order.setCodeName(codeName);
                    order.setSubscriptNo(Integer.valueOf(subscriptNoStr));
                    order.setSubscriptMonth(Integer.valueOf(subscriptMonthStr));
                    order.setTotalPrice(Integer.valueOf(labelTotalPrice.getText()));

                    //检测重复提交订单
                    boolean isSubScripted = queryMapper.isSubScripted(userId,codeName);
                    if(isSubScripted){
                        str = "已订阅该报刊，请勿重复订阅!";
                        alert.setContentText(str);
                        alert.showAndWait();
                    }else{
                        int result = queryMapper.SubScriptPaper(order);
                        if(result < 0){
                            str = "订单提交失败，请重新填写";
                            alert.setContentText(str);
                            alert.showAndWait();
                        }else{
                            str = "提交订单成功！可在查询功能里查询自己的订单";
                            alert.setContentText(str);
                            alert.showAndWait();
                            s.close();
                        }
                    }
                }else {//如果订阅份数或订阅月数为空，则提示错误信息
                    alert.setContentText(str);
                    alert.showAndWait();
                }
            }
        });

        btnSubscript.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btnSubscript.setCursor(Cursor.HAND);
            }
        });
    }

    //子窗口，修改个人信息
    public void modifyProfile(){
        //平面
        AnchorPane ap = new AnchorPane();
        ap.setStyle("-fx-background-color:#ffffff");
        ap.setPrefWidth(371);
        ap.setPrefHeight(504);

        User userInfo = queryMapper.queryUserInfoById(loginController.getLabelUserId());

        final Label label1 = new Label();
        label1.setText("用户ID：");
        label1.setFont(Font.font(24));
        label1.setPrefWidth(100);
        label1.setPrefHeight(32);
        label1.setLayoutX(51);
        label1.setLayoutY(38);

        final Label labelUserId = new Label();
        labelUserId.setText(loginController.getLabelUserId());
        labelUserId.setFont(Font.font(24));
        labelUserId.setPrefWidth(88);
        labelUserId.setPrefHeight(32);
        labelUserId.setLayoutX(155);
        labelUserId.setLayoutY(38);

        Label label2 = new Label();
        label2.setText("密码：");
        label2.setFont(Font.font(24));
        label2.setPrefWidth(88);
        label2.setPrefHeight(32);
        label2.setLayoutX(76);
        label2.setLayoutY(81);

        final PasswordField passwordFieldFirst = new PasswordField();
        passwordFieldFirst.setPromptText("请输入密码...");
        passwordFieldFirst.setPrefWidth(200);
        passwordFieldFirst.setPrefHeight(32);
        passwordFieldFirst.setLayoutX(150);
        passwordFieldFirst.setLayoutY(82);

        Label label3 = new Label();
        label3.setText("确认密码：");
        label3.setFont(Font.font(24));
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
        label4.setFont(Font.font(24));
        label4.setPrefWidth(88);
        label4.setPrefHeight(32);
        label4.setLayoutX(78);
        label4.setLayoutY(165);

        final TextField textFieldName = new TextField();
        textFieldName.setPromptText("请输入姓名...");
        textFieldName.setText(userInfo.getName());
        textFieldName.setPrefWidth(200);
        textFieldName.setPrefHeight(32);
        textFieldName.setLayoutX(150);
        textFieldName.setLayoutY(166);

        Label label5 = new Label();
        label5.setText("身份证号：");
        label5.setFont(Font.font(24));
        label5.setPrefWidth(120);
        label5.setPrefHeight(32);
        label5.setLayoutX(30);
        label5.setLayoutY(211);

        final TextField  textFieldIDCard = new TextField();
        textFieldIDCard.setPromptText("请输入身份证号...");
        textFieldIDCard.setText(userInfo.getIDCard());
        textFieldIDCard.setPrefWidth(200);
        textFieldIDCard.setPrefHeight(32);
        textFieldIDCard.setLayoutX(150);
        textFieldIDCard.setLayoutY(212);

        Label label6 = new Label();
        label6.setText("手机号：");
        label6.setFont(Font.font(24));
        label6.setPrefWidth(96);
        label6.setPrefHeight(32);
        label6.setLayoutX(50);
        label6.setLayoutY(252);

        final TextField textFieldPhone = new TextField();
        textFieldPhone.setPromptText("请输入手机号...");
        if(!"".equals(userInfo.getPhone())){
            textFieldPhone.setText(userInfo.getPhone());
        }
        textFieldPhone.setPrefWidth(200);
        textFieldPhone.setPrefHeight(32);
        textFieldPhone.setLayoutX(150);
        textFieldPhone.setLayoutY(253);

        Label label7 = new Label();
        label7.setText("地址：");
        label7.setFont(Font.font(24));
        label7.setPrefWidth(88);
        label7.setPrefHeight(32);
        label7.setLayoutX(74);
        label7.setLayoutY(296);

        final TextField textFieldAddress = new TextField();
        textFieldAddress.setPromptText("请输入地址...");
        if(!"".equals(userInfo.getAddress())){
            textFieldAddress.setText(userInfo.getAddress());
        }
        textFieldAddress.setPrefWidth(200);
        textFieldAddress.setPrefHeight(32);
        textFieldAddress.setLayoutX(150);
        textFieldAddress.setLayoutY(297);

        Label label8 = new Label();
        label8.setText("部门号：");
        label8.setFont(Font.font(24));
        label8.setPrefWidth(96);
        label8.setPrefHeight(32);
        label8.setLayoutX(50);
        label8.setLayoutY(335);

        final TextField textFieldDeptNo = new TextField();
        textFieldDeptNo.setPromptText("请输入部门号...");
        textFieldDeptNo.setText(userInfo.getDeptNo().toString());
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
                labelUserId,passwordFieldFirst,passwordFieldSecond,btnSummit,
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
                String userIdStr = labelUserId.getText().trim();
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
                if ("".equals(str)) {
                    if(!passwordStrFirst.equals(passwordStrSecond)) {
                        str = "两次密码输入不相同，请重新输入！";
                        alert.setContentText(str);
                        alert.showAndWait();
                        return;
                    }
                    if(IDCardStr.length()!=18) {
                        String str2 = "身份证号只能为18位！";
                        alert.setContentText(str2);
                        alert.showAndWait();
                        return;
                    }
                    //判断输入的部门号是否为纯数字
                    if(isNotNumeric(deptStr)){
                        //如果不是纯数字，则弹出错误信息
                        String str3 = "部门号必须是纯数字，且部门号为21-25！";
                        alert.setContentText(str3);
                        alert.showAndWait();
                        return;
                    }
                    //判断输入的部门号在数据库中是否存在
                    boolean deptNoIsExist = queryMapper.queryDeptNoIsExist(Integer.valueOf(deptStr));
                    if (!deptNoIsExist){//如果输入的部门号不存在
                        String str4 = "输入的部门号不存在！部门号为21-25！";
                        alert.setContentText(str4);
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
                int result = queryMapper.updateUser(user);
                if (result > 0) {
                    str = "更改个人信息成功！";
                    alert.setContentText(str);
                    alert.showAndWait();
                    s.close();
                }else{
                    str = "更改个人信息失败，请重新填写！";
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


    public boolean isNotNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( isNum.matches() ){
            return false;
        }
        return true;
    }

    public void setUserName(){
        userName.setText(loginController.getLabelUserName());
    }

}