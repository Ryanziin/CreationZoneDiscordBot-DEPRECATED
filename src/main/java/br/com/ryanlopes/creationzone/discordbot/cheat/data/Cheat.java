package br.com.ryanlopes.creationzone.discordbot.cheat.data;

import br.com.ryanlopes.creationzone.discordbot.cheat.listeners.CheatListeners;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Cheat {
    
    private JDA jda;
    private String CLIENT_TOKEN;
    
    private long USER_ID;
    private long GUILD_ID;
    private long CHANNEL_ID;
    
    private long started_at;
    
    public Cheat(String client_token, long user_id, long guild_id, long channel_id){
        this.CLIENT_TOKEN = client_token;
        
        this.USER_ID = user_id;
        this.GUILD_ID = guild_id;
        this.CHANNEL_ID = channel_id;
    }
    
    public void start(){
        JDABuilder jdaBuilder = new JDABuilder(AccountType.CLIENT);
        jdaBuilder.setToken(this.CLIENT_TOKEN);
        jdaBuilder.setAutoReconnect(true);
        
        try {
            this.jda = jdaBuilder.buildBlocking();
        } catch(LoginException exception){
            exception.printStackTrace();
        } catch(InterruptedException exception){
            exception.printStackTrace();
        }
        
        registerListeners();
        
        this.started_at = System.currentTimeMillis();
    }
    
    private void registerListeners(){
        this.jda.addEventListener(new CheatListeners());
    }
    
    public void stop(){
        this.jda.shutdown();
    }
    
    public JDA getJDA(){
        return this.jda;
    }
    
    public long getUserID(){
        return this.USER_ID;
    }
    
    public long getGuildID(){
        return this.GUILD_ID;
    }
    
    public long getChannelID(){
        return this.CHANNEL_ID;
    }
    
}
