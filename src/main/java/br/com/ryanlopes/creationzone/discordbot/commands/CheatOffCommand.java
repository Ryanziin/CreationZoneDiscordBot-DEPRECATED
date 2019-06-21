package br.com.ryanlopes.creationzone.discordbot.commands;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import br.com.ryanlopes.creationzone.discordbot.cheat.CheatManager;
import br.com.ryanlopes.creationzone.discordbot.cheat.data.Cheat;
import br.com.ryanlopes.creationzone.discordbot.data.RegisterCheat;
import br.com.ryanlopes.creationzone.discordbot.utils.CommandCreator;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class CheatOffCommand extends CommandCreator {
    
    public CheatOffCommand(){
        super("/", "cheatoff", false, null);
    }
    
    @Override
    public void execute(User user, MessageChannel channel, Guild guild, Message message, String[] args){
        if(DiscordBotManager.containsUserAction(user)){
            channel.sendMessage(user.getAsMention() + ", você precisa terminar a última ação pendente para fazer isso.").complete();
            return;
        }
        
        if((user.getIdLong() == 263781927029440513L || // RianSantos
                user.getIdLong() == 304786491677081601L) == false){ // Ryanziin
            channel.sendMessage(user.getAsMention() + ", você não pode fazer isso.").complete();
            return;
        }
        
        CheatManager cheatManager = DiscordBotManager.getCheatManager();
        
        Cheat cheat = cheatManager.getCheat();
        
        if(cheat == null){
            channel.sendMessage(user.getAsMention() + ", o cheat já está desativado.").complete();
            return;
        }
        
        cheat.stop();
        cheatManager.destroy();
        channel.sendMessage(user.getAsMention() + ", cheat desativado com sucesso.").complete();
    }
    
}
