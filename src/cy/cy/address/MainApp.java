package cy.cy.address;

import java.io.IOException;

import cy.cy.address.Controller.*;
import cy.cy.address.model.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;

public class MainApp extends Application {

    private Stage primaryStage,dialogstage;
    private AnchorPane loginLayout,rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("学生信息管理系统");
        this.primaryStage.getIcons().add(new Image("file:image/13.png"));
        initLogin();
     //   hide();
    }

    /**
     * Initializes the initlayout.
     */

    public void initLogin() {
        try {
            Login tempLogin = new Login();
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/login.fxml"));
            loginLayout = (AnchorPane) loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(loginLayout);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            // Set the person into the controller.
            LoginController controller = loader.getController();
            controller.setLogin(tempLogin);
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    /**
     * Returns the main stage.
     * @return dialogStage
     */
    public Stage getDialogStage() {
        return dialogstage;
    }
    /**
     * 展示主窗口
     * @param
     */

    public void showRootLayoutDialog(Login login) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            BorderPane page = (BorderPane) loader.load();
             // Create the dialog Stage.
            Stage  dialogStage=new Stage();
            dialogStage.setTitle("管理员："+login.getLoginName());
            dialogStage.getIcons().add(new Image("file:image/13.png"));
             /**
             * initModality(Modality modality) 设置stage的形态，
             * 根据功能来讲就是窗口之间的阻塞模式，该方法不能被primaryStage调用，
             * 只能被自己创建的stage调用。
              */
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //initOwner(Window owner)指定所属的window，就相当指定父窗口
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            primaryStage.hide();
            this.dialogstage=dialogStage;
            dialogstage.setResizable(false);
            // Set the person into the controller.
            mainController controller = loader.getController();
            controller.setMainApp(this);
            StudentInformationController controller2=controller.getStuManController();
            controller2.setMainApp(this);
            CourseInformationController controller3=controller.getCouManController();
            controller3.setMainApp(this);
            GradeInformationController controller4=controller.getGraManController();
            controller4.setMainApp(this);
            controller.setGraManController(controller4);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    public void showprimaryStage()
    {
        if(dialogstage != null)
            dialogstage.hide();
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

}