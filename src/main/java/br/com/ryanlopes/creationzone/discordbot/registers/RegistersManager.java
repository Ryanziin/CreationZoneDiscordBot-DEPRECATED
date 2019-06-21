package br.com.ryanlopes.creationzone.discordbot.registers;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import br.com.ryanlopes.creationzone.discordbot.database.DatabaseManager;
import br.com.ryanlopes.creationzone.discordbot.registers.commands.RegisterCommand;
import br.com.ryanlopes.creationzone.discordbot.registers.commands.UnregisterCommand;
import br.com.ryanlopes.creationzone.discordbot.registers.listeners.UnregisterListener;
import br.com.ryanlopes.creationzone.discordbot.registers.repositories.RegistersRepository;
import br.com.ryanlopes.creationzone.discordbot.registers.repositories.UsersRepository;
import net.dv8tion.jda.core.JDA;

public class RegistersManager {
    
    private long CHANNEL_ID_LOG_REGISTERS;
    
    // SECURITY
    private RegistersSecurity registersSecurity;
    
    // REPOSITORIES
    private UsersRepository usersRepository;
    private RegistersRepository registersRepository;
    
    public RegistersManager(){
        this.CHANNEL_ID_LOG_REGISTERS = 591293259733532674L;
        
        this.registersSecurity = new RegistersSecurity("kYtRdFgH37gTeWqA");
    }
    
    public void initialize(){
        loadRepositories();
        
        registerListeners();
        registerCommands();
    }
    
    private void loadRepositories(){
        DatabaseManager databaseManager = DiscordBotManager.getDatabaseManager();
        
        this.usersRepository = new UsersRepository(databaseManager.getMariaDB("rlopes_creationzone"));
        this.registersRepository = new RegistersRepository(databaseManager.getMariaDB("rlopes_creationzone"));
    }
    
    private void registerListeners(){
        JDA jda = DiscordBotManager.getJDA();
        
        jda.addEventListener(new UnregisterListener());
    }
    
    private void registerCommands(){
        new RegisterCommand();
        new UnregisterCommand();
    }
    
    public long getChannelIDLogRegisters(){
        return this.CHANNEL_ID_LOG_REGISTERS;
    }
    
    public RegistersSecurity getRegistersSecurity(){
        return this.registersSecurity;
    }
    
    public UsersRepository getUsersRepository(){
        return this.usersRepository;
    }
    
    public RegistersRepository getRegistersRepository(){
        return this.registersRepository;
    }
    
}
