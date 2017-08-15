package edu.vt.alic.animamorphacandy.listeners;

import edu.vt.alic.animamorphacandy.Food;
import edu.vt.alic.animamorphacandy.Main;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class FoodEatListener implements Listener {

    private Main plugin;

    public FoodEatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFoodEat(PlayerItemConsumeEvent e) {
        final Player p = e.getPlayer();
        if (!p.hasPermission("animamorphacandy.consume")) {
            return;
        }
        Food eatenFood = plugin.getFood(e.getItem());
        if (eatenFood == null) {
            return;
        }

        DisguiseType disguiseType = eatenFood.getRandomDisguiseType();
        if (disguiseType != null) {
            int duration = plugin.getConfig().getInt("Foods."
                    + eatenFood.getName() + ".DisguiseTypes."
                    + disguiseType.toString() + ".Duration");

            DisguiseAPI.disguiseToAll(p, new MobDisguise(disguiseType));

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (DisguiseAPI.isDisguised(p)) {
                        DisguiseAPI.undisguiseToAll(p);
                        p.sendMessage("ur disguise is gone");
                    }
                }
            }, duration * 20);
        }

        List<PotionEffect> effects = eatenFood.getEffects();
        if (!effects.isEmpty()) {
            for (int i = 0; i < effects.size(); i++) {
                p.addPotionEffect(effects.get(i));
            }
        }
        if (effects.isEmpty() && disguiseType == null) {
            return;
        }

        p.sendMessage("you ate the food and got some disguise or effects or both");
    }
}
