package com.tacz.guns.compat.emi.recipe;

import com.tacz.guns.compat.emi.GunModPlugin;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

public class GunSmithTableEmiRecipe extends BasicEmiRecipe {

    public GunSmithTableEmiRecipe(GunSmithTableRecipe recipe) {
        super(GunModPlugin.GUN_SMITH_TABLE, recipe.getId(), 160, 40);
        this.inputs.addAll(recipe.getInputs().stream().map(i -> EmiIngredient.of(i.ingredient(), i.count())).toList());
        this.outputs.add(EmiStack.of(recipe.getOutput()));
//        this.catalysts.add(EmiStack.of(ModItems.GUN_SMITH_TABLE));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        EmiStack output = outputs.get(0);
        widgets.addSlot(output, 3, 12).recipeContext(this);

        int size = inputs.size();
        // 单行排布
        if (size < 7) {
            for (int i = 0; i < size; i++) {
                int xOffset = 35 + 20 * i;
                int yOffset = 12;
                widgets.addSlot(getInput(i), xOffset, yOffset);
            }
        }
        // 双行排布
        else {
            for (int i = 0; i < 6; i++) {
                int xOffset = 35 + 20 * i;
                int yOffset = 2;
                widgets.addSlot(getInput(i), xOffset, yOffset);
            }
            for (int i = 6; i < size; i++) {
                int xOffset = 35 + 20 * (i - 6);
                int yOffset = 22;
                widgets.addSlot(getInput(i), xOffset, yOffset);
            }
        }
    }

    private EmiIngredient getInput(int index) {
        if (index < inputs.size()) {
            return inputs.get(index);
        }
        return EmiStack.EMPTY;
    };
}
