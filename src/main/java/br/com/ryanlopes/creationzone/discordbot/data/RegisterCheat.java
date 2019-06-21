package br.com.ryanlopes.creationzone.discordbot.data;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import br.com.ryanlopes.creationzone.discordbot.cheat.CheatManager;
import br.com.ryanlopes.creationzone.discordbot.cheat.data.Cheat;
import java.awt.Color;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class RegisterCheat extends ListenerAdapter {
    
    private User user;
    private int step;
    
    // DATA
    private String token;
    private long guild_id;
    private long channel_id;
    
    public RegisterCheat(User user){
        this.user = user;
        this.step = 0;
        
        DiscordBotManager.addUserAction(user);
        
        PrivateChannel privateChannel = user.openPrivateChannel().complete();
        privateChannel.sendMessage("Olá, tudo bem?").submitAfter(1, TimeUnit.SECONDS);
        privateChannel.sendMessage("Conferi aqui no sistema e você solicitou ativar o cheat.").submitAfter(3, TimeUnit.SECONDS);
        privateChannel.sendMessage("Lembre-se que a qualquer momento você pode digitar **cancelar**.").submitAfter(4, TimeUnit.SECONDS);
        privateChannel.sendMessage("**Qual o token do seu cliente?**").submitAfter(5, TimeUnit.SECONDS);
        this.step = 1;
        
        DiscordBotManager.getJDA().addEventListener(this);
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getMessage().getAuthor().getIdLong() == this.user.getIdLong()){
            Guild guild = event.getGuild();

            Message message = event.getMessage();
            if(guild == null){
                switch (this.step) {
                    case 1: // Qual o token do seu cliente?
                        if(message.getContentDisplay().equalsIgnoreCase("cancelar")){
                            DiscordBotManager.getJDA().removeEventListener(this);
                            DiscordBotManager.removeUserAction(this.user);
                            event.getPrivateChannel().sendMessage("Envio cancelado.").submitAfter(1, TimeUnit.SECONDS);
                        } else {
                            this.token = message.getContentDisplay();
                            
                            this.step = 2;
                            event.getPrivateChannel().sendMessage("**Qual o ID do servidor no discord?**").submitAfter(1, TimeUnit.SECONDS);
                        }
                        break;
                    case 2: // Qual o ID do servidor no discord?
                        if(message.getContentDisplay().equalsIgnoreCase("cancelar")){
                            DiscordBotManager.getJDA().removeEventListener(this);
                            DiscordBotManager.removeUserAction(this.user);
                            event.getPrivateChannel().sendMessage("Envio cancelado.").submitAfter(1, TimeUnit.SECONDS);
                        } else {
                            this.guild_id = Long.parseLong(message.getContentDisplay());
                            
                            this.step = 3;
                            event.getPrivateChannel().sendMessage("**Qual o ID do canal de punições?**").submitAfter(1, TimeUnit.SECONDS);
                        }
                        break;
                    case 3: // Qual o ID do canal de punições?
                        if(message.getContentDisplay().equalsIgnoreCase("cancelar")){
                            DiscordBotManager.getJDA().removeEventListener(this);
                            DiscordBotManager.removeUserAction(this.user);
                            event.getPrivateChannel().sendMessage("Envio cancelado.").submitAfter(1, TimeUnit.SECONDS);
                        } else {
                            this.channel_id = Long.parseLong(message.getContentDisplay());
                            
                            this.step = 4;
                            event.getPrivateChannel().sendMessage("**Confirme o envio:**").submitAfter(1, TimeUnit.SECONDS);
                            event.getPrivateChannel().sendMessage(summary().build()).submitAfter(2, TimeUnit.SECONDS);
                            event.getPrivateChannel().sendMessage("Digite **confirmar** para confirmar o registro.").submitAfter(3, TimeUnit.SECONDS);
                            event.getPrivateChannel().sendMessage("Digite **cancelar** para cancelar o registro.").submitAfter(4, TimeUnit.SECONDS);
                        }
                        break;
                    case 4: // Confirmação (Sumário)
                        if(message.getContentDisplay().equalsIgnoreCase("confirmar")){
                            DiscordBotManager.getJDA().removeEventListener(this);
                            DiscordBotManager.removeUserAction(this.user);
                            event.getPrivateChannel().sendMessage("Ativando cheat, aguarde.").submitAfter(1, TimeUnit.SECONDS);
                            
                            CheatManager cheatManager = DiscordBotManager.getCheatManager();
                            Cheat cheat = cheatManager.registerCheat(this.token, user.getIdLong(), this.guild_id, this.channel_id);
                            cheat.start();
                            
                            event.getPrivateChannel().sendMessage("Cheat ativado.").submitAfter(5, TimeUnit.SECONDS);
                        } else if(message.getContentDisplay().equalsIgnoreCase("cancelar")){
                            DiscordBotManager.getJDA().removeEventListener(this);
                            DiscordBotManager.removeUserAction(this.user);
                            event.getPrivateChannel().sendMessage("Envio cancelado.").submitAfter(1, TimeUnit.SECONDS);
                        } else {
                            event.getPrivateChannel().sendMessage("Digite **confirmar** para confirmar o registro.").submitAfter(1, TimeUnit.SECONDS);
                            event.getPrivateChannel().sendMessage("Digite **cancelar** para cancelar o registro.").submitAfter(2, TimeUnit.SECONDS);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    private EmbedBuilder summary(){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(new Color(85, 85, 255));
        embedBuilder.setAuthor("Revisão de Registro");
        embedBuilder.setDescription("**Seu token:** " + this.token + "\n"
                                  + "**ID do servidor no Discord** " + this.guild_id + "\n"
                                  + "**ID do canal no servidor:** " + this.channel_id);
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setFooter("Horário:", this.user.getAvatarUrl());
        
        return embedBuilder;
    }
    
}
