package br.com.ryanlopes.creationzone.discordbot.registers.repositories;

import br.com.ryanlopes.creationzone.discordbot.database.mariadb.MariaDB;
import br.com.ryanlopes.creationzone.discordbot.registers.data.UserData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersRepository {
    
    private MariaDB mariaDB;
    private String table_name;
    
    private String CREATE_TABLE;
    
    private String SQL_INSERT;
    private String SQL_SELECT_PER_ID;
    private String SQL_SELECT_PER_NICK_OR_UUID;
    private String SQL_UPDATE;
    
    public UsersRepository(MariaDB mariaDB){
        this.mariaDB = mariaDB;
        this.table_name = "users";
        
        this.CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s("
                + "id INT NOT NULL AUTO_INCREMENT, "
                + "nick VARCHAR(16) NOT NULL, "
                + "uniqueId VARCHAR(36) NOT NULL, "
                + "created_at BIGINT(14) NOT NULL, "
                + "PRIMARY KEY(id, nick, uniqueId));";
        
        this.SQL_INSERT = "INSERT INTO " + this.table_name + "(nick,uniqueId,created_at) "
                + "VALUES(?,?,?)";
        this.SQL_SELECT_PER_ID = "SELECT * FROM " + this.table_name + " WHERE id=?";
        this.SQL_SELECT_PER_NICK_OR_UUID = "SELECT * FROM " + this.table_name + " WHERE nick=? OR uniqueId=?";
        this.SQL_UPDATE = "UPDATE " + this.table_name + " SET nick=?, "
                + "uniqueId=?, created_at=? WHERE id=?";
        
        createTable();
    }
    
    private void createTable(){
        try {
            PreparedStatement preparedStatement = this.mariaDB.getConnection().prepareStatement(
                    String.format(CREATE_TABLE, table_name));
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch(SQLException e){
            System.out.println("[MySQL]: The " + table_name + " table could not be created.");
            e.printStackTrace();
        }
    }
    
    public void insert(UserData user) throws SQLException {
        PreparedStatement preparedStatement = this.mariaDB.getConnection().
                prepareStatement(this.SQL_INSERT);
        
        preparedStatement.setString(1, user.getNick());
        preparedStatement.setString(2, user.getUniqueId());
        preparedStatement.setLong(3, user.getCreatedAt());
        preparedStatement.executeUpdate();
        
        preparedStatement.close();
    }
    
    public void update(UserData user) throws SQLException {
        PreparedStatement preparedStatement = this.mariaDB.getConnection().
                prepareStatement(this.SQL_UPDATE);
        
        preparedStatement.setString(1, user.getNick());
        preparedStatement.setString(2, user.getUniqueId());
        preparedStatement.setLong(3, user.getCreatedAt());
        preparedStatement.setInt(4, user.getID());
        preparedStatement.executeUpdate();
        
        preparedStatement.close();
    }
    
    public UserData select(int id) throws SQLException {
        UserData user = null;
        
        PreparedStatement preparedStatement = this.mariaDB.getConnection().
                prepareStatement(this.SQL_SELECT_PER_ID);
        
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if(resultSet.next()){
            user = new UserData(resultSet.getInt("id"), 
                    resultSet.getString("nick"), 
                    resultSet.getString("uniqueId"), 
                    resultSet.getLong("created_at"));
        }
        
        preparedStatement.close();
        resultSet.close();
        
        return user;
    }
    
    public UserData select(String nick_or_uniqueId) throws SQLException {
        UserData user = null;
        
        PreparedStatement preparedStatement = this.mariaDB.getConnection().
                prepareStatement(this.SQL_SELECT_PER_NICK_OR_UUID);
        
        preparedStatement.setString(1, nick_or_uniqueId);
        preparedStatement.setString(2, nick_or_uniqueId);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if(resultSet.next()){
            user = new UserData(resultSet.getInt("id"), 
                    resultSet.getString("nick"), 
                    resultSet.getString("uniqueId"), 
                    resultSet.getLong("created_at"));
        }
        
        preparedStatement.close();
        resultSet.close();
        
        return user;
    }
    
}
