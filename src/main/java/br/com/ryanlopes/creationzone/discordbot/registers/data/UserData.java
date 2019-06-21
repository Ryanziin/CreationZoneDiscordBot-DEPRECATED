package br.com.ryanlopes.creationzone.discordbot.registers.data;

public class UserData {
    
    private int id;
    private String nick, uniqueId;
    private long created_at;
    
    public UserData(int id, String nick, String uniqueId, long created_at){
        this.id = id;
        this.nick = nick;
        this.uniqueId = uniqueId;
        this.created_at = created_at;
    }
    
    public int getID(){
        return this.id;
    }
    
    public void setNick(String nick){
        this.nick = nick;
    }
    
    public String getNick(){
        return this.nick;
    }
    
    public String getUniqueId(){
        return this.uniqueId;
    }
    
    public long getCreatedAt(){
        return this.created_at;
    }
    
}
