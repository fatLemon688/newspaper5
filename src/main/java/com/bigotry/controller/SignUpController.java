package com.bigotry.controller;

import com.bigotry.Main;
import com.bigotry.handler.DragWindowHandler;
import com.bigotry.mapper.queryMapper;
import com.bigotry.pojo.User;
import com.bigotry.view.UserLoginView;
import com.bigotry.view.SignUpView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：JJHu
 * @date ：Created in 2020/11/29 21:48
 * @description：javafx中fmlx文件指定的控制器中的方法如果没有加上@FXML注解的话，就必须是public
 * 但有时是public的话，会提示@FXML的成员变量没有初始化，得把方法改为private
 * @modified By：
 * @version: $
 */
@FXMLController
public class SignUpController implements Initializable{

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
    SignUpView registerView;

    @FXML
    private Button btnToLogin;
    @FXML
    private Button btnSignUp;
    @FXML
    private TextField userId;
    @FXML
    private TextField passwordFieldFirst;
    @FXML
    private TextField passwordFieldSecond;
    @FXML
    private TextField name;
    @FXML
    private TextField IDCard;
    @FXML
    private TextField phone;
    @FXML
    private TextField address;
    @FXML
    private TextField deptNo;
    @Autowired
    queryMapper queryMapper;
    @Autowired
    private User user;



    @FXML
    public void handleSignUpButtonAction(){
        String userIdStr = userId.getText().trim();
        String passwordStrFirst = passwordFieldFirst.getText().trim();
        String passwordStrSecond = passwordFieldSecond.getText().trim();
        String nameStr = name.getText().trim();
        String IDCardStr = IDCard.getText().trim();
        String phoneStr = phone.getText().trim();
        String addressStr = address.getText().trim();
        String deptStr = deptNo.getText().trim();
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("信息");
        String str = "";
        if ("".equals(userIdStr)|| "".equals(passwordStrFirst)
                ||"".equals(nameStr) || "".equals(IDCardStr)
                || "".equals(deptStr)|| "".equals(passwordStrSecond)) {
            str = "电话和地址可以为空外其他字段不能为空！";
        }
        //如果提示字符串str为空
        if ("".equals(str)) {
            //如果输入的userId不是纯数字
            if(isNotNumeric(userIdStr)){
                str = "用户ID必须是纯数字！";
                alert.setContentText(str);
                alert.showAndWait();
                return;
            }
            if(!passwordStrFirst.equals(passwordStrSecond)) {
                str = "两次密码输入不相同，请重新输入！";
                alert.setContentText(str);
                alert.showAndWait();
                return;
            }
            //身份证不是18位，弹出错误信息。
            if(IDCardStr.length()!=18) {
                str = "身份证号只能为18位！";
                alert.setContentText(str);
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
        if(!"".equals(phoneStr)){
            user.setPhone(phoneStr);
        }
        if(!"".equals(addressStr)){
            user.setAddress(addressStr);
        }
        user.setDeptNo(Integer.valueOf(deptStr));
        int result = queryMapper.insertIntoUser(user);
        if (result > 0) {
            str = "注册成功！";
            alert.setContentText(str);
            alert.showAndWait();
        }else{
            str = "注册失败，请重新填写！";
            alert.setContentText(str);
            alert.showAndWait();
        }

    }
    @FXML
    public void handleBackToLoginButtonAction() throws IOException {
        stage.setWidth(472);
        stage.setHeight(445);
        stage.setResizable(false);
        Main.showView(UserLoginView.class);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        btnToLogin.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnToLogin.setCursor(Cursor.HAND);
            }
        });
        btnSignUp.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnSignUp.setCursor(Cursor.HAND);
            }
        });
    }

    /**
     * 启动初始化组件
     */
    void init() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent parent = registerView.getView();
                SignUpController.this.stage = (Stage) parent.getScene().getWindow();
                stage.setTitle("新用户注册");
                stage.setWidth(500);
                stage.setHeight(436);
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
                            Main.showView(SignUpView.class);
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

    public boolean isNotNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( isNum.matches() ){
            return false;
        }
        return true;
    }
}
