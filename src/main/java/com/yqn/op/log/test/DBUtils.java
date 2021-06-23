package com.yqn.op.log.test;

import java.sql.*;

/**
 * @author huayuanlin
 * @date 2021/06/22 23:34
 * @desc the class desc
 */
public class DBUtils {

    // 参数定义
    private static String url = "jdbc:mysql://localhost:3306/mytest";
    // 数据库地址
    // 数据库用户名
    private static String username = "root";
    // 数据库密码
    private static String password = "root";

    private DBUtils() {

    }

    // 加载驱动
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("驱动加载出错!");
        }
    }

    // 获得连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // 释放连接
    public static void free(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close(); // 关闭结果集
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close(); // 关闭Statement
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close(); // 关闭连接
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
