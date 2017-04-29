package se.wiktoreriksson.spigottest;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener {
    /**
     * This method executes when this plugin disables.
     * @since SpigotTest 1.0
     * @see JavaPlugin#onDisable()
     */
    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Listener) this);
        Bukkit.getLogger().log(Level.INFO, "Shutting down SpigotTest.......\nDone!");
    }
    /**
     * This method executes when this plugin enables.
     * @since SpigotTest 1.0
     * @see JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {
        Bukkit.getLogger().log(Level.INFO, "Starting SpigotTest (1.0.1, author: Wiktor Eriksson)......\nDone!");
        getServer().getPluginManager().registerEvents(this, this);
    }
    public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2)
    {
        List<Block> blocks = new ArrayList<>();

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    /**
     * This method executes when a command executes.
     * @since SpigotTest 1.0.1
     * @see JavaPlugin#onCommand(CommandSender, Command, String, String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (sender instanceof ConsoleCommandSender){
                ConsoleCommandSender ccs = (ConsoleCommandSender) sender;
                if (command.getName().equalsIgnoreCase("superpower")) {
                    Collection<? extends Player> c = getServer().getOnlinePlayers();
                    for (Player p:c) {
                        List<PotionEffect> alpe = new ArrayList<>();
                        alpe.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1000, 20, true, false));
                        alpe.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000, 4, true, false));
                        alpe.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 1000, 2, true, false));
                        alpe.add(new PotionEffect(PotionEffectType.REGENERATION, 1000, 5, true, false));
                        alpe.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000, 4, true, false));
                        p.addPotionEffects(alpe);
                        ccs.sendMessage("§fGave all online players superpowers for 1000 seconds!");
                        p.sendMessage("§7[Server] §fGave all online players superpowers for 1000 seconds!");
                    }
                }
                if ("bb".equalsIgnoreCase(command.getName())){
                    getServer().createBossBar("Hello!", BarColor.PURPLE, BarStyle.SOLID);
                    return true;
                }
            }
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if ("bb".equalsIgnoreCase(command.getName())) {
                    getServer().createBossBar("Hello!", BarColor.PURPLE, BarStyle.SOLID);
                    return true;
                }
                if ("hello".equalsIgnoreCase(command.getName())) {
                    if (args.length == 0) {
                        p.chat("§aHello!");
                    } else {
                        p.sendMessage(new String[]{"§7[CmdHandler] §cWrong syntax!\n", "Usage: §a/hello"});
                        return false;
                    }
                    return true;
                }
                if ("sf".equals(command.getName())) {
                    getServer().addRecipe(new FurnaceRecipe(new ItemStack(Material.IRON_NUGGET, 2), new MaterialData(Material.COBBLESTONE), 3.5F));
                    return true;
                }
                if ("fakeblock".equalsIgnoreCase(command.getName())) {
                    if (args.length == 1) {
                        Location loc = new Location(p.getWorld(), (int) p.getLocation().getX(), (int) p.getLocation().getY(), (int) p.getLocation().getZ());
                        World w = p.getWorld();
                        FallingBlock fb = w.spawnFallingBlock(loc, new MaterialData(Material.getMaterial(args[0])));
                        fb.setGravity(false);
                        return true;
                    } else {
                        p.sendMessage(new String[]{"§7[CmdHandler] §cWrong syntax!\n", "Usage: §a/fakeblock <block ID>"});
                        return false;
                    }
                }
                if ("head".equalsIgnoreCase(command.getName())) {
                    if (args.length != 1) {
                        p.sendMessage(new String[]{"§7[CmdHandler] §cWrong syntax!\n", "Usage: §a/head <player>"});
                        return false;
                    }
                    ItemStack is = new ItemStack(Material.SKULL, 1, (short)0);
                    SkullMeta sm = (SkullMeta) is.getItemMeta();
                    sm.setOwner(args[0]);
                    p.getInventory().addItem(is);
                    return true;
                }
                if ("ptp".equalsIgnoreCase(command.getName())) {
                    Player target;
                    if (args.length == 1) {
                        target = getServer().getPlayer(args[0]);
                        if (target.isOnline()) {
                            p.teleport(target);
                            p.sendMessage("§aSuccessfully teleported to " + target.getName() + "!");
                            target.sendMessage(p.getName() + " teleported to you with /ptp!");
                        } else {
                            p.sendMessage("§cPlayer not found/isn't online!");
                            return false;
                        }
                    } else {
                        p.sendMessage(new String[]{"§7[CmdHandler] §cWrong syntax!\n", "Usage: §a/ptp <player name>"});
                        return false;
                    }
                    return true;
                }
                if (command.getName().equalsIgnoreCase("floatingtext")) {
                    if (args.length == 1) {
                        Location loc = p.getLocation();
                        World w = p.getWorld();
                        ArmorStand as = w.spawn(loc, ArmorStand.class);
                        as.setGravity(false);
                        as.setCustomNameVisible(true);
                        as.setCustomName(args[0]);
                        as.setInvulnerable(true);
                        as.setVisible(false);
                    } else {
                        p.sendMessage(new String[]{"§7[CmdHandler] §cWrong syntax!\n", "Usage: §a/head <player>"});
                        return false;
                    }
                }
            }
        } catch (IllegalArgumentException | CommandException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}