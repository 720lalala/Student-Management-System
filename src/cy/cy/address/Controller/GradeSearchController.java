package cy.cy.address.Controller;

import cy.cy.address.model.Grade;
import cy.cy.address.util.DataAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GradeSearchController {
    private Stage GradedialogStage;
    @FXML
    private TableView<Grade> gradeTable;
    @FXML
    private TableColumn<Grade, String> snoColumn;
    @FXML
    private TableColumn<Grade, String> cnoColumn;
    @FXML
    private TableColumn<Grade, String> scoreColumn;
    @FXML
    private TableColumn<Grade, String> editColumn;
    @FXML
    private TableColumn<Grade, String> deleteColumn;
    private GradeInformationController GradeInformation;
    private ObservableList<Grade> GradeData3 = FXCollections.observableArrayList();

    public void setDialogStage(Stage GradedialogStage) {
        this.GradedialogStage = GradedialogStage;
    }

    public void setGradeInformation(GradeInformationController GradeInformation) {
        this.GradeInformation = GradeInformation;
        this.GradedialogStage.getIcons().add(new Image("file:image/13.png"));
    }

    public void setGradeData3(ObservableList<Grade> CourseData) {
        this.GradeData3 = CourseData;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    public void init() {
        System.out.println(GradeInformation.getGradeData3().size() + " 1111111");
        gradeTable.setItems(GradeInformation.getGradeData3());
        cnoColumn.setCellValueFactory(cellData -> cellData.getValue().cnoProperty());
        snoColumn.setCellValueFactory(cellData -> cellData.getValue().snoProperty());
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty());
        editColumn.setCellFactory((col) -> {
            TableCell<Grade, String> edit = new TableCell<Grade, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        Button editBtn = new Button("修改");
                        editBtn.setStyle(null);
                        this.setGraphic(editBtn);
                        editBtn.setOnMouseClicked((me) -> {
                            Grade clickedCou = this.getTableView().getItems().get(this.getIndex());
                            System.out.println("修改 " + clickedCou.getCno() + clickedCou.getSno() + " 的记录");
                            int selectedIndex = this.getIndex();

                            System.out.println(selectedIndex);
                            if (selectedIndex >= 0) {
                                EditGradeData(clickedCou, selectedIndex);
                            }

                        });
                    }
                }

            };
            return edit;
        });
        deleteColumn.setCellFactory((col) -> {
            TableCell<Grade, String> dele = new TableCell<Grade, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        Button editBtn = new Button("删除");
                        editBtn.setStyle(null);
                        this.setGraphic(editBtn);
                        editBtn.setOnMouseClicked((me) -> {
                            Grade clickedCou = this.getTableView().getItems().get(this.getIndex());
                            System.out.println("删除 " + clickedCou.getCno() + clickedCou.getSno() + " 的记录");
                            int selectedIndex = this.getIndex();

                            System.out.println(selectedIndex);
                            if (selectedIndex >= 0) {
                                boolean a = DeleteGradeData(clickedCou);
                                if (!a) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("error");
                                    alert.setHeaderText("运行出错");
                                    alert.setContentText("所删除的课程有关联信息！");
                                    alert.showAndWait();
                                } else {
                                    GradeData3.remove(clickedCou);
                                    gradeTable.refresh();
                                    GradeInformation.searchDelete(clickedCou.getCno(), clickedCou.getSno());
                                }
                            }

                        });
                    }
                }

            };
            return dele;
        });
    }

    private void EditGradeData(Grade grade, int Index) {
        Grade tempGrade = grade;
        boolean okClicked = GradeInformation.showGradeEditDialog(tempGrade);
        if (okClicked) {
            GradeData3.set(Index, tempGrade);
            gradeTable.refresh();
            GradeInformation.searchEdit(tempGrade, grade.getCno(), grade.getSno());
        }


    }

    private boolean DeleteGradeData(Grade grade) {
        Grade tempGrade = grade;
        Connection conn=null;
        Statement stmt=null;
        try {
            conn= DataAccess.getConnection();
            stmt=conn.createStatement();
            String sql=null;
            System.out.println("Opened database successfully");

            sql = "DELETE from grade where cno='" + grade.getCno() +
                    "' and sno='" + grade.getSno() + "'";
            System.out.println(sql);
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            System.out.println("Line="+e.getStackTrace()[0].getLineNumber()+5556666);// 打印出错行号
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        }
        finally {
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
            return true;
        }

    }
}
