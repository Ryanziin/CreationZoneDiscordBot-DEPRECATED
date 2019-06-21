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

public class RegisterUser extends ListenerAdapter {
    
    private User user;
    private int step;
    
    // DATA
    private UserData userData;
    private RegisterData registerData;
    
    public RegisterUser(User user){
        this.user = user;
        this.step = 0;
        
        DiscordBotManager.addUserAction(user);
        
        PrivateChannel privateChannel = user.openPrivateChannel().complete();
        privateChannel.sendMessage("Olá, tudo bem?").submitAfter(1, TimeUnit.SECONDS);
        privateChannel.sendMessage("Conferi aqui no sistema e você solicitou se registrar no servidor.").submitAfter(3, TimeUnit.SECONDS);
        privateChannel.sendMessage("Lembre-se que a qualquer momento você pode digitar **cancelar**.").submitAfter(4, TimeUnit.SECONDS);
        privateChannel.sendMessage("**Qual o seu nick no minecraft?**").submitAfter(5, TimeUnit.SECONDS);
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
                RegistersSecurity registersSecurity = registersManager.getRegistersSecurity();
                
                switch (this.step) {
                    case 1: // Qual o seu nick no minecraft?
                        if(message.getContentDisplay().equalsIgnoreCase("cancelar")){
                            DiscordBotManager.getJDA().removeEventListener(this);
                            DiscordBotManager.removeUserAction(this.user);
                            event.getPrivateChannel().sendMessage("Envio cancelado.").submitAfter(1, TimeUnit.SECONDS);
                        } else {
                            String messageContent = message.getContentDisplay();
                            try {
                                UserData _userData = registersManager.getUsersRepository().select(messageContent);
                                
                                if(_userData == null){
                                    event.getPrivateChannel().sendMessage("Este usuário não existe.").submitAfter(1, TimeUnit.SECONDS);
                                    event.getPrivateChannel().sendMessage("**Qual o seu nick no minecraft?**").submitAfter(2, TimeUnit.SECONDS);
                                } else {
                                    this.userData = _userData;
                                    this.step = 2;
                                    event.getPrivateChannel().sendMessage("**Digite uma senha para sua conta.**").submitAfter(1, TimeUnit.SECONDS);
                                }
                            } catch(SQLException exception){
                                event.getPrivateChannel().sendMessage("Erro, tente novamente.").submitAfter(1, TimeUnit.SECONDS);
                                exception.printStackTrace();
                            }
                        }
                        break;
                    case 2: // Digite uma senha para sua conta.
                        if(message.getContentDisplay().equalsIgnoreCase("cancelar")){
                            DiscordBotManager.getJDA().removeEventListener(this);
                            DiscordBotManager.removeUserAction(this.user);
                            event.getPrivateChannel().sendMessage("Envio cancelado.").submitAfter(1, TimeUnit.SECONDS);
                        } else {
                            String messageContent = message.getContentDisplay();
                            
                            if(messageContent.length() < 4){
                                event.getPrivateChannel().sendMessage("Número de caracteres insuficientes.").submitAfter(1, TimeUnit.SECONDS);
                                event.getPrivateChannel().sendMessage("**Digite uma senha para sua conta.**").submitAfter(1, TimeUnit.SECONDS);
                                return;
                            }
                            
                            this.registerData = new RegisterData(this.userData.getID(), user.getIdLong(), 
                                    registersSecurity.encrypt(messageContent), 
                                    false, System.currentTimeMillis());
                            
                            this.step = 3;
                            event.getPrivateChannel().sendMessage("**Confirme o envio:**").submitAfter(1, TimeUnit.SECONDS);
                            event.getPrivateChannel().sendMessage(summary().build()).submitAfter(2, TimeUnit.SECONDS);
                            event.getPrivateChannel().sendMessage("Digite **confirmar** para confirmar o registro.").submitAfter(3, TimeUnit.SECONDS);
                            event.getPrivateChannel().sendMessage("Digite **cancelar** para cancelar o registro.").submitAfter(4, TimeUnit.SECONDS);
                        }
                        break;
                    case 3: // Confirmação (Sumário)
                        if(message.getContentDisplay().equalsIgnoreCase("confirmar")){
                            try {
                                event.getPrivateChannel().sendMessage("Cadastrando, aguarde...").submitAfter(1, TimeUnit.SECONDS);

                                // REGISTRAR
                                registersManager.getRegistersRepository().insert(this.registerData);
                                // ANUNCIAR REGISTRO
                                TextChannel textChannel = DiscordBotManager.getJDA().getTextChannelById(registersManager.getChannelIDLogRegisters());
                                textChannel.sendMessage(user.getAsMention() + " registrou o nickname " + this.userData.getNick() + ".").completeAfter(2, TimeUnit.SECONDS);

                                event.getPrivateChannel().sendMessage("Cadastro efetivado com sucesso!").submitAfter(5, TimeUnit.SECONDS);
                                
                                DiscordBotManager.getJDA().removeEventListener(this);
                                DiscordBotManager.removeUserAction(this.user);
                            } catch(SQLException exception){
                                exception.printStackTrace();
                                event.getPrivateChannel().sendMessage("Ocorreu um erro, tente novamente.").submitAfter(1, TimeUnit.SECONDS);
                                event.getPrivateChannel().sendMessage("Digite **confirmar** para confirmar o registro.").submitAfter(1, TimeUnit.SECONDS);
                                event.getPrivateChannel().sendMessage("Digite **cancelar** para cancelar o registro.").submitAfter(2, TimeUnit.SECONDS);
                            }
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
        embedBuilder.setAuthor("Revisão de dados");
        embedBuilder.setDescription("**Seu nickname:** " + this.userData.getNick() + "\n"
                                  + "**ID de registro:** " + this.userData.getID() + "\n"
                                  + "**Seu UUID:** " + this.userData.getUniqueId() + "\n"
                                  + "**Sua senha:** " + this.registerData.getPasswordDecrypted());
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setFooter("Horário:", this.user.getAvatarUrl());
        
        return embedBuilder;
    }
    
}
