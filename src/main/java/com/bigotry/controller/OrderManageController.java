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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：JJHu
 * @date ：Created in 2020/12/6 14:12
 * @description：
 * @modified By：
 * @version: $
 */
@FXMLController
public class OrderManageController  implements Initializable {

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
    @Autowired
    private OrderManageView orderManageView;
    @FXML
    private  ListView OrderListView;
    @FXML
    private  Text textOrderNo;
    @FXML
    private Label userName;
    @FXML
    private  Text textUserId;
    @FXML
    private  Text textPaperName;
    @FXML
    private  Text textSubScriptNo;
    @FXML
    private  Text textSubScriptMonth;
    @FXML
    private  Text textTotalPrice;
    @FXML
    private  Text textDateTime;
    @FXML
    private Button btnFlushUI;
    @FXML
    private Button btnCancelOrder;
    @Autowired
    queryMapper queryMapper;
    @Autowired
    User user;
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
        handleQuerySubScripted();
        btnFlushUI.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnFlushUI.setCursor(Cursor.HAND);
            }
        });

        btnCancelOrder.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btnCancelOrder.setCursor(Cursor.HAND);
            }
        });
    }


    //设置图片初始化
    private void setImageInit(File file , ImageView imageView){
        String string = file.toURI().toString();
        Image image = new Image(string);
        imageView.setImage(image);
    }
    //取消订单
    @FXML
    public void handleCancelOrder() {
        String orderNo = (String) OrderListView.getSelectionModel().getSelectedItem();
        TbOrder orderInfo = queryMapper.queryOrderByOrderNo(orderNo);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("信息");
        String str = "";
        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION,"确定要取消该订单？",new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("确定", ButtonBar.ButtonData.YES));
        //设置窗口的标题
        _alert.setTitle("确认");
        //设置对话框的 icon 图标，参数是主窗口的 stage
        _alert.initOwner(stage);
        //showAndWait() 将在对话框消失以前不会执行之后的代码
        Optional<ButtonType> _buttonType = _alert.showAndWait();
        //根据点击结果返回
        if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
            if(orderInfo!=null){
                //删除当前这条订单
                int result = queryMapper.deleteOrderByOrderNo(orderNo);
                if(result>0){
                    str = "取消订单成功!";
                    alert.setContentText(str);
                    alert.showAndWait();
                    //返回订单管理的首页
                    flushUI();
                    Main.showView(OrderManageView.class);
                }else{
                    str = "取消订单失败!";
                    alert.setContentText(str);
                    alert.showAndWait();
                }
            }
        }else{
            Main.showView(OrderManageView.class);
        }

    }
    //刷新界面
    @FXML
    public  void flushUI(){
        //定义列表的数据
        ObservableList<String> items = FXCollections.observableArrayList();
        //获取所有订单
        List<TbOrder> orders  = queryMapper.queryAllOrderByUserId(loginController.getLabelUserId());
        //遍历所有订单并获取当前订单的编号，并存入ListView
        for (TbOrder order:orders) {
            //显示订单编号
            items.add(order.getOrderNo());
        }
        OrderListView.setItems(items);
        textOrderNo.setText("");
        textDateTime.setText("");
        textUserId.setText("");
        textPaperName.setText("");
        textSubScriptNo.setText("");
        textSubScriptMonth.setText("");
        textTotalPrice.setText("");
    }
    /**
     * 启动初始化组件
     */
    void init() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent parent = orderManageView.getView();
                OrderManageController.this.stage = (Stage) parent.getScene().getWindow();
                stage.setWidth(728);
                stage.setHeight(596);
                stage.setResizable(false);

                //为按钮设置图标
                ImageView imageView = new ImageView(new Image(getClass().getResource( "/images/最小化图标1.png").toExternalForm()));
                btnMin.setGraphic(imageView);
                ImageView imageView1 = new ImageView(new Image(getClass().getResource( "/images/关闭.png").toExternalForm()));
                btnClose.setGraphic(imageView1);
                ImageView imageView2 = new ImageView(new Image(getClass().getResource( "/images/刷新.jpg").toExternalForm()));
                btnFlushUI.setGraphic(imageView2);

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
                            Main.showView(OrderManageView.class);
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
    //查询所有订单
    @FXML
    public void handleQuerySubScripted() {
        //定义列表的数据
        ObservableList<String> items = FXCollections.observableArrayList();
        //获取所有订单
        List<TbOrder> orders  = queryMapper.queryAllOrderByUserId(loginController.getLabelUserId());
        //遍历所有订单并获取当前订单的编号，并存入ListView
        for (TbOrder order:orders) {
            //显示订单编号
            items.add(order.getOrderNo());
        }
        OrderListView.setItems(items);
        //为ListView的选中项添加监听事件
        OrderListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            //ListView选中项的监听器类，一旦选中某项，就展示订单信息
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String orderNo = (String) OrderListView.getSelectionModel().getSelectedItem();
                //从数据库查询当前orderNo是否存在
                TbOrder order = queryMapper.queryOrderByOrderNo(orderNo);
                if(order != null) {
                    textOrderNo.setText(orderNo);
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(orderNo.substring(0,13))));
                    textDateTime.setText(timeStamp);
                    textUserId.setText(loginController.getLabelUserId());
                    String paperName = queryMapper.queryPaperNameByOrderNo(orderNo).getPaperName();
                    textPaperName.setText(paperName);
                    textSubScriptNo.setText(order.getSubscriptNo().toString());
                    textSubScriptMonth.setText(order.getSubscriptMonth().toString());
                    textTotalPrice.setText(order.getTotalPrice().toString());
                }
            }
        });
    }
    /*修改个人信息*/
    @FXML
    public void handleModifyProfile(ActionEvent actionEvent) {
        modifyProfile();
    }
    /*退出登录*/
    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        //切换回登录界面
        stage.setWidth(472);
        stage.setHeight(445);
        stage.setResizable(false);
        Main.showView(UserLoginView.class);
    }
    /*展示所有报刊*/
    @FXML
    public void handleShowPaper(ActionEvent actionEvent) {
        flushUI();
        //切换回userMain界面
        stage.setWidth(1280);
        stage.setHeight(760);
        stage.setResizable(false);
        Main.showView(UserMainView.class);
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

    public void textIsNull(Text text){
        if(text!=null){
            text.setText("");
        }else{
            text=new Text();
            text.setText("");
        }
    }
}

