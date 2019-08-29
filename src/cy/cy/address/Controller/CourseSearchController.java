package cy.cy.address.Controller;

import cy.cy.address.model.Course;
import cy.cy.address.util.DataAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.image.Image;

public class CourseSearchController {
    private Stage CoursedialogStage;
    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> cnoColumn;
    @FXML
    private TableColumn<Course, String> cnameColumn;
    @FXML
    private TableColumn<Course, String> classesColumn;
    @FXML
    private TableColumn<Course, String> editColumn;
    @FXML
    private TableColumn<Course, String> deleteColumn;
    private CourseInformationController CourseInformation;
    private ObservableList<Course> CourseData3 = FXCollections.observableArrayList();

    public void setDialogStage(Stage CoursedialogStage) {
        this.CoursedialogStage = CoursedialogStage;
        this.CoursedialogStage.getIcons().add(new Image("file:image/13.png"));
    }

    public void setCourseInformation(CourseInformationController CourseInformation) {
        this.CourseInformation = CourseInformation;
    }

    public void setCourseData3(ObservableList<Course> CourseData) {
        this.CourseData3 = CourseData;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    public void init() {
        System.out.println(CourseInformation.getCourseData3().size() + " 1111111");
        courseTable.setItems(CourseInformation.getCourseData3());
        cnoColumn.setCellValueFactory(cellData -> cellData.getValue().cnoProperty());
        cnameColumn.setCellValueFactory(cellData -> cellData.getValue().cnameProperty());
        classesColumn.setCellValueFactory(cellData -> cellData.getValue().classesProperty());
        editColumn.setCellFactory((col) -> {
            TableCell<Course, String> edit = new TableCell<Course, String>() {

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
                            Course clickedCou = this.getTableView().getItems().get(this.getIndex());
                            System.out.println("修改 " + clickedCou.getCno() + clickedCou.getCname() + " 的记录");
                            int selectedIndex = this.getIndex();

                            System.out.println(selectedIndex);
                            if (selectedIndex >= 0) {
                                EditCourseData(clickedCou, selectedIndex);
                            }

                        });
                    }
                }

            };
            return edit;
        });
        deleteColumn.setCellFactory((col) -> {
            TableCell<Course, String> dele = new TableCell<Course, String>() {

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
                            Course clickedCou = this.getTableView().getItems().get(this.getIndex());
                            System.out.println("删除 " + clickedCou.getCno() + clickedCou.getCname() + " 的记录");
                            int selectedIndex = this.getIndex();

                            System.out.println(selectedIndex);
                            if (selectedIndex >= 0) {

                                // studentTable.getItems().remove(selectedIndex);
                                boolean a = DeleteCourseData(clickedCou);
                                if (!a) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("error");
                                    alert.setHeaderText("运行出错");
                                    alert.setContentText("所删除的课程有关联信息！");
                                    alert.showAndWait();
                                } else {

                                    CourseData3.remove(clickedCou);
                                    courseTable.refresh();
                                    CourseInformation.searchDelete(clickedCou.getCno());
                                }
                            }

                        });
                    }
                }

            };
            return dele;
        });
    }

    private void EditCourseData(Course course, int Index) {
        Course tempCourse = course;
        boolean okClicked = CourseInformation.showCourseEditDialog(tempCourse);
        if (okClicked) {
            CourseData3.set(Index, tempCourse);
            courseTable.refresh();
            CourseInformation.searchEdit(tempCourse, (course.getCno()));
        }


    }

    private boolean DeleteCourseData(Course course) {
        Course tempCourse = course;
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt=conn.createStatement();
            String sql=null;
            System.out.println("Opened database successfully");
            rs = stmt.executeQuery("SELECT * FROM Grade where cno='" + course.getCno() + "';");
            if (rs.next()) {
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
                return false;
            }
            sql = "DELETE from Course where cno='" + course.getCno() + "';";

            stmt.executeUpdate(sql);

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
