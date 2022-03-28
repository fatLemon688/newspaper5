package com.bigotry;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.GUIState;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.bigotry.view.UserLoginView;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.**")
public class Main extends AbstractJavaFxApplicationSupport{

    public static void main(String[] args) {
        launch(Main.class , UserLoginView.class ,args);
    }

    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
       stage.initStyle(StageStyle.TRANSPARENT); /* 透明标题栏 */
      stage.setResizable(false); /* 设置窗口不可改变 */
        // 窗口始终悬浮
        //stage.setAlwaysOnTop(true);

    }
}
