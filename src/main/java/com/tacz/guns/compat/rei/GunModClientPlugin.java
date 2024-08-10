package com.tacz.guns.compat.rei;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.compat.jei.entry.AttachmentQueryEntry;
import com.tacz.guns.compat.rei.category.AttachmentQueryCategory;
import com.tacz.guns.compat.rei.category.GunSmithTableCategory;
import com.tacz.guns.compat.rei.display.AttachmentQueryDisplay;
import com.tacz.guns.compat.rei.display.GunSmithTableDisplay;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import com.tacz.guns.init.ModItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;

public class GunModClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new GunSmithTableCategory());
        registry.add(new AttachmentQueryCategory());

        registry.addWorkstations(GunSmithTableDisplay.ID, EntryStack.of(VanillaEntryTypes.ITEM, ModItems.GUN_SMITH_TABLE.getDefaultStack()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(GunSmithTableRecipe.class, GunSmithTableDisplay::new);
        registry.registerFiller(AttachmentQueryEntry.class, AttachmentQueryDisplay::new);

        for (GunSmithTableRecipe recipe : TimelessAPI.getAllRecipes().values()) {
            for (Display display : registry.tryFillDisplay(recipe)) {
                registry.add(display, recipe);
            }
        }

        for (AttachmentQueryEntry entry : AttachmentQueryEntry.getAllAttachmentQueryEntries()) {
            for (Display display : registry.tryFillDisplay(entry)) {
                registry.add(display, entry);
            }
        }
    }
}
