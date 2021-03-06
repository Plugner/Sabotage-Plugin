package com.gmail.jrhluckow.sabotage.commands;


import com.gmail.jrhluckow.sabotage.game.GameStatus;
import com.gmail.jrhluckow.sabotage.lang.TranslatableContent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Start implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()) {
            GameStatus.forceStartGame();
            Bukkit.getOnlinePlayers().forEach((player) -> player.sendMessage(TranslatableContent.translateContent("%messages.FORCE_START")));
        }
        return false;
    }
}
