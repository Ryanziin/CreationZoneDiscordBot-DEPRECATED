package br.com.ryanlopes.creationzone.discordbot.registers.commands;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import br.com.ryanlopes.creationzone.discordbot.data.RegisterUser;
import br.com.ryanlopes.creationzone.discordbot.data.UnregisterUser;
import br.com.ryanlopes.creationzone.discordbot.registers.RegistersManager;
import br.com.ryanlopes.creationzone.discordbot.registers.data.RegisterData;
import br.com.ryanlopes.creationzone.discordbot.utils.CommandCreator;
import br.com.ryanlopes.creationzone.discordbot.utils.Group;
import java.sql.SQLException;
import java.util.Arrays;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class UnregisterCommand extends CommandCreator {
    
    public UnregisterCommand(){
        super("/", "unregister", false, Arrays.asList(Group.BUILDER, Group.HELPER, Group.MODERATOR, Group.ADMIN, Group.MANAGER, Group.MASTER));
    }
    
    @Override
    public void execute(User user, MessageChannel channel, Guild guild, Message message, String[] args){
        if(DiscordBotManager.containsUserAction(user)){
            channel.sendMessage(user.getAsMention() + ", você precisa terminar a última ação pendente para fazer isso.").complete();
            return;
        }
        
        RegistersManager registersManager = DiscordBotManager.getRegistersManager();
        
        try {
            RegisterData registerData = registersManager.getRegistersRepository().select(user.getIdLong());
            
            if(registerData != null){
                new UnregisterUser(user, registerData);
                message.delete().complete();
            } else {
                channel.sendMessage(user.getAsMention() + ", você não tem uma conta cadastrada no servidor.").complete();
            }
        } catch(SQLException exception){
            exception.printStackTrace();
            channel.sendMessage(user.getAsMention() + ", ocorreu um erro. Tente novamente mais tarde.").complete();
        }
    }
    
}
