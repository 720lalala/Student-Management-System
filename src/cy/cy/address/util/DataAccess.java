package cy.cy.address.util;
import java.sql.*;
public class DataAccess {
    public static Connection getConnection()
    {
        Connection Conn=null;
        try {
            //注册JDBC程序            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Conn = DriverManager.getConnection("jdbc:mysql://localhost/fucha?useSSL=FALSE&serverTimezone=UTC","root","123456");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Conn;
    }
    //关闭数据库链接
    public static void closeConnection(Connection Conn)
    {
        if(Conn!=null)
        {
            try {
                Conn.close();
                Conn=null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //关闭发送SQL语句
    public static void closeConnection(Statement stmt)
    {
        if(stmt!=null)
        {
            try {
                stmt.close();
                stmt=null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //关闭结果集
    public static void closeConnection(ResultSet rs)
    {
        if(rs!=null)
        {
            try {
                rs.close();
                rs=null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
