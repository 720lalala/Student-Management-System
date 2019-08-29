package cy.cy.address.Controller;

import cy.cy.address.MainApp;
import cy.cy.address.model.Student;
import cy.cy.address.util.DataAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentSearchController {
    private Stage StudentdialogStage;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> snoColumn;
    @FXML
    private TableColumn<Student, String> snameColumn;
    @FXML
    private TableColumn<Student, String> ssexColumn;
    @FXML
    private TableColumn<Student, String> classesColumn;
    @FXML
    private TableColumn<Student, String> editColumn;
    @FXML
    private TableColumn<Student, String> deleteColumn;
    private StudentInformationController StudentInformation;
    private ObservableList<Student> StudentData3 = FXCollections.observableArrayList();

    public void setDialogStage(Stage StudentdialogStage) {
        this.StudentdialogStage = StudentdialogStage;

        this.StudentdialogStage.getIcons().add(new Image("file:image/13.png"));

    }

    public void setStudentInformation(StudentInformationController StudentInformation) {
        this.StudentInformation = StudentInformation;
    }
    public void setStudentData3(ObservableList<Student> StudentData) {
        this.StudentData3 = StudentData;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    public void init() {
        studentTable.setItems(StudentInformation.getStudentData3());
        snoColumn.setCellValueFactory(cellData -> cellData.getValue().snoProperty());
        snameColumn.setCellValueFactory(cellData -> cellData.getValue().snameProperty());
        ssexColumn.setCellValueFactory(cellData -> cellData.getValue().ssexProperty());
        classesColumn.setCellValueFactory(cellData -> cellData.getValue().classesProperty());
        editColumn.setCellFactory((col) -> {
            TableCell<Student, String> edit = new TableCell<Student, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        //ImageView delICON = new ImageView(getClass().getResource("delete.png").toString());
                        Button editBtn = new Button("修改");
                        editBtn.setStyle(null);
                        this.setGraphic(editBtn);
                        editBtn.setOnMouseClicked((me) -> {
                            Student clickedCou = this.getTableView().getItems().get(this.getIndex());
                            System.out.println("修改 " + clickedCou.getSno() + clickedCou.getSname() + " 的记录");
                            int selectedIndex = this.getIndex();

                            System.out.println(selectedIndex);
                            if (selectedIndex >= 0) {
                                EditStudentData(clickedCou, selectedIndex);
                                // studentTable.getItems().remove(selectedIndex);
                                // StudentInformation.EditStudentData(clickedCou, selectedIndex);
                            }

                        });
                    }
                }

            };
            return edit;
        });
        deleteColumn.setCellFactory((col) -> {
            TableCell<Student, String> dele = new TableCell<Student, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        //ImageView delICON = new ImageView(getClass().getResource("delete.png").toString());
                        Button editBtn = new Button("删除");
                        editBtn.setStyle(null);
                        this.setGraphic(editBtn);
                        editBtn.setOnMouseClicked((me) -> {
                            Student clickedCou = this.getTableView().getItems().get(this.getIndex());
                            System.out.println("删除 " + clickedCou.getSno() + clickedCou.getSname() + " 的记录");
                            int selectedIndex = this.getIndex();
                            System.out.println(selectedIndex);
                            if (selectedIndex >= 0) {
                                // studentTable.getItems().remove(selectedIndex);
                                boolean a = DeleteStudentData(clickedCou);
                                if (!a) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("error");
                                    alert.setHeaderText("运行出错");
                                    alert.setContentText("所删除的学生有关联信息！");
                                    alert.showAndWait();
                                } else {

                                    StudentData3.remove(clickedCou);
                                    studentTable.refresh();
                                    StudentInformation.searchDelete(clickedCou.getSno());
                                }
                            }

                        });
                    }
                }

            };
            return dele;
        });
    }

    private void EditStudentData(Student student, int Index) {
        Student tempStuent = student;
        boolean okClicked = StudentInformation.showStudentEditDialog(tempStuent);
        if (okClicked) {
            StudentData3.set(Index, tempStuent);
            studentTable.refresh();
            StudentInformation.searchEdit(tempStuent, (student.getSno()));
        }


    }

    private boolean DeleteStudentData(Student student) {
        Student tempStuent = student;
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt=conn.createStatement();
            String sql=null;
            System.out.println("Opened database successfully");
            rs = stmt.executeQuery("SELECT * FROM Grade where sno='" + student.getSno() + "';");
            if (rs.next()) {
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
                return false;
            }
            sql = "DELETE from Student where sno='" + student.getSno() + "';";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        }
        finally {
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
            return true;
        }
    }
}
