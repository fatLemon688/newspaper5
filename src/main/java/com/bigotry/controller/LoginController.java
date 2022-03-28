package com.bigotry.controller;

import com.bigotry.Main;
import com.bigotry.handler.DragWindowHandler;
import com.bigotry.view.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import com.bigotry.mapper.queryMapper;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import com.bigotry.pojo.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
@FXMLController
public class LoginController implements Initializable {

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
    UserLoginView userLoginView;
    @FXML
    private GridPane login;
    @Autowired
    User user;
    @FXML
    private TextField txtUserId;
    @FXML
    private PasswordField txtPasswordField;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnAdminLogin;
    @Autowired
    User labelUser;
    //对第几次打开用户页面计数
    private Integer openModifyUserMainPageCount = 0;
    @Autowired
    UserMainController userMainController;
    @Autowired
    queryMapper query;
    @FXML
    private ImageView arrowImg;
    @FXML
    private ImageView arrow2Img;
    @FXML
    private ImageView userLoginImg;
    @FXML
    private ImageView passwordImg;
    @FXML
    private ImageView adminLoginImg;

    public void initialize(URL location, ResourceBundle resources) {
        init();
        //加载图片文件
        File arrowFile = new File("src/main/resources/images/userLogin/arrow-1773939_1280.png");
        setImageInit(arrowFile , arrowImg);
        File arrow2File = new File("src/main/resources/images/userLogin/arrow2.jpg");
        setImageInit(arrow2File , arrow2Img);
        File userLoginFile = new File("src/main/resources/images/userLogin/userLogin.jpg");
        setImageInit(userLoginFile , userLoginImg);
        File passwordFile = new File("src/main/resources/images/userLogin/password.jpg");
        setImageInit(passwordFile , passwordImg);
        File adminLoginFile = new File("src/main/resources/images/userLogin/adminLogin.jpg");
        setImageInit(adminLoginFile , adminLoginImg);

        btnLogin.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnLogin.setCursor(Cursor.HAND);
            }
        });

        btnAdminLogin.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnAdminLogin.setCursor(Cursor.HAND);
            }
        });

        btnClose.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnClose.setCursor(Cursor.HAND);
            }
        });

        btnMin.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                btnMin.setCursor(Cursor.HAND);
            }
        });

    }

    //设置图片初始化
    private void setImageInit(File file , ImageView imageView){
        String string = file.toURI().toString();
        Image image = new Image(string);
        imageView.setImage(image);
    }
    /**
     * 登录
     *
     */
    @FXML
    private void handleSubmitButtonAction() throws IOException {
        String userIdStr = txtUserId.getText().trim();
        String passwordStr = txtPasswordField.getText().trim();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("信息");

        String str = "";
        if ("".equals(userIdStr) && "".equals(passwordStr)) {
            str = "用户名和密码不能为空！";
        } else if ("".equals(userIdStr)) {
            str = "用户名不能为空！";
        } else if ("".equals(passwordStr)) {
            str = "密码不能为空！";
        }
        if ("".equals(str.trim())) {
            user = checkLogin(userIdStr, passwordStr);
            // 登录
            if (user != null) {
                labelUser.setUserId(userIdStr);
                 labelUser.setName(user.getName());
                stage.setWidth(1280);
                stage.setHeight(760);
                stage.setResizable(false);
                //第一次登录，先初始化
                if(openModifyUserMainPageCount < 1){
                    Main.showView(UserMainView.class);
                    openModifyUserMainPageCount++;
                }else{
                    //不是第一次登录，先设置用户名称，在展示页面
                    userMainController.setUserName();
                    userMainController.handleShowPaper();
                    Main.showView(UserMainView.class);
                }

            }else{
                alert.setContentText("请输入正确的账号或密码！");
                alert.showAndWait();
            }
        } else {
                alert.setContentText(str);
                alert.showAndWait();
            }
        }

        @FXML
        private void handleGoTOAdminLogin() throws IOException {
            Main.showView(AdminLoginView.class);
        }
        @FXML
        public void handleSignUpAction() throws Exception {
            stage.setWidth(500);
            stage.setHeight(436);
            stage.setResizable(false);
            Main.showView(SignUpView.class);
        }

    /**
     * 判断账号和密码是否正确
     */
    private User checkLogin(String userName, String password) {

        return query.queryUserInfo(userName, password);
    }

    /**
     * 启动初始化组件
     */
    void init() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Parent parent = userLoginView.getView();
                LoginController.this.stage = (Stage) parent.getScene().getWindow();
                stage.setWidth(472);
                stage.setHeight(445);
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
                            Main.showView(UserLoginView.class);
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

    public String getLabelUserName() {
        return labelUser.getName();
    }
    public String getLabelUserId() {
        return labelUser.getUserId();
    }
}
