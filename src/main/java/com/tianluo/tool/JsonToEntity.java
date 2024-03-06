package com.tianluo.tool;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import com.tianluo.entity.DataBaseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonToEntity {
    private static final Logger logger= LoggerFactory.getLogger(JsonToEntity.class);
     public String getFileInfo(String fileName)
     {
         String content="";
         StringBuilder strb=new StringBuilder();
         File file=new File(fileName);
         InputStreamReader streamReader = null;
         try {
             streamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(streamReader);

             while ((content = bufferedReader.readLine()) != null)
                 strb.append(content);
         } catch (FileNotFoundException e) {
             logger.error("读取文件流异常",e);
         } catch (IOException e) {
             logger.error("读取文件内容出错",e);
         }
         return strb.toString();
     }
    public List<DataBaseInfo> getDataBaseInfos(String jsonfile)
    {
        String fileInfo=getFileInfo(jsonfile);
        JSONObject jsonObject=JSONObject.parseObject(fileInfo);
        JSONArray jsonArray=jsonObject.getJSONArray("databaseList");
        List<DataBaseInfo> dataBaseInfoList=new ArrayList<>();
        if(jsonArray!=null)
        {
            for(Object o:jsonArray)
            {
                JSONObject jsonObject1=JSONObject.from(o);
                DataBaseInfo dataBaseInfo=jsonObject1.toJavaObject(DataBaseInfo.class);
                dataBaseInfoList.add(dataBaseInfo);
            }
        }
        return dataBaseInfoList;
    }
}
