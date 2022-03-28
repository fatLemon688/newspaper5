package com.bigotry.handler;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * 窗口拖到处理器
 * @author xiyang.ycj
 * @since Nov 28, 2019 15:33:27 PM
 */
public class DragWindowHandler implements EventHandler<MouseEvent> {
    /**
     * primaryStage为start方法头中的Stage
     */
    private Stage primaryStage;
    private double oldStageX;
    private double oldStageY;
    private double oldScreenX;
    private double oldScreenY;

    public DragWindowHandler(Stage primaryStage) { //构造器
        this.primaryStage = primaryStage;
    }

    @Override
    public void handle(MouseEvent e) {
        // 鼠标按下的事件
        if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
            this.oldStageX = this.primaryStage.getX();
            this.oldStageY = this.primaryStage.getY();
            this.oldScreenX = e.getScreenX();
            this.oldScreenY = e.getScreenY();
        // 鼠标拖动的事件
        } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            this.primaryStage.setX(e.getScreenX() - this.oldScreenX + this.oldStageX);
            this.primaryStage.setY(e.getScreenY() - this.oldScreenY + this.oldStageY);
        }
    }

}
