package edu.vt.alic.animamorphacandy;


import edu.vt.alic.animamorphacandy.listeners.FoodEatListener;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * I fucking hate naming my main class Main, but that name is way too long.
 *
 */
public class Main extends JavaPlugin {

    private ArrayList<Food> foods;

    @Override
    public void onEnable() {
        foods = new ArrayList<Food>();
        setupConfig();
        setupListeners();
        populateFoods();
    }

    @Override
    public void onDisable() {
        foods.clear();
    }


    private void setupConfig()
    {
        getConfig().getDefaults();
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new FoodEatListener(this), this);
    }

    private void populateFoods() {
        for (String food : getConfig().getConfigurationSection("Foods").getKeys(false)) {
            String path = "Foods." + food + ".";

            int id = getConfig().getInt("Foods." + food + ".ID");
            int data = getConfig().getInt("Foods." + food + ".ID_Data");
            ItemStack foodItem = new ItemStack(id, 1, (short) data);

            HashMap<DisguiseType, Double> disguiseChances = new HashMap<DisguiseType, Double>();
            for (String disguise : getConfig()
                    .getConfigurationSection(path + "DisguiseTypes")
                    .getKeys(false)) {
                double chance = getConfig().getDouble(path + "DisguiseTypes." + disguise + ".Chance");
                disguiseChances.put(DisguiseType.valueOf(disguise), chance);
            }

            HashMap<PotionEffect, Double> effectChances = new HashMap<PotionEffect, Double>();
            for (String effect : getConfig()
                    .getConfigurationSection(path + "Effects")
                    .getKeys(false)) {
                PotionEffectType type = PotionEffectType.getByName(effect);
                double chance = getConfig().getDouble(path + "Effects." + effect + ".Chance");
                int amplifier = getConfig().getInt(path + "Effects." + effect + ".Amplifier");
                int duration = getConfig().getInt(path + "Effects." + effect + ".Duration") * 20;
                effectChances.put(new PotionEffect(type, duration, amplifier), chance);
            }

            foods.add(new Food(food, foodItem, disguiseChances, effectChances));
        }
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public Food getFood(ItemStack item) {
        for (Food food : getFoods()) {
            ItemStack foodItem = food.getItemStack();
            if (item.getType().equals(foodItem.getType())
                    && item.getData().getData() == foodItem.getData().getData()) {
                return food;
            }
        }
        return null;
    }
}
