package com.tianluo.entity;

import lombok.Data;

/**
 * 数据库信息实体类
 */
@Data
public class DataBaseInfo {
    String driver;
    String username;
    String password;
    String url;
    String execsql;
    String dataBaseName;
    String dataBaseCn;
}
