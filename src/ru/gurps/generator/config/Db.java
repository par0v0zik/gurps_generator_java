package ru.gurps.generator.config;

import org.h2.jdbc.JdbcSQLException;
import ru.gurps.generator.Main;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;


public class Db {
    public static Connection connect = null;
    
    protected static void createConnection(){
        if(connect != null){
            return;
        }
        
        try {
            Class.forName("org.h2.Driver").newInstance();
            String parent = "\\w*.jar";
            String s = URLDecoder.decode(ru.gurps.generator.Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll(parent, ""), "UTF-8");
            connect = DriverManager.getConnection("jdbc:h2:" + s + "db/gurps", "sa", "");
        } catch(JdbcSQLException e){
            if(e.getErrorCode() == 90020){
                System.err.println(Main.locale.getString("app_is_running"));
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
