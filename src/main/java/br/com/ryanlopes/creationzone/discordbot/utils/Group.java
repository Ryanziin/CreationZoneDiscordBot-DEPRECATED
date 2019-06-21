package br.com.ryanlopes.creationzone.discordbot.utils;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import net.dv8tion.jda.core.entities.Role;

public enum Group {
    
    // STAFF
    MASTER(55, "A", "§6", "Master", 589196616032976897L),
    MANAGER(50, "B", "§4", "Gerente", 589198096492134460L),
    ADMIN(45, "C", "§c", "Admin", 589196867049488385L),
    MODERATOR(40, "D", "§2", "Moderador", 589198126271823873L),
    HELPER(35, "E", "§e", "Ajudante", 589197941869248512L),
    
    // PARTNERS
    BUILDER(30, "F", "§a", "Construtor", -1),
    YOUTUBER(25, "G", "§c", "YouTuber", -1),
    
    // VIPS
    MVP2(20, "H", "§b", "MVP+", -1),
    MVP(15, "I", "§6", "MVP", -1),
    VIP(10, "J", "§a", "VIP", -1),
    
    // DEFAULT GROUP
    DEFAULT(1, "Z", "§7", null, 583739315402833940L);
    
    private int id;
    private String order;
    private String color;
    private String display_name;
    private long discord_id;
    
    Group(int id, String order, String color, String display_name, long discord_id){
        this.id = id;
        this.order = order;
        this.color = color;
        this.display_name = display_name;
        this.discord_id = discord_id;
    }
    
    public String getDisplayName(){
        return this.display_name;
    }
    
    public Role getRole(){
        return DiscordBotManager.getGuild().getRoleById(this.discord_id);
    }
    
}
