package br.com.ryanlopes.creationzone.discordbot.commands;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import br.com.ryanlopes.creationzone.discordbot.registers.RegistersSecurity;
import br.com.ryanlopes.creationzone.discordbot.utils.CommandCreator;
import br.com.ryanlopes.creationzone.discordbot.utils.Group;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandTestCommand extends CommandCreator {
    
    public CommandTestCommand(){
        super("/", "commandtest", false, Arrays.asList(Group.MASTER, Group.HELPER));
    }
    
    @Override
    public void execute(User user, MessageChannel channel, Guild guild, Message message, String[] args){
        RegistersSecurity registersSecurity = DiscordBotManager.getRegistersManager().getRegistersSecurity();
        
        String encrypted = registersSecurity.encrypt("Olá, tudo bem?");
        String decrypted = registersSecurity.decrypt(encrypted);
        
        Message messageResponse = channel.sendMessage("Mensagem encriptada: " + encrypted).completeAfter(1, TimeUnit.SECONDS);
        Message messageResponse2 = channel.sendMessage("Mensagem desencriptada: " + decrypted).completeAfter(1, TimeUnit.SECONDS);
        message.delete().completeAfter(3, TimeUnit.SECONDS);
        messageResponse.delete().completeAfter(3, TimeUnit.SECONDS);
        messageResponse2.delete().completeAfter(3, TimeUnit.SECONDS);
    }
    
}
