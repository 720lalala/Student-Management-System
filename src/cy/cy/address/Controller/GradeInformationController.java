package cy.cy.address.Controller;
import cy.cy.address.MainApp;
import cy.cy.address.model.Grade;
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public  class GradeInformationController {
    @FXML
    private   TableView<Grade> GradeTable;
    @FXML
    private TableColumn<Grade, String> snoColumn;
    @FXML
    private TableColumn<Grade, String> cnoColumn;
    @FXML
    private TableColumn<Grade, String> scoreColumn;
    @FXML
    private TableColumn<Grade, String> editColumn;
    @FXML
    private TableColumn<Grade, Boolean> chooseColumn;
    @FXML
    private TextField searchLable;

    private ObservableList<Grade> GradeData = FXCollections.observableArrayList();
    private ObservableList<Grade> GradeData2 = FXCollections.observableArrayList();
    private ObservableList<Grade> GradeData3 = FXCollections.observableArrayList();
    private MainApp mainapp;
    public void setMainApp(MainApp mainApp) {
        this.mainapp = mainApp;

    }
    /**
     *构造函数。
     *构造函数在initialize（）方法之前调用。
     */
    public GradeInformationController() {

    }

    /**
     * intialize() 方法在fxml文件完成载入时被自动调用. 那时, 所有的FXML属性都应已被初始化.
     */
    /**
     * 将数据作为可观察的人员列表返回。
     * @return
     */
    public ObservableList<Grade> getGradeData() {
        return GradeData;
    }
    public ObservableList<Grade> getGradeData3() {
        return GradeData3;
    }
    @FXML
    private void initialize() {
        // 用两列初始化Person表格。
        /**
         * 我们在表格列上使用setCellValueFactory(...)
         * 来确定为特定列使用Person对象的某个属性.
         * 箭头 -> 表示我们在使用Java 8的 Lambdas 特性
         */

        GradeTable.setItems(null
        );
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        String sql =null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            sql = "SELECT * FROM grade;";
            rs=stmt.executeQuery(sql);
            System.out.println("qazwsx");
            GradeData.clear();
            while (rs.next()) {
                String sno = rs.getString("sno");
                String cno = rs.getString("cno");
                int score = rs.getInt("score");

                GradeData.add(new Grade(sno, cno, Integer.toString(score)));
            }
        }
        catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            try{
                sql="create table Grade(" +
                        "sno char(10) not null," +
                        "cno char(10) not null," +
                        "score int check(score>-1 and score<101)," +
                        "primary key(sno,cno),\n" +
                        "constraint fk_sno foreign key(sno) references Student(sno)," +
                        "constraint fk_cno foreign key(cno) references Course(cno)" +
                        ");";
                stmt.executeUpdate(sql);
            }
            catch (Exception b)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("出错啦！");
                alert.setContentText("系统运行错误！");
                alert.showAndWait();
            }
        }
        finally {
            GradeTable.refresh();
            DataAccess.closeConnection(rs);
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
            GradeTable.setItems(getGradeData());
            cnoColumn.setCellValueFactory(null);
            snoColumn.setCellValueFactory(null);
            scoreColumn.setCellValueFactory(null);
            cnoColumn.setCellValueFactory(cellData -> cellData.getValue().cnoProperty());
            snoColumn.setCellValueFactory(cellData -> cellData.getValue().snoProperty());
            scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty());
        }
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

                                    EditGradeData(clickedCou,selectedIndex);
                                }

                            });
                        }
                    }

                };
                return edit;
            });

        chooseColumn.setCellFactory((col) -> {
            TableCell<Grade, Boolean> cell = new TableCell<Grade, Boolean>() {
                CheckBox checkBox = new CheckBox();
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        this.setGraphic(checkBox);
                        checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
                            if (newVal) {
                                // 添加选中时执行的代码
                                System.out.println("第" + this.getIndex() + "行被选中！");
                                Grade clickedCou = this.getTableView().getItems().get(this.getIndex());
                                if(!GradeData2.contains(clickedCou))
                                    GradeData2.add(clickedCou);
                                // 获取当前单元格的对象
                                // this.getItem();
                            }
                            else
                            {
                                System.out.println("第" + this.getIndex() + "行被取消！");
                                Grade clickedCou = this.getTableView().getItems().get(this.getIndex());
                                if(GradeData2.contains(clickedCou))
                                    GradeData2.remove(clickedCou);
                            }
                        });
                    }
                }

            };
            return cell;
        });

    }
    /**
     *当用户点击新按钮时调用。打开一个对话框进行编辑
     *新人的详细信息。
     */
    @FXML
    public void handleNewGrade() {
        Grade tempGrade = new  Grade();
        boolean okClicked = showGradeNewDialog(tempGrade);
        if (okClicked) {
            getGradeData().add(tempGrade);
            GradeTable.refresh();
            GradeData2.clear();
        }
    }

    public boolean showGradeNewDialog(Grade grade) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GradeDialog.fxml"));
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
            GradeDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setGrade(grade);
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
    public void EditGradeData(Grade grade,int Index )
    {
        Grade tempGrade =grade;
        boolean okClicked = showGradeEditDialog(tempGrade);
        if (okClicked) {
            getGradeData().set(Index,tempGrade);
            GradeTable.refresh();
            GradeData2.clear();
        }
    }

    public boolean showGradeEditDialog(Grade grade) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GradeEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("成绩信息修改");
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
            GradeEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setGrade(grade);
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
    public void DeleteGradeData()
    {
        Connection conn=null;
        Statement stmt=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql = null;
            System.out.println("11111");
            System.out.println("Opened database successfully");
            System.out.println(GradeData2.size());
            int i=0;
            while(GradeData2.size()>i)
            {
                GradeData.remove(GradeData2.get(i));
                sql = "DELETE from Grade where cno='"+GradeData2.get(i).getCno()+"' AND " +
                        "sno='"+GradeData2.get(i).getSno()+"';";

                stmt.executeUpdate(sql);
                i++;
            }
        }
        catch (Exception e ) {
            System.out.println("Line="+e.getStackTrace()[0].getLineNumber()+55551);// 打印出错行号
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        finally {
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
        }
        GradeData2.clear();
        GradeTable.refresh();

    }
    /**
     * 模糊查询 搜索
     */
    public void handlesearch()
    {
        if(searchLable.getText()!=null&&searchLable.getText().length() != 0)
        {
            GradeData3.removeAll(GradeData3);
            String Keyword=searchLable.getText();
            System.out.println(Keyword);
            Connection conn=null;
            Statement stmt=null;
            ResultSet rs=null;
            try {
                conn= DataAccess.getConnection();
                stmt = conn.createStatement();//创建SQL语句
                String sql = "call select_grade('%"+Keyword+"%');";
                rs=stmt.executeQuery(sql);
                System.out.println("11111");
                while ( rs.next() ) {
                    String sno = rs.getString("sno");
                    String cno = rs.getString("cno");
                    int score = rs.getInt("score");
                    GradeData3.add(new Grade(sno, cno, Integer.toString(score)));
                } System.out.println( GradeData3.size());
            }
            catch (Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            finally {
                DataAccess.closeConnection(rs);
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
            }
            if(GradeData3.size()==0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("error");
                alert.setHeaderText("出错啦！");
                alert.setContentText("没有搜索到所需内容！");
                alert.showAndWait();
            }
            else{
                try {
                    // Load the fxml file and create a new stage for the popup dialog.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/GradeSearch.fxml"));
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
                    GradeSearchController controller = loader.getController();
                    controller.setDialogStage(dialogStage);
                    controller.setGradeInformation(this);
                    controller.setGradeData3(GradeData3);
                    controller.init();
                    GradeData2.clear();
                    // Show the dialog and wait until the user closes it
                    dialogStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
        else{
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
    public void searchEdit(Grade grade,String c,String s)
    {
        int i=0;
        for(;i<GradeData.size();i++)
            if(GradeData.get(i).getCno().equals(c)&&GradeData.get(i).getSno().equals(s)) break;
        System.out.println(i);
        getGradeData().set(i,grade);
        GradeTable.refresh();
        GradeData2.clear();
    }
    /**
     * 搜索处理删除1
     */
    public void searchDelete(String c,String s)
    {
        System.out.println("55555");
        int i=0;
        for(;i<GradeData.size();i++)
            if(GradeData.get(i).getCno().equals(c)&&GradeData.get(i).getSno().equals(s)) break;
        GradeData.remove(i);
        GradeTable.refresh();
        GradeData2.clear();
    }
    /**
     * 一键清空
     */
    public void EmptyGradeData()
    {
        Connection conn=null;
        Statement stmt=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql = null;;
            System.out.println("11111");
            System.out.println(GradeData2.size());
            GradeData.clear();
            sql = "DELETE from Grade ;";
            stmt.executeUpdate(sql);

        }
        catch (Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );

        }
        finally {
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
        }
        GradeData2.clear();
        GradeTable.refresh();
    }


/**
 * get到gradetable
 * **/
public void refresh()
{
    System.out.println("444423");
    initialize();
}

}

