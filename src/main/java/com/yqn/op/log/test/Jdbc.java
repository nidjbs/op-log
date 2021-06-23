package com.yqn.op.log.test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huayuanlin
 * @date 2021/06/22 23:30
 * @desc the class desc
 */
public class Jdbc {


    public static void main(String[] args) {
        DbMeta();

        List<Map<String, Object>> datas = read("select * from users where id < 3");
        System.out.println("----------List数据返回------------");
        System.out.print(datas);
    }

    // 通过结果集元数据封装List结果集
    public static List<Map<String, Object>> read(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = ps.getMetaData();

            // 取得结果集列数
            int columnCount = rsmd.getColumnCount();

            // 构造泛型结果集
            List<Map<String, Object>> datas = new ArrayList<>();
            Map<String, Object> data = null;

            // 循环结果集
            while (rs.next()) {
                data = new HashMap<>(16);
                // 每循环一条将列名和列值存入Map
                for (int i = 1; i < columnCount; i++) {
                    data.put(rsmd.getColumnLabel(i), rs.getObject(rsmd
                            .getColumnLabel(i)));
                }
                // 将整条数据的Map存入到List中
                datas.add(data);
            }
            return datas;
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            DBUtils.free(rs, ps, conn);
        }
    }


    private static List<Map<String, Object>> get(ResultSetMetaData resultSetMetaData, ResultSet resultSet) throws SQLException {
        int columnCount = resultSetMetaData.getColumnCount();
        List<Map<String, Object>> datas = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> data = new HashMap<>(16);
            // 每循环一条将列名和列值存入Map
            for (int i = 1; i < columnCount; i++) {
                data.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(resultSetMetaData
                        .getColumnLabel(i)));
            }
            // 将整条数据的Map存入到List中
            datas.add(data);
        }
        return null;
    }

    // 通过数据库元数据获得服务器信息
    public static void DbMeta() {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            DatabaseMetaData dbma = conn.getMetaData();
            System.out.println("----------数据库信息------------");
            System.out.println("数据库名称: " + dbma.getDatabaseProductName());
            System.out.println("驱动版本: " + dbma.getDriverVersion());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
