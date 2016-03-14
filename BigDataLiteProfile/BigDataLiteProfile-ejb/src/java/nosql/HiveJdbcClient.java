/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 *
 * @author oracle
 */
public class HiveJdbcClient {
    
  private static String driverName = "org.apache.hive.jdbc.HiveDriver";
 
  public static void main(String[] args) throws SQLException {
    try {
        
          Class.forName(driverName);

          Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000", "oracle", "welcome1");
          Statement stmt = con.createStatement();
          String tableName = "Utilisateur";
          
          //creation tables externes Utilisateur depuis le KVSTORE ORACLE
          String usrExtHive ="CREATE EXTERNAL TABLE "+tableName+"  (IDUSER int, NOM string, PRENOM string, "
                  + "EMAIL string, profil_id int) STORED BY 'oracle.kv.hadoop.hive.table.TableStorageHandler' "
                  + "TBLPROPERTIES (\"oracle.kv.kvstore\" = \"kvstore\",\"oracle.kv.hosts\" = "
                  + "\"bigdatalite.localdomain:5000\",\"oracle.kv.hadoop.hosts\" = "
                  + "\"bigdatalite.localdomain/127.0.0.1\",\"oracle.kv.tableName\" = \""+tableName+"\")";
          
          stmt.execute("drop table " + tableName); //utiliser execute au lieu de executeQuery....
          
            //stmt.execute("create table " + tableName + " (key int, value string)");
          // show tables
          stmt.execute(usrExtHive);
          String sql = "show tables '" + tableName + "'";
          System.out.println("Running: " + sql);
          ResultSet res = stmt.executeQuery(sql);
          if (res.next()) {
            System.out.println(res.getString(1));
          }
          // describe table
          sql = "describe " + tableName;
          System.out.println("Running: " + sql);
          res = stmt.executeQuery(sql);
          while (res.next()) {
            System.out.println(res.getString(1) + "\t" + res.getString(2));
          }

          // select * query
          sql = "select * from " + tableName;
          System.out.println("Running: " + sql);
          res = stmt.executeQuery(sql);
          while (res.next()) {
            System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
          }
 
    } catch (ClassNotFoundException e) {
      
      System.out.println(e.getMessage());
      System.exit(1);
    } catch (SQLException sqle) {
        System.out.println(sqle.getMessage());
    }
  }
}
