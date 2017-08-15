package edu.vt.alic.animamorphacandy;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Food {

    private String name;

    private ItemStack itemStack;

    private HashMap<DisguiseType, Double> disguises;
    private HashMap<PotionEffect, Double> effects;

    public Food(final String name,
                final ItemStack itemStack,
                final HashMap<DisguiseType, Double> disguises,
                final HashMap<PotionEffect, Double> effects) {
        this.name = name;
        this.itemStack = itemStack;
        this.disguises = disguises;
        this.effects = effects;
    }

    public String getName() { return name; }
    public ItemStack getItemStack() {
        return itemStack;
    }

    public DisguiseType getRandomDisguiseType() {
        double random = new Random().nextDouble();
        Iterator it = disguises.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (random <= (Double) pair.getValue()) {
                return (DisguiseType) pair.getKey();
            }
            it.remove();
        }
        return null;
    }

    public List<PotionEffect> getEffects() {
        double random = new Random().nextDouble();
        List<PotionEffect> effectsList = new ArrayList<PotionEffect>();
        Iterator it = effects.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (random <= (Double) pair.getValue()) {
                effectsList.add((PotionEffect) pair.getKey());
            }
            it.remove();
        }
        return effectsList;
    }
}
