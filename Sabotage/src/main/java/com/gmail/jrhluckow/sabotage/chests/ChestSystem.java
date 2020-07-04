package com.gmail.jrhluckow.sabotage.chests;

import com.gmail.jrhluckow.sabotage.Sabotage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChestSystem {
    public static FileConfiguration config;
    public static Sabotage main;
    public ChestSystem(Sabotage main) {
        this.config = main.getConfig();
        this.main = main;
    }
    public static List<ItemStack> CHEST_ITEMS = new ArrayList<>();
    public static void registerItems() {

    }
}
