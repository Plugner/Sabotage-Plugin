package com.gmail.jrhluckow.sabotage.lang;

import com.gmail.jrhluckow.sabotage.Sabotage;
import com.gmail.jrhluckow.sabotage.exceptions.MessageNotFoundException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class TranslatableContent {
    public static FileConfiguration config;
    public static Sabotage main;
    public TranslatableContent(Sabotage main) {
     this.config = main.getConfig();
     this.main = main;
    }
    public static String translateContent( String content) {
      if(config.contains(content.replace("%", ""))) {
          return ChatColor.translateAlternateColorCodes('&',config.getString(content.replace("%", "")));
      }else{
          return "";
      }
    }
    public static String translateContentThrows( String content) throws MessageNotFoundException {
        if(config.contains(content.replace("%", ""))) {
            return config.getString(content.replace("%", ""));
        }else{
            throw new MessageNotFoundException("Could not locate message");
        }
    }
}