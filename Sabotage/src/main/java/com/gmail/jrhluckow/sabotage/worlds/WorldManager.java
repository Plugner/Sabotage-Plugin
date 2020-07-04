package com.gmail.jrhluckow.sabotage.worlds;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.ArrayList;
import java.util.List;

public class WorldManager {
    public static List<World> getLoadedWorlds() {
        return loadedWorlds;
    }

    public static void loadWorld(String w) {
        loadedWorlds.add(Bukkit.getServer().createWorld(new WorldCreator(w)));
    }
    public static List<World> loadedWorlds = new ArrayList<>();
    public WorldManager() {

 }
}
