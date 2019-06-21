package br.com.ryanlopes.creationzone.discordbot.data;

import br.com.ryanlopes.creationzone.discordbot.DiscordBotManager;
import br.com.ryanlopes.creationzone.discordbot.registers.RegistersManager;
import br.com.ryanlopes.creationzone.discordbot.registers.RegistersSecurity;
import br.com.ryanlopes.creationzone.discordbot.registers.data.RegisterData;
import br.com.ryanlopes.creationzone.discordbot.registers.data.UserData;
import java.awt.Color;
import java.sql.SQLException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class UnregisterUser extends ListenerAdapter {
    
    private User user;
    private int step;
    
    // DATA
    private RegisterData registerData;
    
    public UnregisterUser(User user, RegisterData registerData){
        this.user = user;
        this.step = 0;
        
        this.registerData = registerData;
        
        DiscordBotManager.addUserAction(user);
        
        PrivateChannel privateChannel = user.openPrivateChannel().complete();
        privateChannel.sendMessage("Olá, tudo bem?").submitAfter(1, TimeUnit.SECONDS);
        privateChannel.sendMessage("Conferi aqui no sistema e você solicitou para deletar sua conta no servidor.").submitAfter(3, TimeUnit.SECONDS);
        privateChannel.sendMessage("Digite **confirmar** para deletar o cadastro.").submitAfter(4, TimeUnit.SECONDS);
        privateChannel.sendMessage("Digite **cancelar** para cancelar esta ação.").submitAfter(5, TimeUnit.SECONDS);
        this.step = 1;
        
        DiscordBotManager.getJDA().addEventListener(this);
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getMessage().getAuthor().getIdLong() == this.user.getIdLong()){
            Guild guild = event.getGuild();

            Message message = event.getMessage();
            if(guild == null){
                RegistersManager registersManager = DiscordBotManager.getRegistersManager();
                
                switch (this.step) {
                    case 1: // Confirmação
                        if(message.getContentDisplay().equalsIgnoreCase("confirmar")){
                            try {
                                event.getPrivateChannel().sendMessage("Cadastrando, aguarde...").submitAfter(1, TimeUnit.SECONDS);

                                // DELETAR
                                registersManager.getRegistersRepository().delete(this.registerData.getDiscordUserID());
                                // ANUNCIAR O CADASTRO DELETADO
                                TextChannel textChannel = DiscordBotManager.getJDA().getTextChannelById(registersManager.getChannelIDLogRegisters());
                                textChannel.sendMessage(user.getAsMention() + " apagou seu cadastro do servidor.").completeAfter(2, TimeUnit.SECONDS);

                                event.getPrivateChannel().sendMessage("Cadastro deletado com sucesso!").submitAfter(5, TimeUnit.SECONDS);
                                
                                DiscordBotManager.getJDA().removeEventListener(this);
                                DiscordBotManager.removeUserAction(this.user);
                            } catch(SQLException exception){
                                exception.printStackTrace();
                                event.getPrivateChannel().sendMessage("Ocorreu um erro, tente novamente.").submitAfter(1, TimeUnit.SECONDS);
                                event.getPrivateChannel().sendMessage("Digite **confirmar** para deletar o cadastro.").submitAfter(1, TimeUnit.SECONDS);
                                event.getPrivateChannel().sendMessage("Digite **cancelar** para cancelar esta ação.").submitAfter(2, TimeUnit.SECONDS);
                            }
                        } else if(message.getContentDisplay().equalsIgnoreCase("cancelar")){
                            DiscordBotManager.getJDA().removeEventListener(this);
                            DiscordBotManager.removeUserAction(this.user);
                            event.getPrivateChannel().sendMessage("Ação cancelada.").submitAfter(1, TimeUnit.SECONDS);
                        } else {
                            event.getPrivateChannel().sendMessage("Digite **confirmar** para deletar o cadastro.").submitAfter(1, TimeUnit.SECONDS);
                            event.getPrivateChannel().sendMessage("Digite **cancelar** para cancelar esta ação.").submitAfter(2, TimeUnit.SECONDS);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
}
