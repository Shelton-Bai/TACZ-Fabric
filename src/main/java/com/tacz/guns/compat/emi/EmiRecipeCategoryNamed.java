package com.tacz.guns.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class EmiRecipeCategoryNamed extends EmiRecipeCategory {
    private final Text name;

    public EmiRecipeCategoryNamed(Identifier id, EmiRenderable icon, Text name) {
        super(id, icon);
        this.name = name;
    }

    @Override
    public Text getName() {
        return name;
    }
}
