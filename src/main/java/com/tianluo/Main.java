package com.tianluo;

import com.alibaba.excel.util.StringUtils;
import com.tianluo.entity.DataBaseInfo;
import com.tianluo.tool.DataBaseUtils;
import com.tianluo.tool.JsonToEntity;
import com.tianluo.tool.OracleDateBaseUtils;

public class Main {
    public static void main(String[] args) {
        //mysql
        DataBaseUtils dataBaseUtils=new DataBaseUtils();
        dataBaseUtils.exportExcel("C:\\project\\files\\test.json");
        //oracle
        OracleDateBaseUtils oracleDateBaseUtils=new OracleDateBaseUtils();
        oracleDateBaseUtils.exportExcel("C:\\project\\files\\test.json");
//        String fileName=System.getProperty("jsonKey");
//        if(StringUtils.isEmpty(fileName))
//        {
//            System.out.println("请设置-DjsonKey=json位置");
//            return;
//        }
//        dataBaseUtils.exportExcel(fileName);
    }
}