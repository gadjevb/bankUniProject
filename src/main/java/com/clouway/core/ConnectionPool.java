package com.clouway.core;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class ConnectionPool {
  private static BasicDataSource source = null;

  public static synchronized BasicDataSource get() {
    if (source == null) {
      source = new BasicDataSource();
      String host = "localhost";
      String user = "root";
      String pass = "clouway.com";
      String dbName = "bankdb";

      source.setDriverClassName("com.mysql.jdbc.Driver");
      source.setUsername(user);
      source.setPassword(pass);
      source.setUrl("jdbc:mysql://" + host + "/" + dbName + "?autoReconnect=true&useSSL=false");
      source.setInitialSize(3);
    }
    return source;
  }
}
