package cy.cy.address.Controller;

import cy.cy.address.MainApp;
import cy.cy.address.model.Course;
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

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CourseInformationController {
    @FXML
    private TableView<Course> CourseTable;
    @FXML
    private TableColumn<Course, String> cnoColumn;
    @FXML
    private TableColumn<Course, String> cnameColumn;
    @FXML
    private TableColumn<Course, String> classesColumn;
    @FXML
    private TableColumn<Course, String> editColumn;
    @FXML
    private TableColumn<Course, Boolean> chooseColumn;
    @FXML
    private TextField searchLable;

    private ObservableList<Course> CourseData = FXCollections.observableArrayList();
    private ObservableList<Course> CourseData2 = FXCollections.observableArrayList();
    private ObservableList<Course> CourseData3 = FXCollections.observableArrayList();
    private MainApp mainapp;

    public void setMainApp(MainApp mainApp) {
        this.mainapp = mainApp;

    }

    /**
     * 构造函数。
     * 构造函数在initialize（）方法之前调用。
     */
    public CourseInformationController() {

    }

    /**
     * intialize() 方法在fxml文件完成载入时被自动调用. 那时, 所有的FXML属性都应已被初始化.
     */
    /**
     * 将数据作为可观察的人员列表返回。
     *
     * @return
     */
    public ObservableList<Course> getCourseData() {
        return CourseData;
    }

    public ObservableList<Course> getCourseData3() {
        return CourseData3;
    }

    /**
     * 初始化
     */
    @FXML
    private void initialize() {
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            rs = stmt.executeQuery("SELECT * FROM Course;");
            while (rs.next()) {
                String cno = rs.getString("cno");
                String cname = rs.getString("cname");
                String classes = rs.getString("class");
                CourseData.add(new Course(cno, cname, classes));
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate("create table Course(" +
                        "cno char(10) primary key not null," +
                        "cname text not null," +
                        "class text not null" +
                        ");");
                System.out.println("111111");

            } catch (Exception b) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("出错啦！");
                alert.setContentText("系统运行错误！");
                alert.showAndWait();
            }



        }
        finally {
            DataAccess.closeConnection(rs);
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
            CourseTable.setItems(getCourseData());
            cnoColumn.setCellValueFactory(cellData -> cellData.getValue().cnoProperty());
            cnameColumn.setCellValueFactory(cellData -> cellData.getValue().cnameProperty());
            classesColumn.setCellValueFactory(cellData -> cellData.getValue().classesProperty());
        }
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
        chooseColumn.setCellFactory((col) -> {
            TableCell<Course, Boolean> cell = new TableCell<Course, Boolean>() {
                CheckBox checkBox = new CheckBox();

                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        //   System.out.println("中！");
                        this.setGraphic(checkBox);
                        checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
                            if (newVal) {
                                // 添加选中时执行的代码
                                System.out.println("第" + this.getIndex() + "行被选中！");
                                Course clickedCou = this.getTableView().getItems().get(this.getIndex());
                                if (!CourseData2.contains(clickedCou))
                                    CourseData2.add(clickedCou);
                                // 获取当前单元格的对象
                                // this.getItem();
                            } else {
                                System.out.println("第" + this.getIndex() + "行被取消！");
                                Course clickedCou = this.getTableView().getItems().get(this.getIndex());
                                if (CourseData2.contains(clickedCou))
                                    CourseData2.remove(clickedCou);
                            }

                        });

                    }
                }

            };
            return cell;
        });

    }

    /**
     * 当用户点击新按钮时调用。打开一个对话框进行编辑
     * 新人的详细信息。
     */
    @FXML
    public void handleNewCourse() {
        Course tempCourse = new Course();
        boolean okClicked = showCourseNewDialog(tempCourse);
        if (okClicked) {
            getCourseData().add(tempCourse);
            CourseTable.refresh();
            CourseData2.clear();
        }
    }

    /**
     * 要新的屏幕
     * @param course
     * @return
     */
    public boolean showCourseNewDialog(Course course) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CourseDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("课程信息");
            /**
             * initModality(Modality modality) 设置stage的形态，
             * 根据功能来讲就是窗口之间的阻塞模式，该方法不能被primaryStage调用，
             * 只能被自己创建的stage调用。
             */
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //initOwner(Window owner)指定所属的window，就相当指定父窗口
            dialogStage.initOwner(mainapp.getDialogStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CourseDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCourse(course);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 编辑学生信息
     */
    public void EditCourseData(Course course, int Index) {
        Course tempCourse = course;
        boolean okClicked = showCourseEditDialog(tempCourse);
        if (okClicked) {
            getCourseData().set(Index, tempCourse);
            CourseTable.refresh();
            CourseData2.clear();
        }
    }

    public boolean showCourseEditDialog(Course course) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CourseEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("课程信息修改");
            /**
             * initModality(Modality modality) 设置stage的形态，
             * 根据功能来讲就是窗口之间的阻塞模式，该方法不能被primaryStage调用，
             * 只能被自己创建的stage调用。
             */
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //initOwner(Window owner)指定所属的window，就相当指定父窗口
            dialogStage.initOwner(mainapp.getDialogStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CourseEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCourse(course);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除
     */
    public void DeleteCourseData() {
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql = null;
            System.out.println("11111");            int i = 0;
            System.out.println(CourseData2.size());
            while (CourseData2.size() > i) {
                rs = stmt.executeQuery("SELECT * FROM Grade where cno='" + CourseData2.get(i).getCno() + "';");
                if (rs.next()) {
                    break;
                }

                i++;
            }
            if (CourseData2.size() == i) {
                i = 0;
                while (CourseData2.size() > i) {

                    CourseData.remove(CourseData2.get(i));

                    sql = "DELETE from Course where cno='" + CourseData2.get(i).getCno() + "';";

                    stmt.executeUpdate(sql);

                    i++;
                }
            } else {
                int res= JOptionPane.showConfirmDialog(null, "是否继续\n继续将会对该课程的成绩信息一并删除", "是否继续", JOptionPane.YES_NO_OPTION);
                if(res==JOptionPane.YES_OPTION){
                    //点击“是”后执行这个代码块
                    i = 0;
                    while (CourseData2.size() > i) {
                        CourseData.remove(CourseData2.get(i));
                        sql = "DELETE from course where cno='" + CourseData2.get(i).getCno() + "';";
                        stmt.executeUpdate(sql);
                        i++;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        finally {
            DataAccess.closeConnection(rs);
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
            CourseData2.clear();
            CourseTable.refresh();
        }

    }

    /**
     * 模糊查询 搜索
     */
    public void handlesearch() {
        if (searchLable.getText() != null && searchLable.getText().length() != 0) {
            CourseData3.removeAll(CourseData3);
            String Keyword = searchLable.getText();
            System.out.println(Keyword);
            Connection conn=null;
            Statement stmt=null;
            ResultSet rs=null;
            try {
                conn= DataAccess.getConnection();
                stmt = conn.createStatement();//创建SQL语句
                String sql = "call select_course('%"+Keyword+"%');";
                rs=stmt.executeQuery(sql);
                System.out.println("11111");
                while (rs.next()) {
                    String cno = rs.getString("cno");
                    String cname = rs.getString("cname");
                    String classes = rs.getString("class");
                    CourseData3.add(new Course(cno, cname, classes));
                    System.out.println(CourseData3.size());

                }
                ///    stmt.executeUpdate(sql);
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
            finally {
                DataAccess.closeConnection(rs);
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
            }
            if (CourseData3.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("出错啦！");
                alert.setContentText("没有搜索到所需内容！");
                alert.showAndWait();
            } else {
                try {
                    // Load the fxml file and create a new stage for the popup dialog.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/CourseSearch.fxml"));
                    AnchorPane page = (AnchorPane) loader.load();
                    // Create the dialog Stage.
                    Stage dialogStage = new Stage();
                    dialogStage.setTitle("查询内容");
                    /**
                     * initModality(Modality modality) 设置stage的形态，
                     * 根据功能来讲就是窗口之间的阻塞模式，该方法不能被primaryStage调用，
                     * 只能被自己创建的stage调用。
                     */
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    //initOwner(Window owner)指定所属的window，就相当指定父窗口
                    dialogStage.initOwner(mainapp.getDialogStage());
                    Scene scene = new Scene(page);
                    dialogStage.setScene(scene);

                    // Set the person into the controller.
                    CourseSearchController controller = loader.getController();
                    controller.setDialogStage(dialogStage);
                    controller.setCourseInformation(this);
                    controller.setCourseData3(CourseData3);
                    controller.init();
                    CourseData2.clear();
                    // Show the dialog and wait until the user closes it
                    dialogStage.showAndWait();


                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("出错啦！");
            alert.setContentText("没有需要搜索的内容！");
            alert.showAndWait();

        }
    }

    /**
     * 搜索处理编辑1
     */
    public void searchEdit(Course course, String t) {
        int i = 0;
        for (; i < CourseData.size(); i++)
            if (CourseData.get(i).getCno().equals(t)) break;
        System.out.println(i);
        getCourseData().set(i, course);
        CourseTable.refresh();
        CourseData2.clear();
    }

    /**
     * 搜索处理删除1
     */
    public void searchDelete(String t) {
        System.out.println("11111");
        int i = 0;
        for (; i < CourseData.size(); i++)
            if (CourseData.get(i).getCno().equals(t)) break;
        CourseData.remove(i);
        CourseTable.refresh();
        CourseData2.clear();
    }

    /**
     * 一键清空
     */
    public void EmptyCourseData() {
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql = null;;
            System.out.println("11111");
            System.out.println(CourseData2.size());
            rs = stmt.executeQuery("SELECT * FROM Grade,Course where Grade.cno=Course.cno;");
            if (rs.next()) {
                //要改的地方
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("运行出错");
                alert.setContentText("所删除的课程有关联信息！");
                alert.showAndWait();
            } else {
                CourseData.clear();
                sql = "DELETE from Course ;";
                stmt.executeUpdate(sql);

            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        }
        finally {
            DataAccess.closeConnection(rs);
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
            CourseData2.clear();
            CourseTable.refresh();
        }
    }


}

