package cy.cy.address.Controller;

import cy.cy.address.model.Course;
import cy.cy.address.util.DataAccess;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CourseEditDialogController {
    @FXML
    private Label CnoLable;
    @FXML
    private TextField CnameField;
    @FXML
    private TextField ClassesField;
    private Stage CoursedialogStage;
    private Course course;
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
     * @param CoursedialogStage
     */
    public void setDialogStage(Stage CoursedialogStage) {
        this.CoursedialogStage = CoursedialogStage;
        this.CoursedialogStage.getIcons().add(new Image("file:image/13.png"));
    }

    /**
     * 在对话框中设置要编辑的人员。
     *
     * @param course
     */
    public void setCourse(Course course) {
        this.course = course;

        CnoLable.setText(course.getCno());
        CnameField.setText(course.getCname());
        ClassesField.setText(course.getClasses());
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
            course.setCno(CnoLable.getText());
            course.setCname(CnameField.getText());
            course.setClasses(ClassesField.getText());
            okClicked = true;
            CoursedialogStage.close();
        }
    }

    /**
     * 取消
     */
    @FXML
    private void handleCancel() {
        CoursedialogStage.close();
    }

    /**
     * 验证文本字段中的用户输入。
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";


        if (CnameField.getText() == null || CnameField.getText().length() == 0) {
            errorMessage += "课程名还没有输入！\n";
        }
        if (ClassesField.getText() == null || ClassesField.getText().length() == 0) {
            errorMessage += "班别还没有输入！\n";
        }
        if (errorMessage.length() == 0) {
            Connection conn=null;
            Statement stmt=null;
            try {
                conn= DataAccess.getConnection();
                stmt = conn.createStatement();//创建SQL语句
                System.out.println("11111");
                String sql = "UPDATE Course set cname='"
                        + CnameField.getText() + "' where cno='" + CnoLable.getText() + "';";
                //   System.out.println(sql);
                stmt.executeUpdate(sql);
                sql = "UPDATE Course set class='"
                        + ClassesField.getText() + "' where cno='" + CnoLable.getText() + "';";
                stmt.executeUpdate(sql);
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
                return false;
            }
            finally {
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);

            }
            System.out.println("Records created successfully");
            return true;
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
