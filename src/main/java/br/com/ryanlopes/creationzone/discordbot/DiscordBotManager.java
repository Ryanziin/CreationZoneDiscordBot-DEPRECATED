package br.com.ryanlopes.creationzone.discordbot;

import br.com.ryanlopes.creationzone.discordbot.cheat.CheatManager;
import br.com.ryanlopes.creationzone.discordbot.commands.CheatOffCommand;
import br.com.ryanlopes.creationzone.discordbot.commands.CheatOnCommand;
import br.com.ryanlopes.creationzone.discordbot.commands.CommandTestCommand;
import br.com.ryanlopes.creationzone.discordbot.configuration.BotConfiguration;
import br.com.ryanlopes.creationzone.discordbot.database.DatabaseManager;
import br.com.ryanlopes.creationzone.discordbot.registers.RegistersManager;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class DiscordBotManager {
    
    private static BotConfiguration botConfiguration;
    
    private static JDA jda;
    
    private static List<Long> users_with_action;
    
    // MANAGERS
    private static DatabaseManager databaseManager;
    private static CheatManager cheatManager;
    private static RegistersManager registersManager;
    
    public DiscordBotManager(){
        botConfiguration = new BotConfiguration();
        
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.setToken(botConfiguration.getDiscordToken());
        jdaBuilder.setAutoReconnect(true);
        
        try {
            jda = jdaBuilder.buildBlocking();
        } catch(LoginException exception){
            exception.printStackTrace();
        } catch(InterruptedException exception){
            exception.printStackTrace();
        }
        
        users_with_action = new ArrayList<>();
        
        databaseManager = new DatabaseManager();
        cheatManager = new CheatManager();
        registersManager = new RegistersManager();
    }
    
    public static void initialize(){
        getJDA().getPresence().setPresence(Game.playing("Creation Zone - IP: cz.ryanlopes.com.br"), true);
        
        databaseManager.initialize();
        registersManager.initialize();
        
        registerCommands();
    }
    
    private static void registerCommands(){
        new CheatOffCommand();
        new CheatOnCommand();
        new CommandTestCommand();
    }
    
    public static JDA getJDA(){
        return jda;
    }
    
    public static Guild getGuild(){
        return getJDA().getGuildById(583739315402833940L);
    }
    
    public static void addUserAction(User user){
        users_with_action.add(user.getIdLong());
    }
    
    public static boolean containsUserAction(User user){
        return users_with_action.contains(user.getIdLong());
    }
    
    public static void removeUserAction(User user){
        users_with_action.remove(user.getIdLong());
    }
    
    public static DatabaseManager getDatabaseManager(){
        return databaseManager;
    }
    
    public static CheatManager getCheatManager(){
        return cheatManager;
    }
    
    public static RegistersManager getRegistersManager(){
        return registersManager;
    }
    
}
