package cy.cy.address.Controller;

import cy.cy.address.Controller.StudentDialogController;
import cy.cy.address.Controller.StudentEditDialogController;
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

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentInformationController {
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
    private TableColumn<Student, Boolean> chooseColumn;
    @FXML
    private TextField searchLable;

    private ObservableList<Student> StudentData = FXCollections.observableArrayList();
    private ObservableList<Student> StudentData2 = FXCollections.observableArrayList();
    private ObservableList<Student> StudentData3 = FXCollections.observableArrayList();
    private MainApp mainapp;

    public void setMainApp(MainApp mainApp) {
        this.mainapp = mainApp;

    }

    /**
     * 构造函数。
     * 构造函数在initialize（）方法之前调用。
     */
    public StudentInformationController() {

    }

    /**
     * intialize() 方法在fxml文件完成载入时被自动调用. 那时, 所有的FXML属性都应已被初始化.
     */
    /**
     * 将数据作为可观察的人员列表返回。
     *
     * @return
     */
    public ObservableList<Student> getStudentData() {
        return StudentData;
    }

    public ObservableList<Student> getStudentData3() {
        return StudentData3;
    }

    @FXML
    private void initialize() {
        // 用两列初始化Person表格。
        /**
         * 我们在表格列上使用setCellValueFactory(...)
         * 来确定为特定列使用Person对象的某个属性.
         * 箭头 -> 表示我们在使用Java 8的 Lambdas 特性
         */
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql = "SELECT * FROM student;";
            rs=stmt.executeQuery(sql);
            System.out.println("222222");
            while (rs.next()) {
                String sno = rs.getString("sno");
                String sname = rs.getString("sname");
                String ssex = rs.getString("ssex");
                String classes = rs.getString("class");
                StudentData.add(new Student(sno, sname, ssex, classes));
                //        System.out.println(sno + "  " + sname + "  " + classes);

            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate("create table student(" +
                        "sno char(10) primary key not null," +
                        "sname char(20) not null," +
                        "ssex char(5) not null," +
                        "class char(20) not null" +
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
            studentTable.setItems(getStudentData());
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

                                    // studentTable.getItems().remove(selectedIndex);
                                    EditStudentData(clickedCou, selectedIndex);
                                }

                            });
                        }
                    }

                };
                return edit;
            });
        }

        chooseColumn.setCellFactory((col) -> {
            TableCell<Student, Boolean> cell = new TableCell<Student, Boolean>() {
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
                                Student clickedCou = this.getTableView().getItems().get(this.getIndex());
                                if (!StudentData2.contains(clickedCou))
                                    StudentData2.add(clickedCou);
                                // 获取当前单元格的对象
                                // this.getItem();
                            } else {
                                System.out.println("第" + this.getIndex() + "行被取消！");
                                Student clickedCou = this.getTableView().getItems().get(this.getIndex());
                                if (StudentData2.contains(clickedCou))
                                    StudentData2.remove(clickedCou);
                            }

                        });

                    }
                }

            };
            return cell;
        });


    }

    public void DeleteStudentData() {
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql = null;
            System.out.println("11111");
            int i = 0;
            System.out.println(StudentData2.size());
            while (StudentData2.size() > i) {
                sql="SELECT * FROM grade where sno='" + StudentData2.get(i).getSno() + "';";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {

                    break;
                }

                i++;
            }
            if (StudentData2.size() == i) {
                i = 0;
                while (StudentData2.size() > i) {

                    StudentData.remove(StudentData2.get(i));

                    sql = "DELETE from student where sno='" + StudentData2.get(i).getSno() + "';";
                    stmt.executeUpdate(sql);

                    i++;
                }
            } else {
                int res= JOptionPane.showConfirmDialog(null, "是否继续\n继续将会对该学生的选课信息一并删除", "是否继续", JOptionPane.YES_NO_OPTION);
                if(res==JOptionPane.YES_OPTION){
                    //点击“是”后执行这个代码块
                    i = 0;
                    while (StudentData2.size() > i) {
                        StudentData.remove(StudentData2.get(i));
                        sql = "DELETE from student where sno='" + StudentData2.get(i).getSno() + "';";
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
            StudentData2.clear();
            studentTable.refresh();

        }

    }

    /**
     * 当用户点击新按钮时调用。打开一个对话框进行编辑
     * 新人的详细信息。
     */
    @FXML
    public void handleNewPerson() {
        Student tempStuent = new Student();
        boolean okClicked = showStudentNewDialog(tempStuent);
        if (okClicked) {
            getStudentData().add(tempStuent);
            studentTable.refresh();
            StudentData2.clear();
        }
    }

    /**
     * 编辑学生信息
     */
    public void EditStudentData(Student student, int Index) {
        Student tempStuent = student;
        boolean okClicked = showStudentEditDialog(tempStuent);
        if (okClicked) {
            getStudentData().set(Index, tempStuent);
            studentTable.refresh();
            StudentData2.clear();
        }
    }

    /**
     * 打开一个对话框来编辑指定人员的详细信息。如果用户
     * 点击确定，更改将保存到提供的人员对象中并为真
     * is returned.
     *
     * @param student 要编辑的人物
     * @return 如果用户点击OK，则返回true，否则返回false。
     */

    public boolean showStudentNewDialog(Student student) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StudentDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("学生信息");
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
            StudentDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStudent(student);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showStudentEditDialog(Student student) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StudentEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("学生信息修改");
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
            StudentEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStudent(student);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 一键清空
     */
    public void EmptyStudentData() {
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql = null;;
            System.out.println("11111");
            System.out.println(StudentData2.size());
            rs = stmt.executeQuery("SELECT * FROM Grade,Student where Grade.sno=Student.sno;");
            if (rs.next()) {
                //将改成级联删除
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("运行出错");
                alert.setContentText("所删除的学生有关联信息！");
                alert.showAndWait();
            } else {
                StudentData.clear();
                sql = "DELETE from Student ;";
                stmt.executeUpdate(sql);
            }


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        }
        finally {
            DataAccess.closeConnection(rs);
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
            StudentData2.clear();
            studentTable.refresh();
        }

    }

    /**
     * 模糊查询 搜索
     */
    public void handlesearch() {
        if (searchLable.getText() != null && searchLable.getText().length() != 0) {
            StudentData3.removeAll(StudentData3);
            String Keyword = searchLable.getText();
            System.out.println(Keyword);
            Connection conn=null;
            Statement stmt=null;
            ResultSet rs=null;
            try {
                conn= DataAccess.getConnection();
                stmt = conn.createStatement();//创建SQL语句
                String sql = "call select_student('%"+Keyword+"%');";
                rs=stmt.executeQuery(sql);
                System.out.println("11111");
                while (rs.next()) {
                    String sno = rs.getString("sno");
                    String sname = rs.getString("sname");
                    String ssex = rs.getString("ssex");
                    String classes = rs.getString("class");
                    StudentData3.add(new Student(sno, sname, ssex, classes));
                    System.out.println(StudentData3.size());

                }

            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
            finally {
                DataAccess.closeConnection(rs);
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
            }
            if (StudentData3.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("出错啦！");
                alert.setContentText("没有搜索到所需内容！");
                alert.showAndWait();
            } else {
                try {
                    // Load the fxml file and create a new stage for the popup dialog.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/StudentSearch.fxml"));
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
                    StudentSearchController controller = loader.getController();
                    controller.setDialogStage(dialogStage);
                    controller.setStudentInformation(this);
                    controller.setStudentData3(StudentData3);
                    controller.init();
                    StudentData2.clear();
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
    public void searchEdit(Student student, String t) {
        int i = 0;
        for (; i < StudentData.size(); i++)
            if (StudentData.get(i).getSno().equals(t)) break;
        System.out.println(i);
        getStudentData().set(i, student);
        studentTable.refresh();
        StudentData2.clear();
    }

    /**
     * 搜索处理删除1
     */
    public void searchDelete(String t) {
        System.out.println("11111");
        int i = 0;
        for (; i < StudentData.size(); i++)
            if (StudentData.get(i).getSno().equals(t)) break;
        StudentData.remove(i);
        studentTable.refresh();
        StudentData2.clear();
    }

}
