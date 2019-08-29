package cy.cy.address.Controller;

import cy.cy.address.model.Course;
import cy.cy.address.model.Grade;
import cy.cy.address.util.DataAccess;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GradeDialogController {
    @FXML
    private TextField SnoField;
    @FXML
    private TextField CnoField;
    @FXML
    private TextField ScoreField;
    private Stage GradedialogStage;
    private Grade grade;
    private boolean okClicked = false;

    /**
     * 初始化控制器类。这个方法被自动调用
     * 在fxml文件被加载后。
     */
    @FXML
    private void initialize() {
    }

    /**
     * 设置好舞台
     *
     * @param GradedialogStage
     */
    public void setDialogStage(Stage GradedialogStage) {
        this.GradedialogStage = GradedialogStage;
        this.GradedialogStage.getIcons().add(new Image("file:image/13.png"));
    }

    /**
     * 在对话框中设置要编辑的人员。
     *
     * @param grade
     */
    public void setGrade(Grade grade) {
        this.grade = grade;

        CnoField.setText(grade.getCno());
        SnoField.setText(grade.getSno());
        ScoreField.setText(grade.getScore());
    }

    /**
     * 如果用户点击OK，则返回true，否则返回false
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * 当用户点击确定时调用。
     */
    @FXML
    private void handleOk() {

        if (isInputValid()) {
            grade.setCno(CnoField.getText());
            grade.setSno(SnoField.getText());
            grade.setScore(ScoreField.getText());
            okClicked = true;
            GradedialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        GradedialogStage.close();
    }

    /**
     * 验证文本字段中的用户输入。
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";
        if (SnoField.getText() == null || SnoField.getText().length() == 0) {
            errorMessage += "学号还没有输入！\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(SnoField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "学号有非法字符！\n";
            }
        }
        if (CnoField.getText() == null || CnoField.getText().length() == 0) {
            errorMessage += "课程号还没有输入！\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(CnoField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "课程号有非法字符！\n";
            }
        }
        if (ScoreField.getText() == null || ScoreField.getText().length() == 0) {
            errorMessage += "还没有输入！\n";
        } else {
            try {
                int i = Integer.parseInt(ScoreField.getText());
                if (i > 100 || i < 0) errorMessage += "分数输入有误！\n";
            } catch (NumberFormatException e) {
                errorMessage += "分数有非法字符！\n";

            }
        }
        if (errorMessage.length() == 0) {
            Connection conn=null;
            Statement stmt=null;
            ResultSet rs=null;
            try {
                conn= DataAccess.getConnection();
                stmt = conn.createStatement();//创建SQL语句
                String sql=null;
                System.out.println("Opened database successfully");
                rs = stmt.executeQuery("SELECT * FROM Course c,Student s where s.sno='"
                        + SnoField.getText()
                        + "'and  c.cno='" + CnoField.getText() + "';");
                if (!rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("error");
                    alert.setHeaderText("运行出错");
                    alert.setContentText("该学号或课程号不存在，请重新输入！");
                    alert.showAndWait();
                    DataAccess.closeConnection(rs);
                    DataAccess.closeConnection(stmt);
                    DataAccess.closeConnection(conn);
                    return false;
                }
                sql = "INSERT INTO Grade VALUES('"
                        + SnoField.getText() + "', '"
                        + CnoField.getText() + "', "
                        + Integer.parseInt(ScoreField.getText()) + ");";
                System.out.println(sql);
                stmt.executeUpdate(sql);
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("该学号和课程号已存在，请重新输入！");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                return false;
            }
            finally {
                DataAccess.closeConnection(rs);
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
                System.out.println("Records created successfully");
                return true;
            }

        } else {
            //显示错误信息。
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("请重新核对");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
