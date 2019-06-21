package br.com.ryanlopes.creationzone.discordbot.cheat;

import br.com.ryanlopes.creationzone.discordbot.cheat.data.Cheat;

public class CheatManager {
    
    private Cheat cheat;
    
    public CheatManager(){
        
    }
    
    public Cheat registerCheat(String client_token, long user_id, long guild_id, long channel_id){
        this.cheat = new Cheat(client_token, user_id, guild_id, channel_id);
        
        return this.cheat;
    }
    
    public Cheat getCheat(){
        return this.cheat;
    }
    
    public void destroy(){
        this.cheat = null;
    }
    
}
