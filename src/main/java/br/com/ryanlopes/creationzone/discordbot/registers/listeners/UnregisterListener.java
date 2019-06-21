package br.com.ryanlopes.creationzone.discordbot.registers.listeners;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import br.com.ryanlopes.creationzone.discordbot.registers.RegistersManager;
import br.com.ryanlopes.creationzone.discordbot.registers.data.RegisterData;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UnregisterListener extends ListenerAdapter {
    
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        User user = event.getUser();
        Guild guild = event.getGuild();
        
        RegistersManager registersManager = DiscordBotManager.getRegistersManager();
        
        if(guild.getIdLong() == DiscordBotManager.getGuild().getIdLong()){
            TextChannel textChannel = guild.getTextChannelById(registersManager.getChannelIDLogRegisters());
            
            if(textChannel != null){
                try {
                    RegisterData registerData = registersManager.getRegistersRepository().select(user.getIdLong());
                    
                    if(registerData == null){
                        textChannel.sendMessage(user.getAsMention() + " saiu mas não tinha cadastro no servidor.").completeAfter(1, TimeUnit.SECONDS);
                    } else {
                        registersManager.getRegistersRepository().delete(registerData.getDiscordUserID());
                        textChannel.sendMessage(user.getAsMention() + " saiu e teve seu registro deletado.").completeAfter(1, TimeUnit.SECONDS);
                    }
                } catch(SQLException exception){
                    exception.printStackTrace();
                    textChannel.sendMessage(user.getAsMention() + " saiu e não foi possível verificar seu cadastro.").completeAfter(1, TimeUnit.SECONDS);
                }
            }
        }
    }
    
}
