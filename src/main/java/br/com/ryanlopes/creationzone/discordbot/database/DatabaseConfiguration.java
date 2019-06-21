package br.com.ryanlopes.creationzone.discordbot.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class DatabaseConfiguration {
    
    private String MARIADB_HOST;
    private String MARIADB_USER;
    private String MARIADB_PASSWORD;
    
    public DatabaseConfiguration(){
        if((new File("DatabaseConfiguration.dat")).exists()){
            read();
        } else {
            this.MARIADB_HOST = "Insira aqui a host de autenticação";
            this.MARIADB_USER = "Insira aqui o usuário de autenticação";
            this.MARIADB_PASSWORD = "Insira aqui a senha de autenticação";
            
            write();
        }
    }
    
    private void read(){
        String line = null;
        
        try {
            FileInputStream fstream = new FileInputStream("DatabaseConfiguration.dat");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            while ((line = br.readLine()) != null){
                String[] tokens = line.split(":");
                
                if(tokens.length > 1){
                    if(tokens[0].equalsIgnoreCase("MARIADB_HOST")){
                        this.MARIADB_HOST = tokens[1];
                    } else if(tokens[0].equalsIgnoreCase("MARIADB_USER")){
                        this.MARIADB_USER = tokens[1];
                    } else if(tokens[0].equalsIgnoreCase("MARIADB_PASSWORD")){
                        this.MARIADB_PASSWORD = tokens[1];
                    }
                }
            }
            
            in.close();
        } catch(Exception e){
            e.printStackTrace();
	    System.err.println("[DatabaseConfiguration] - Exception - Line: " + line);
        }
    }
    
    public void write(){
        try {
            FileWriter fstream = new FileWriter("DatabaseConfiguration.dat");
            BufferedWriter out = new BufferedWriter(fstream);
            
            out.write("MARIADB_HOST:" + this.MARIADB_HOST);
            out.write("\n");
            out.write("MARIADB_USER:" + this.MARIADB_USER);
            out.write("\n");
            out.write("MARIADB_PASSWORD:" + this.MARIADB_PASSWORD);

            out.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String getMariaDBHost(){
        return this.MARIADB_HOST;
    }
    
    public String getMariaDBUser(){
        return this.MARIADB_USER;
    }
    
    public String getMariaDBPassword(){
        return this.MARIADB_PASSWORD;
    }
    
}
