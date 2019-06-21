package br.com.ryanlopes.creationzone.discordbot.registers.repositories;

import br.com.ryanlopes.creationzone.discordbot.database.mariadb.MariaDB;
import br.com.ryanlopes.creationzone.discordbot.registers.data.RegisterData;
import br.com.ryanlopes.creationzone.discordbot.registers.data.UserData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistersRepository {
    
    private MariaDB mariaDB;
    private String table_name;
    
    private String CREATE_TABLE;
    
    private String SQL_INSERT;
    private String SQL_SELECT_PER_USER_OR_DISCORD_ID;
    private String SQL_DELETE;
    
    public RegistersRepository(MariaDB mariaDB){
        this.mariaDB = mariaDB;
        this.table_name = "registers";
        
        this.CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s("
                + "user_id INT NOT NULL, "
                + "discord_user_id BIGINT(32) NOT NULL, "
                + "password LONGTEXT NOT NULL, "
                + "blocked BOOLEAN NOT NULL, "
                + "registered_at BIGINT(14) NOT NULL, "
                + "PRIMARY KEY(user_id, discord_user_id));";
        
        this.SQL_INSERT = "INSERT INTO " + this.table_name + "(user_id,discord_user_id,password,"
                + "blocked,registered_at) VALUES(?,?,?,?,?)";
        this.SQL_SELECT_PER_USER_OR_DISCORD_ID = "SELECT * FROM " + this.table_name + " WHERE user_id=? OR discord_user_id=?";
        this.SQL_DELETE = "DELETE FROM " + this.table_name + " WHERE discord_user_id=?";
        
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
    
    public void insert(RegisterData registerData) throws SQLException {
        PreparedStatement preparedStatement = this.mariaDB.getConnection().
                prepareStatement(this.SQL_INSERT);
        
        preparedStatement.setInt(1, registerData.getUserID());
        preparedStatement.setLong(2, registerData.getDiscordUserID());
        preparedStatement.setString(3, registerData.getPassword());
        preparedStatement.setBoolean(4, registerData.hasBlocked());
        preparedStatement.setLong(5, registerData.getRegisteredAt());
        preparedStatement.executeUpdate();
        
        preparedStatement.close();
    }
    
    public RegisterData select(long id) throws SQLException {
        RegisterData registerData = null;
        
        PreparedStatement preparedStatement = this.mariaDB.getConnection().
                prepareStatement(this.SQL_SELECT_PER_USER_OR_DISCORD_ID);
        
        preparedStatement.setInt(1, (int) id);
        preparedStatement.setLong(2, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if(resultSet.next()){
            registerData = new RegisterData(resultSet.getInt("user_id"), 
                    resultSet.getLong("discord_user_id"), 
                    resultSet.getString("password"), 
                    resultSet.getBoolean("blocked"), 
                    resultSet.getLong("registered_at"));
        }
        
        preparedStatement.close();
        resultSet.close();
        
        return registerData;
    }
    
    public void delete(long discord_user_id) throws SQLException {
        PreparedStatement preparedStatement = this.mariaDB.getConnection().
                prepareStatement(this.SQL_DELETE);
        
        preparedStatement.setLong(1, discord_user_id);
        preparedStatement.executeUpdate();
        
        preparedStatement.close();
    }
    
}
