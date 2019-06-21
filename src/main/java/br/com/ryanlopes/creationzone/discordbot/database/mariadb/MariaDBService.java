package br.com.ryanlopes.creationzone.discordbot.database.mariadb;

import br.com.ryanlopes.creationzone.discordbot.database.DatabaseConfiguration;
import java.util.HashMap;

public class MariaDBService {
    
    private HashMap<String, MariaDB> connections;
    
    private DatabaseConfiguration databaseConfiguration;
    
    public MariaDBService(DatabaseConfiguration databaseConfiguration){
        this.connections = new HashMap<>();
        
        this.databaseConfiguration = databaseConfiguration;
        
        this.createConnection("rlopes_creationzone");
    }
    
    public void connect(){
        System.out.println("[MariaDB]: Creating connections...");
        this.connections.values().forEach((mariaDB) -> {
            mariaDB.getConnection();
        });
    }
    
    public void updateConnections(){
        System.out.println("[MariaDB]: Checking connections...");
        this.connections.values().forEach((mariaDB) -> {
            mariaDB.getConnection();
        });
    }
    
    public void disconnect(){
        System.out.println("[MariaDB]: Disconnecting...");
        this.connections.values().forEach((mariaDB) -> {
            mariaDB.closeConnection();
        });
    }
    
    public MariaDB createConnection(String database){
        this.connections.put(database, new MariaDB(this.databaseConfiguration.getMariaDBHost(), 
                this.databaseConfiguration.getMariaDBUser(), 
                this.databaseConfiguration.getMariaDBPassword(), database));
        this.connections.get(database).getConnection();
        
        return this.connections.get(database);
    }
    
    public MariaDB getMariaDB(String database){
        return !this.connections.containsKey(database) ? createConnection(database) : this.connections.get(database);
    }
    
}
