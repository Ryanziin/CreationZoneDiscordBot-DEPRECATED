package br.com.ryanlopes.creationzone.discordbot.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class BotConfiguration {
    
    private String DISCORD_TOKEN;
    
    public BotConfiguration(){
        if((new File("BotConfiguration.dat")).exists()){
            read();
        } else {
            this.DISCORD_TOKEN = "Insira o token aqui";
            
            write();
        }
    }
    
    private void read(){
        String line = null;
        
        try {
            FileInputStream fstream = new FileInputStream("BotConfiguration.dat");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            while ((line = br.readLine()) != null){
                String[] tokens = line.split(":");
                
                if(tokens.length > 1){
                    if(tokens[0].equalsIgnoreCase("DISCORD_TOKEN")){
                        this.DISCORD_TOKEN = tokens[1];
                    }
                }
            }
            
            in.close();
        } catch(Exception e){
            e.printStackTrace();
	    System.err.println("[BotConfiguration] - Exception - Line: " + line);
        }
    }
    
    public void write(){
        try {
            FileWriter fstream = new FileWriter("BotConfiguration.dat");
            BufferedWriter out = new BufferedWriter(fstream);
            
            out.write("DISCORD_TOKEN:" + this.DISCORD_TOKEN);

            out.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String getDiscordToken(){
        return this.DISCORD_TOKEN;
    }
    
}
