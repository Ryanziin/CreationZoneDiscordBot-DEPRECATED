package br.com.ryanlopes.creationzone.discordbot.utils;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class CommandCreator extends ListenerAdapter {
    
    private String prefix;
    private String command;
    private boolean private_works;
    private List<Group> groups;
    
    public CommandCreator(String prefix, String command, boolean private_works, List<Group> groups){
        this.prefix = prefix;
        this.command = command;
        this.private_works = private_works;
        this.groups = new ArrayList<>();
        if(groups != null){
            this.groups.addAll(groups);
        }
        
        DiscordBotManager.getJDA().addEventListener(this);
    }
    
    public abstract void execute(User user, MessageChannel channel, Guild guild, Message message, String[] args);
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Guild guild = event.getGuild();
        
        boolean authorized = guild != null ? true : (this.private_works ? true : false);
        
        if(authorized){
            if(event.getMessage().getContentDisplay().startsWith(this.prefix)){
                String[] args = event.getMessage().getContentDisplay().split(" ");
                
                if(args[0].equalsIgnoreCase(this.prefix + this.command)){
                    User user = event.getAuthor();
                    MessageChannel messageChannel = event.getChannel();
                    Message message = event.getMessage();
                    
                    if(containsPermission(user)){
                        execute(user, messageChannel, guild, message, args);
                    } else {
                        Message messageResponse = messageChannel.sendMessage(user.getAsMention() + ", você não tem permissão para fazer isso.").completeAfter(1, TimeUnit.SECONDS);
                        message.delete().completeAfter(3, TimeUnit.SECONDS);
                        messageResponse.delete().complete();
                    }
                }
            }
        }
    }
    
    private boolean containsPermission(User user){
        if(this.groups.isEmpty()){
            return true;
        }
        
        boolean contains = false;
        for(Group group : this.groups){
            if(DiscordBotManager.getGuild().getMemberById(user.getIdLong()).getRoles().contains(group.getRole())){
                contains = true;
                break;
            }
        }
        
        return contains;
    }
 
}