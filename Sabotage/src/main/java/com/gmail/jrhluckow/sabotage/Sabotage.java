package com.gmail.jrhluckow.sabotage;

import com.gmail.jrhluckow.sabotage.chests.ChestSystem;
import com.gmail.jrhluckow.sabotage.commands.End;
import com.gmail.jrhluckow.sabotage.commands.Start;
import com.gmail.jrhluckow.sabotage.game.GameStatus;
import com.gmail.jrhluckow.sabotage.game.Team;
import com.gmail.jrhluckow.sabotage.lang.TranslatableContent;
import com.gmail.jrhluckow.sabotage.listener.PluginEventHandler;
import com.gmail.jrhluckow.sabotage.worlds.WorldManager;
import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVPlugin;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiversePlugin;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class Sabotage extends JavaPlugin {
    public static MultiverseCore l = getMultiverseCore();

    public static MVWorldManager wm = l.getMVWorldManager();
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    public static ArrayList<MultiverseWorld> loadedWorlds = new ArrayList<>();
    public static MultiverseCore getMultiverseCore() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

        if (plugin instanceof MultiverseCore) {
            return (MultiverseCore) plugin;
        }

        throw new RuntimeException("MultiVerse not found!");
    }
    @Override
    public void onEnable() {

        GameStatus gameStatus = new GameStatus(this);
        this.getCommand("start").setExecutor(new Start());
        this.getCommand("end").setExecutor(new End());
        this.getServer().getPluginManager().registerEvents(new PluginEventHandler(),this);
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
             wm.loadWorld((String)map);
             loadedWorlds.add(wm.getMVWorld((String)map));
             MAP_LOAD_SUCESS.getAndIncrement();
         }
        });

        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&lSabotage- &cPlugin started.\n&2&lSabotage- &aMaps Loaded Successfully: " + MAP_LOAD_SUCESS.get() + "\n&2&lSabotage- &cMaps with Errors: " + MAP_LOAD_ERROR.get() + "&c(" +MAPS_WITH_ERRORS.toString().replace("[]","")+ ")"));
        GameStatus.startDelay();


    }

    @Override
    public void onDisable() {
        GameStatus.endGame();
        Team.clearTeams();
    }

    public static MultiverseWorld randomWorld() {
        Random r = new Random();
        int num_random = r.nextInt(loadedWorlds.size());
        return loadedWorlds.get(num_random);
    }
}
