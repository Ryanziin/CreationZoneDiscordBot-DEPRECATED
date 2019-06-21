package br.com.ryanlopes.creationzone.discordbot.database;

import br.com.ryanlopes.creationzone.discordbot.database.mariadb.MariaDB;
import br.com.ryanlopes.creationzone.discordbot.database.mariadb.MariaDBService;

public class DatabaseManager {
    
    private DatabaseConfiguration databaseConfiguration;
    
    private MariaDBService mariaDBService;
    
    public DatabaseManager(){
        this.databaseConfiguration = new DatabaseConfiguration();
    }
    
    public void initialize(){
        this.mariaDBService = new MariaDBService(databaseConfiguration);
    }
    
    public void unitialize(){
        this.mariaDBService.disconnect();
        this.mariaDBService = null;
        
        this.databaseConfiguration = null;
    }
    
    public MariaDBService getMariaDBService(){
        return this.mariaDBService;
    }
    
    public MariaDB getMariaDB(String database){
        return this.getMariaDBService().getMariaDB(database);
    }
    
}
