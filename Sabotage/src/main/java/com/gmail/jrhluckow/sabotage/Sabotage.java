package com.gmail.jrhluckow.sabotage;

import com.gmail.jrhluckow.sabotage.chests.ChestSystem;
import com.gmail.jrhluckow.sabotage.lang.TranslatableContent;
import com.gmail.jrhluckow.sabotage.worlds.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class Sabotage extends JavaPlugin {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    @Override
    public void onEnable() {
        saveDefaultConfig();

        TranslatableContent translatableContent = new TranslatableContent(this);
        ChestSystem chestSystem = new ChestSystem(this);
        ChestSystem.registerItems();

        AtomicInteger MAP_LOAD_SUCESS= new AtomicInteger();
        AtomicInteger MAP_LOAD_ERROR= new AtomicInteger();
        List<String> MAPS_WITH_ERRORS= new ArrayList<>();
        getConfig().getList("maps").forEach((maps) -> {
         Object map = (Object)maps;
         if(Bukkit.getWorld((String)map) == null) {
             MAP_LOAD_ERROR.getAndIncrement();
             MAPS_WITH_ERRORS.add((String)map);
         }else{
             WorldManager.loadWorld((String)map);
             MAP_LOAD_SUCESS.getAndIncrement();
         }
        });

        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&lSabotage- &cPlugin started.\n&2&lSabotage- &aMaps Loaded Successfully: " + MAP_LOAD_SUCESS.get() + "\n&2&lSabotage- &cMaps with Errors: " + MAP_LOAD_ERROR.get() + "&c(" +MAPS_WITH_ERRORS.toArray().toString()+ ")"));



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
