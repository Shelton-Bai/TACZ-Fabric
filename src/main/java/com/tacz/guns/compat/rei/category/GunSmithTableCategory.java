package com.tacz.guns.compat.rei.category;

import com.google.common.collect.Lists;
import com.tacz.guns.compat.rei.display.GunSmithTableDisplay;
import com.tacz.guns.crafting.GunSmithTableIngredient;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import com.tacz.guns.init.ModItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class GunSmithTableCategory implements DisplayCategory<GunSmithTableDisplay> {
    private static final Text TITLE = Text.translatable("block.tacz.gun_smith_table");
    private static final Renderer ICON = EntryStack.of(VanillaEntryTypes.ITEM, ModItems.GUN_SMITH_TABLE.getDefaultStack());

    @Override
    public List<Widget> setupDisplay(GunSmithTableDisplay display, Rectangle bounds) {
        int x = bounds.x + 5;
        int y = bounds.y;

        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));

        GunSmithTableRecipe recipe = display.getRecipe();

        EntryStack<ItemStack> output = EntryStack.of(VanillaEntryTypes.ITEM, recipe.getOutput());
        widgets.add(Widgets.createSlot(new Point(x + 3, y + 12)).entry(output).markOutput());

        List<GunSmithTableIngredient> inputs = recipe.getInputs();
        int size = inputs.size();
        // 单行排布
        if (size < 7) {
            for (int i = 0; i < size; i++) {
                int xOffset = 35 + 20 * i;
                int yOffset = 12;
                widgets.add(Widgets.createSlot(new Point(x + xOffset, y + yOffset)).entries(getInput(inputs, i)).markInput());
            }
        }
        // 双行排布
        else {
            for (int i = 0; i < 6; i++) {
                int xOffset = 35 + 20 * i;
                int yOffset = 2;
                widgets.add(Widgets.createSlot(new Point(x + xOffset, y + yOffset)).entries(getInput(inputs, i)).markInput());
            }
            for (int i = 6; i < size; i++) {
                int xOffset = 35 + 20 * (i - 6);
                int yOffset = 22;
                widgets.add(Widgets.createSlot(new Point(x + xOffset, y + yOffset)).entries(getInput(inputs, i)).markInput());
            }
        }

        return widgets;
    }

    private List<EntryStack<ItemStack>> getInput(List<GunSmithTableIngredient> inputs, int index) {
        if (index < inputs.size()) {
            GunSmithTableIngredient ingredient = inputs.get(index);
            ItemStack[] items = ingredient.ingredient().getMatchingStacks();
            Arrays.stream(items).forEach(stack -> stack.setCount(ingredient.count()));
            return Stream.of(items).map(stack -> EntryStack.of(VanillaEntryTypes.ITEM, stack)).toList();
        }
        return Collections.singletonList(EntryStack.of(VanillaEntryTypes.ITEM, ItemStack.EMPTY));
    }

    @Override
    public CategoryIdentifier<? extends GunSmithTableDisplay> getCategoryIdentifier() {
        return GunSmithTableDisplay.ID;
    }

    @Override
    public Text getTitle() {
        return TITLE;
    }

    @Override
    public Renderer getIcon() {
        return ICON;
    }

    @Override
    public int getDisplayWidth(GunSmithTableDisplay display) {
        return 160;
    }

    @Override
    public int getDisplayHeight() {
        return 40;
    }
}
