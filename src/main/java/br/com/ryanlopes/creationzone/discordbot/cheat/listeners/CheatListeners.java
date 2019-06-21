package br.com.ryanlopes.creationzone.discordbot.cheat.listeners;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import br.com.ryanlopes.creationzone.discordbot.cheat.data.Cheat;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CheatListeners extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Cheat cheat = this.getCheatClient();
        if(cheat != null){
            Guild guild = event.getGuild();
            
            if(guild != null){
                if(guild.getIdLong() == cheat.getGuildID()){
                    Message message = event.getMessage();
                    
                    if(message.getChannel().getIdLong() == cheat.getChannelID()){
                        if(message.getAuthor().getIdLong() != cheat.getUserID()){
                            if(message.getContentDisplay().toLowerCase().startsWith("/punir")){
                                System.out.println("Punição identificada, mandando \"Analisando\"...");
                                try {
                                    event.getChannel().sendMessage("Analisando...").complete(false);
                                } catch (RateLimitedException ex){
                                    System.out.println("Ocorreu um erro ao tentar mandar \"Analisando...\".");
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private Cheat getCheatClient(){
        return DiscordBotManager.getCheatManager().getCheat();
    }
    
}
