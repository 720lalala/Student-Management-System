package cy.cy.address.Controller;

import cy.cy.address.model.Student;
import cy.cy.address.util.DataAccess;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class StudentDialogController {
    @FXML
    private TextField SnoField;
    @FXML
    private TextField SnameField;
    @FXML
    private ComboBox SsexField;
    @FXML
    private TextField ClassesField;


    private Stage StudentdialogStage;
    private Student student;
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
     * @param StudentdialogStage
     */
    public void setDialogStage(Stage StudentdialogStage) {
        this.StudentdialogStage = StudentdialogStage;
        this.StudentdialogStage.getIcons().add(new Image("file:image/13.png"));
    }

    /**
     * 在对话框中设置要编辑的人员。
     *
     * @param student
     */
    public void setStudent(Student student) {
        this.student = student;

        SnoField.setText(student.getSno());
        SnameField.setText(student.getSname());
        SsexField.setValue(student.getSsex());
        ClassesField.setText(student.getClasses());
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
            student.setSno(SnoField.getText());
            student.setSname(SnameField.getText());
            student.setSsex(SsexField.getValue().toString());
            student.setClasses(ClassesField.getText());
            okClicked = true;
            StudentdialogStage.close();
        }
    }
    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        StudentdialogStage.close();
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
        if (SnameField.getText() == null || SnameField.getText().length() == 0) {
            errorMessage += "姓名还没有输入！\n";
        }
        if (SsexField.getValue() == null) {
            errorMessage += "性别还没有选择！\n";
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
                String sql=null;
                System.out.println("Opened database successfully");
                sql = "INSERT INTO Student VALUES('"
                        + SnoField.getText() + "', '"
                        + SnameField.getText() + "', '"
                        + SsexField.getValue() + "', '"
                        + ClassesField.getText() + "');";
                System.out.println(sql);
                stmt.executeUpdate(sql);



            } catch (Exception e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("该学号已存在，请重新输入！");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
                return false;
            }

           finally {
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
                System.out.println("Records created successfully");
                return true;
            }

        }
        else {
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
