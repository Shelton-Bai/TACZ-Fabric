package com.tacz.guns.compat.rei.display;

import com.tacz.guns.GunMod;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.Collections;
import java.util.Optional;

public class GunSmithTableDisplay extends BasicDisplay {
    public static final CategoryIdentifier<GunSmithTableDisplay> ID = CategoryIdentifier.of(GunMod.MOD_ID, "gun_smith_table");

    private final GunSmithTableRecipe recipe;

    public GunSmithTableDisplay(GunSmithTableRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
                Optional.ofNullable(recipe.getId()));
        this.recipe = recipe;
    }

    public GunSmithTableRecipe getRecipe() {
        return recipe;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ID;
    }
}
