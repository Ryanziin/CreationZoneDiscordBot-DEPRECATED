package br.com.ryanlopes.creationzone.discordbot.registers.data;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import br.com.ryanlopes.creationzone.discordbot.registers.RegistersSecurity;

public class RegisterData {
    
    private int user_id;
    private long discord_user_id;
    private String password;
    private boolean blocked;
    private long registered_at;
    
    public RegisterData(int user_id, long discord_user_id, String password, boolean blocked, long registered_at){
        this.user_id = user_id;
        this.discord_user_id = discord_user_id;
        this.password = password;
        this.blocked = blocked;
        this.registered_at = registered_at;
    }
    
    public int getUserID(){
        return this.user_id;
    }
    
    public long getDiscordUserID(){
        return this.discord_user_id;
    }
    
    public String getPasswordDecrypted(){
        RegistersSecurity registersSecurity = DiscordBotManager.getRegistersManager().getRegistersSecurity();
        
        return registersSecurity.decrypt(this.password);
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public boolean hasBlocked(){
        return this.blocked;
    }
    
    public long getRegisteredAt(){
        return this.registered_at;
    }
    
}
