package br.com.ryanlopes.creationzone.discordbot.database.mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDB {
    
    private Connection connection;
    private final String database;
    
    private final String host;
    private final String user;
    private final String password;
    
    public MariaDB(String host, String user, String password, String database){
        this.database = database;
        
        this.host = host;
        this.user = user;
        this.password = password;
    }
    
    private Connection createConnection(){
        try {
            String connStr = "jdbc:mariadb://" + this.host + ":3306/" + this.database + "?autoReconnect=true";
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(connStr, this.user, this.password);
        } catch(Exception ex){
            ex.printStackTrace();
        }
        
        return this.connection;
    }
    
    public void closeConnection(){
        try {
            this.connection.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public synchronized Connection getConnection(){
        try {
            if(this.connection == null){
                connection = createConnection();
                connection.setAutoCommit(true);
                System.out.println("[MariaDB]: Conexão com o database " + this.database + " criada.");
            }
        } catch(SQLException ex){
            System.out.println("[MariaDB]: Não foi possível estabelecer conexão com o database " + this.database + ".");
            ex.printStackTrace();
        }
        
        return this.connection;
    }
    
}