package cy.cy.address.Controller;

import cy.cy.address.MainApp;
import cy.cy.address.model.Login;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField loginNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    String[][] mylogin = {{"720", "1234567"}, {"jack", "1234567"}, {"kei", "1234567"}};
    private Login login;
    private MainApp mainapp;
    private Stage primaryStage;

    /**
     * *由主应用程序调用以将参考返回给自己。
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainapp = mainApp;

    }

    /**
     * 当用户点击确定时调用。
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            login.setLoginName(loginNameField.getText());
            login.setpassword(passwordField.getText());
            passwordField.setText("");
            loginNameField.setText("");
            mainapp.showRootLayoutDialog(login);

        }
    }

    private boolean isInputValid() {
        String loginName = loginNameField.getText();
        String password = passwordField.getText();
        String errorMessage = "";
        if (loginName == null || loginName.length() == 0)
            errorMessage = "登录名为空\n";
        else if (password == null || password.length() <= 6) {
            errorMessage = "密码不能少于6位\n";
            passwordField.setText("");
        }


        if (errorMessage.length() == 0) {
            int i;
            for (i = 0; i < 3; i++) {
                if (mylogin[i][0].equals(loginName) && mylogin[i][1].equals(password)) break;
            }
            if (i < 2)
                return true;
            else {
                passwordField.setText("");
                loginNameField.setText("");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("密码或登录名错误！");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                return false;
            }
        } else {
            //显示错误信息。
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("出现错误");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    /**
     * 在对话框中设置要编辑的人员。
     *
     * @param login
     */
    public void setLogin(Login login) {
        this.login = login;
        loginNameField.setText(login.getLoginName());
        passwordField.setText(login.getpassword());
    }

    /**
     * 当用户点击退出时调用。
     */
    @FXML
    private void handleClock() {
        System.exit(0);
    }


}
