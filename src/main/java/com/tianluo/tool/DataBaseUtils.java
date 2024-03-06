package com.tianluo.tool;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;

import com.tianluo.entity.DataBaseInfo;
import com.tianluo.entity.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取数据库表信息，并生成文档。
 * 参考网页: https://blog.csdn.net/hyg0811/article/details/100520545
  */

public class DataBaseUtils {
    private static final Logger logger= LoggerFactory.getLogger(DataBaseUtils.class);

    /**
     * 数据库开启链接
     * @param dataBaseInfo： 数据库配置信息
     * @return Connection: 数据库连接
     */
    public   Connection OpenConnection(DataBaseInfo dataBaseInfo)
    {
        Connection con=null;
       try{
            Class.forName(dataBaseInfo.getDriver());
            con= DriverManager.getConnection(dataBaseInfo.getUrl(),dataBaseInfo.getUsername(),dataBaseInfo.getPassword());
       }
       catch (ClassNotFoundException e)
       {
           logger.error("找不到数据库连接驱动类",e);
       }
       catch(SQLException e)
       {
           logger.error("数据库加载链接错误",e);
       }
           return con;
    }

    /**
     * 数据库连接关闭
     * @param con ： 开启的数据库连接
     */
    public  void closeConnection(Connection con)
    {
        try {
           if(con!=null)
           {
               con.close();
           }
        } catch (SQLException e) {
            logger.error("数据库连接关闭异常！",e);
        }
    }

    /**
     * 获取数据库的所有表名
     * @param dataBaseInfo: 数据库基本配置信息
     * @return List 数据库表
     */
    public List<String> getAllTables(DataBaseInfo dataBaseInfo){
        List<String> tableList=new ArrayList<>();
        Connection con=OpenConnection(dataBaseInfo);
        ResultSet rs=null;
        try {
            DatabaseMetaData db=con.getMetaData();
            rs=db.getTables(dataBaseInfo.getDataBaseName(),null ,null,new String[]{"TABLE"});
            while(rs.next())
            {
                tableList.add(rs.getString(3));
            }
        } catch (SQLException e) {
            logger.error("获取数据库表异常",e);
        }
        finally {
            try {
                rs.close();
                closeConnection(con);
            } catch (SQLException e) {
                logger.error("关闭结果集异常",e);
            }
        }
        return tableList;
    }
    public List<TableInfo> getTableInfos(DataBaseInfo dataBaseInfo, String tableName)
    {
        if(StringUtils.isEmpty(dataBaseInfo.getDataBaseCn()))
        {
            dataBaseInfo.setDataBaseCn("test");
        }
        List<TableInfo> columnList=new ArrayList<>();
        Connection con=OpenConnection(dataBaseInfo);
        PreparedStatement pStamt=null;
        ResultSet rs=null;
        String tableSql=dataBaseInfo.getExecsql()+tableName;
        try {
            pStamt=con.prepareStatement(tableSql);
            ResultSetMetaData rsmd=pStamt.getMetaData();
            int size= rsmd.getColumnCount();
            rs= pStamt.executeQuery("show full columns from "+tableName);
            for (int i = 0; i <size ; i++) {
                TableInfo tableInfo=new TableInfo();
                tableInfo.setDataBaseName(dataBaseInfo.getDataBaseName());
                tableInfo.setTableName(tableName);
                tableInfo.setTableNameCn(dataBaseInfo.getDataBaseCn());
                tableInfo.setColumn(rsmd.getColumnName(i+1));
                tableInfo.setColumnType(rsmd.getColumnTypeName(i+1));
                tableInfo.setColumnLength(rsmd.getColumnDisplaySize(i+1));
                if(rs.next())
                {
                    tableInfo.setComment(rs.getString("Comment"));
                }
                columnList.add(tableInfo);
            }
        } catch (SQLException e) {
            logger.error("数据库设置sql异常",e);
        }
        finally {
            if(pStamt!=null&&rs!=null)
            {
                try {
                    pStamt.close();
                    rs.close();
                    closeConnection(con);
                } catch (SQLException e) {
                  logger.error("获取列名时关闭连接失败！",e);
                }
            }
        }
        return columnList;
    }

     public void generateExcel(List<TableInfo> tableInfos,String fileName)
     {
         EasyExcel.write(fileName, TableInfo.class).sheet("模板").doWrite(tableInfos);
     }
     public void exportExcel(String fileName)
     {
         JsonToEntity jsonToEntity=new JsonToEntity();
         List<DataBaseInfo> dataBaseInfoList=jsonToEntity.getDataBaseInfos(fileName);
         for(DataBaseInfo a:dataBaseInfoList)
         {
             List<String> tableNames=getAllTables(a);
             List<TableInfo> tableInfoList=new ArrayList<>();
             for(String tables:tableNames){
                 tableInfoList.addAll(getTableInfos(a,tables));
         }
             String usrDir=System.getProperty("user.dir");
             StringBuilder excelName=new StringBuilder(usrDir).append("/");
             excelName.append(a.getDataBaseCn()).append(System.currentTimeMillis()).append(".xlsx");
             generateExcel(tableInfoList,excelName.toString());
         }
         System.out.println("生成完毕！");
     }
}
