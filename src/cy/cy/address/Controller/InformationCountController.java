package cy.cy.address.Controller;

import cy.cy.address.util.DataAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InformationCountController {
    @FXML
    private PieChart pieChartsex;
    @FXML
    private PieChart pieChartclass;
    @FXML
    private BarChart<String, Integer> cnoBar;
    @FXML
    private BarChart<String, Integer> gradeBar;
    @FXML
    TabPane tabpane;
    @FXML
    private Label caption;
    @FXML
    private Label caption2;
    @FXML
    private Label caption3;
    @FXML
    private Label tip;
    @FXML
    private TextField search;
    @FXML
    private Label searchresult;

    private XYChart.Series<String, Integer> cnodata = new XYChart.Series<>();
    private XYChart.Series<String, Integer> gradedata = new XYChart.Series<>();
    private int sum1, sum2, sum3;
    java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

    @FXML
    private void initialize() {
        pieChartsex.setTitle("学生性别统计饼状图");
        pieChartsex.setData(getsexData());
        pieChartsex.setLabelLineLength(10);
        pieChartsex.setLegendSide(Side.LEFT);
        pieChartclass.setTitle("学生班级统计饼状图");
        pieChartclass.setData(getclassData());
        pieChartclass.setLabelLineLength(10);
        pieChartclass.setLegendSide(Side.LEFT);
        cnoBar.setTitle("学生选课直方图");
        cnoBar.getData().add(getcnoData());
        cnoBar.setLegendSide(Side.RIGHT);
        cnoBar.setAlternativeColumnFillVisible(true);
        gradeBar.setTitle("学生成绩直方图");
        gradedata.getData().add(new XYChart.Data<>("0-60", 0));
        gradedata.getData().add(new XYChart.Data<>("61-70", 0));
        gradedata.getData().add(new XYChart.Data<>("71-80", 0));
        gradedata.getData().add(new XYChart.Data<>("81-90", 0));
        gradedata.getData().add(new XYChart.Data<>("91-100", 0));
        gradeBar.getData().add(gradedata);
        gradeBar.setLegendSide(Side.LEFT);
        gradeBar.setAlternativeColumnFillVisible(true);
        // final Label caption = new Label("");
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 24 arial;");
        caption2.setTextFill(Color.BLACK);
        caption2.setStyle("-fx-font: 24 arial;");
        caption3.setTextFill(Color.BLACK);
        caption3.setStyle("-fx-font: 16 arial;-fx-font-weight: bold;");
        mousechsex();
        mousechclass();
        mousechcno();
    }

    private ObservableList<PieChart.Data> getsexData() {
        sum1 = 0;
        ObservableList<PieChart.Data> answer = FXCollections.observableArrayList();
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql=null;
            rs = stmt.executeQuery("SELECT  ssex,COUNT(sno) sc from Student  GROUP BY ssex;");
            while (rs.next()) {
                int sc = rs.getInt("sc");
                String ssex = rs.getString("ssex");
                sum1 += sc;
                answer.add(new PieChart.Data(ssex, sc));
            }
            ///    stmt.executeUpdate(sql);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("失败啦！");
            alert.setContentText("没有学生！");
            alert.showAndWait();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        finally {
            DataAccess.closeConnection(rs);
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
        }


        return answer;
    }

    private ObservableList<PieChart.Data> getclassData() {
        sum2 = 0;
        ObservableList<PieChart.Data> answer = FXCollections.observableArrayList();
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql=null;
            rs = stmt.executeQuery("SELECT  class,COUNT(sno) sc from Student  GROUP BY class;");
            while (rs.next()) {
                int sc = rs.getInt("sc");
                String classes = rs.getString("class");
                sum2 += sc;
                answer.add(new PieChart.Data(classes, sc));
            }
            ///    stmt.executeUpdate(sql);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("失败啦！");
            alert.setContentText("没有学生！");
            alert.showAndWait();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        finally {
            DataAccess.closeConnection(rs);
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
        }


        return answer;
    }

    private XYChart.Series<String, Integer> getcnoData() {
        //ObservableList<XYChart<String,Integer>> Data=FXCollections.observableArrayList();
        sum3 = 0;

        ObservableList<String> cnoo = FXCollections.observableArrayList();
        ObservableList<Integer> scc = FXCollections.observableArrayList();
        //XYChart.Series answer = new XYChart.Series();
        XYChart.Series<String, Integer> answer = new XYChart.Series<>();
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql=null;
            rs = stmt.executeQuery("SELECT  cno,COUNT(sno) sc from Grade  GROUP BY cno;");
            int i = 0;
            while (rs.next()) {
                int sc = rs.getInt("sc");
                String cno = rs.getString("cno");
                sum3 += sc;
                scc.add(sc);
                cnoo.add(cno);
                answer.getData().add(new XYChart.Data<>(cnoo.get(i), scc.get(i)));

                i++;
                //series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
            }
            ///    stmt.executeUpdate(sql);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("失败啦！");
            alert.setContentText("没有学生选修课程！");
            alert.showAndWait();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        finally {cnodata = answer;
            DataAccess.closeConnection(rs);
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
        }

        return answer;

    }

    private XYChart.Series<String, Integer> getgradeData() {
        searchresult.setText("");
        int as = 0;
        int no = 0;
        //  ObservableList<String> score = FXCollections.observableArrayList();
        //  ObservableList<Integer> count = FXCollections.observableArrayList();
        //XYChart.Series answer = new XYChart.Series();
        XYChart.Series<String, Integer> answer = new XYChart.Series<>();
        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;
        try {
            conn= DataAccess.getConnection();
            stmt = conn.createStatement();//创建SQL语句
            String sql=null;
            rs = stmt.executeQuery("SELECT  * from Course  where cno='" + search.getText() + "';");
            if (rs.next()) {
                String cname = rs.getString("cname");
                String classes = rs.getString("class");
                tip.setText("课程名：" + cname + "   班级：" + classes);
            }
            try {
                ResultSet rs1 = stmt.executeQuery("SELECT  COUNT(sno) a from Grade  where cno='" + search.getText() + "'and score>-1 AND score<61;");
                if (rs1.next()) {
                    answer.getData().add(new XYChart.Data<>("0-60", rs1.getInt("a")));
                    as += rs1.getInt("a");
                    no = rs1.getInt("a");
                }
            } catch (Exception a) {
                answer.getData().add(new XYChart.Data<>("0-60", 0));
            }
            try {
                ResultSet rs1 = stmt.executeQuery("SELECT  COUNT(sno) a  from Grade  where cno='" + search.getText() + "'and score>60 AND score<71;");
                if (rs1.next()) {
                    answer.getData().add(new XYChart.Data<>("61-70", rs1.getInt("a")));
                    as += rs1.getInt("a");
                }
            } catch (Exception a) {
                answer.getData().add(new XYChart.Data<>("61-70", 0));
            }
            try {
                ResultSet rs1 = stmt.executeQuery("SELECT  COUNT(sno) a  from Grade  where cno='" + search.getText() + "'and score>70 AND score<81;");
                if (rs1.next()) {
                    answer.getData().add(new XYChart.Data<>("71-80", rs1.getInt("a")));
                    as += rs1.getInt("a");
                }
            } catch (Exception a) {
                answer.getData().add(new XYChart.Data<>("71-80", 0));
            }
            try {
                ResultSet rs1 = stmt.executeQuery("SELECT  COUNT(sno) a  from Grade  where cno='" + search.getText() + "'and score>80 AND score<91;");
                if (rs1.next()) {
                    answer.getData().add(new XYChart.Data<>("81-90", rs1.getInt("a")));
                    as += rs1.getInt("a");
                }
            } catch (Exception a) {
                answer.getData().add(new XYChart.Data<>("81-90", 0));
            }
            try {
                ResultSet rs1 = stmt.executeQuery("SELECT  COUNT(sno) a  from Grade  where cno='" + search.getText() + "'and score>90 AND score<101;");
                if (rs1.next()) {
                    answer.getData().add(new XYChart.Data<>("91-100", rs1.getInt("a")));
                    as += rs1.getInt("a");
                }
            } catch (Exception a) {
                answer.getData().add(new XYChart.Data<>("91-100", 0));
            }
            ///    stmt.executeUpdate(sql);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        finally {
            gradedata = answer;
            System.out.println(as);
            System.out.println(no);
            searchresult.setText(String.valueOf(df.format((as * 1.0 - no * 1.0) / as * 100.0)) + "%");
            DataAccess.closeConnection(rs);
            DataAccess.closeConnection(stmt);
            DataAccess.closeConnection(conn);
        }

        return answer;

    }

    public void reflesh1() {
        caption.setText("");
        pieChartsex.setData(getsexData());
        mousechsex();
    }

    public void reflesh2() {
        caption2.setText("");
        pieChartclass.setData(getclassData());
        mousechclass();

    }

    public void reflesh3() {
        caption3.setText("");
        cnoBar.getData().removeAll(cnodata);
        cnoBar.getData().add(getcnoData());

        mousechcno();

    }

    private void mousechsex() {
        for (final PieChart.Data data : pieChartsex.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX() - 100);
                            caption.setTranslateY(e.getSceneY() - 110);
                            caption.setText(String.valueOf(df.format(data.getPieValue() / sum1 * 100)) + "%");
                            System.out.println(e.getSceneX() + "  " + e.getSceneY());
                            System.out.println(caption.getText());
                        }
                    });
            data.getNode().setOnMouseEntered((MouseEvent e) -> {
                data.getNode().setEffect(new DropShadow());
                data.getNode().setScaleX(1.1);
                data.getNode().setScaleY(1.1);
            });
            data.getNode().setOnMouseExited((MouseEvent e) ->
            {
                data.getNode().setEffect(null);
                data.getNode().setScaleX(1.0);
                data.getNode().setScaleY(1.0);

            });
        }

    }

    private void mousechclass() {
        for (final PieChart.Data data : pieChartclass.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {

                            caption2.setTranslateX(e.getSceneX() - 100);
                            caption2.setTranslateY(e.getSceneY() - 110);
                            caption2.setText(String.valueOf(df.format(data.getPieValue() / sum2 * 100)) + "%\n(" + (int) data.getPieValue() + ")");
                            System.out.println(e.getSceneX() + "  " + e.getSceneY());
                            System.out.println(caption2.getText());
                        }
                    });
            data.getNode().setOnMouseEntered((MouseEvent e) -> {
                data.getNode().setEffect(new DropShadow());
                data.getNode().setScaleX(1.1);
                data.getNode().setScaleY(1.1);
            });
            data.getNode().setOnMouseExited((MouseEvent e) ->
            {
                data.getNode().setEffect(null);
                data.getNode().setScaleX(1.0);
                data.getNode().setScaleY(1.0);

            });
        }

    }

    private void mousechcno() {
        for (final XYChart.Series<String, Integer> series : cnoBar.getData()) {
            for (final XYChart.Data<String, Integer> data : series.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                caption3.setTranslateX(e.getSceneX() - 740);
                                caption3.setTranslateY(e.getSceneY() - 250);
                                Connection conn=null;
                                Statement stmt=null;
                                ResultSet rs=null;
                                try {
                                    conn= DataAccess.getConnection();
                                    stmt = conn.createStatement();//创建SQL语句
                                    String sql=null;
                                    rs = stmt.executeQuery("SELECT  * from Course  WHERE cno='" + data.getXValue() + "';");
                                    int i = 0;
                                    while (rs.next()) {
                                        String cname = rs.getString("cname");
                                        String classes = rs.getString("class");
                                        caption3.setText(cname + "\n" + classes);
                                    }

                                } catch (Exception b) {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("error");
                                    alert.setHeaderText("运行错误");
                                    alert.setContentText("请稍后再试");
                                    alert.showAndWait();
                                    System.err.println(b.getClass().getName() + ": " + b.getMessage());
                                    System.exit(0);
                                }
                                finally {
                                    DataAccess.closeConnection(rs);
                                    DataAccess.closeConnection(stmt);
                                    DataAccess.closeConnection(conn);
                                }


                            }
                        });
                data.getNode().setOnMouseEntered((MouseEvent e) -> {
                    data.getNode().setEffect(new DropShadow());
                    data.getNode().setScaleX(1.1);
                    data.getNode().setScaleY(1.1);
                });
                data.getNode().setOnMouseExited((MouseEvent e) ->
                {
                    data.getNode().setEffect(null);
                    data.getNode().setScaleX(1.0);
                    data.getNode().setScaleY(1.0);

                });
            }

        }

    }

    private void mousechgrade() {
        for (final XYChart.Series<String, Integer> series : gradeBar.getData()) {
            for (final XYChart.Data<String, Integer> data : series.getData()) {

                data.getNode().setOnMouseEntered((MouseEvent e) -> {
                    data.getNode().setEffect(new DropShadow());
                    data.getNode().setScaleX(1.1);
                    data.getNode().setScaleY(1.1);
                });
                data.getNode().setOnMouseExited((MouseEvent e) ->
                {
                    data.getNode().setEffect(null);
                    data.getNode().setScaleX(1.0);
                    data.getNode().setScaleY(1.0);

                });
            }

        }

    }

    public void setSearch() {

        if (search.getText() == null || search.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("失败啦！");
            alert.setContentText("没有选择课程号！");
            alert.showAndWait();
        } else {
                tip.setText("");
                Connection conn=null;
                Statement stmt=null;
                ResultSet rs=null;
                try {
                    conn= DataAccess.getConnection();
                    stmt = conn.createStatement();//创建SQL语句
                    String sql=null;
                 rs = stmt.executeQuery("SELECT  *  from Grade  where cno='" + search.getText() + "';");
                if (rs.next()) {

                    gradeBar.getData().removeAll(gradedata);
                    gradeBar.getData().add(getgradeData());
                    mousechgrade();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("error");
                    alert.setHeaderText("失败啦！");
                    alert.setContentText("没有学生选修该课程！");
                    alert.showAndWait();

                }

            } catch (Exception e) {

                    System.err.println(e.getClass().getName() + ": " + e.getMessage());

            }
            finally {
                DataAccess.closeConnection(rs);
                DataAccess.closeConnection(stmt);
                DataAccess.closeConnection(conn);
            }

        }


    }


}
