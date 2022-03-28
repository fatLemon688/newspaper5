package com.bigotry.controller;

import com.bigotry.Main;
import com.bigotry.handler.DragWindowHandler;
import com.bigotry.mapper.queryMapper;
import com.bigotry.pojo.AdminUser;
import com.bigotry.view.AdminLoginView;
import com.bigotry.view.AdminMainView;
import com.bigotry.view.QueryByPaperView;
import com.bigotry.view.UserLoginView;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author ：JJHu
 * @date ：Created in 2020/12/1 15:17
 * @description：
 * @modified By：
 * @version: $
 */
@FXMLController
public class AdminLoginController implements Initializable {

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

    @FXML
    private TextField textAdminName;
    @FXML
    private PasswordField AdminPasswordField;
    @FXML
    private Button btnAdminLogin;
    @FXML
    private Button btnBackToUserLogin;
    @Autowired
    private queryMapper queryMapper;
    @Autowired
    private AdminLoginView adminLoginView;
    public static  String adminName;

    @FXML
    private ImageView adminImg;
    @FXML
    private ImageView admin2Img;
    /**
     * 登录
     *
     */
    @FXML
    private void handleAdminLogin() throws IOException {
        String adminNameStr = textAdminName.getText().trim();
        String passwordStr = AdminPasswordField.getText().trim();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("信息");

        String str = "";
        if ("".equals(adminNameStr) && "".equals(passwordStr)) {
            str = "管理员名和密码不能为空！";
        } else if ("".equals(adminNameStr)) {
            str = "管理员名不能为空！";
        } else if ("".equals(passwordStr)) {
            str = "密码不能为空！";
        }
        if ("".equals(str.trim())) {
            AdminUser info = checkLogin(adminNameStr, passwordStr);
            // 登录
            if (info != null) {
                adminName = adminNameStr;
                stage.setWidth(1179);
                stage.setHeight(752);
                stage.setResizable(false);
                Main.showView(QueryByPaperView.class);
            }else{
                alert.setContentText("请输入正确的账号或密码！");
                alert.showAndWait();
            }
        } else {
            alert.setContentText(str);
            alert.showAndWait();
        }
    }

    //设置图片初始化
    private void setImageInit(File file , ImageView imageView){
        String string = file.toURI().toString();
        Image image = new Image(string);
        imageView.setImage(image);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        //加载图片文件
        File adminFile = new File("src/main/resources/images/adminLogin/admin.jpg");
        setImageInit(adminFile , adminImg);
        File admin2File = new File("src/main/resources/images/adminLogin/admin2.jpg");
        setImageInit(admin2File , admin2Img);
        btnAdminLogin.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnAdminLogin.setCursor(Cursor.HAND);
            }
        });
        btnBackToUserLogin.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnBackToUserLogin.setCursor(Cursor.HAND);
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
                Parent parent = adminLoginView.getView();
                AdminLoginController.this.stage = (Stage) parent.getScene().getWindow();
                stage.setWidth(472);
                stage.setHeight(444);
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
                            Main.showView(AdminLoginView.class);
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
    /**
     * 判断账号和密码是否正确
     */
    private AdminUser checkLogin(String adminName, String password) {

        return queryMapper.queryAdminLoginInfo(adminName, password);
    }

    @FXML
    public void handleBackToUserLogin() {
        stage.setWidth(472);
        stage.setHeight(445);
        stage.setResizable(false);
        Main.showView(UserLoginView.class);
    }
}
