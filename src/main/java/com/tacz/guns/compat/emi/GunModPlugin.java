package com.tacz.guns.compat.emi;

import com.google.common.collect.Lists;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.gun.GunItemManager;
import com.tacz.guns.compat.emi.recipe.AttachmentQueryRecipe;
import com.tacz.guns.compat.emi.recipe.GunSmithTableEmiRecipe;
import com.tacz.guns.compat.jei.entry.AttachmentQueryEntry;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import com.tacz.guns.init.ModCreativeTabs;
import com.tacz.guns.init.ModItems;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GunModPlugin implements EmiPlugin {
    public static final EmiRecipeCategory GUN_SMITH_TABLE = new EmiRecipeCategoryNamed(
            Identifier.of(GunMod.MOD_ID, "gun_smith_table"),
            EmiStack.of(ModItems.GUN_SMITH_TABLE),
            Text.translatable("block.tacz.gun_smith_table"));
    public static final EmiRecipeCategory ATTACHMENT_QUERY = new EmiRecipeCategoryNamed(
            Identifier.of(GunMod.MOD_ID, "attachment_query"),
            EmiStack.of(ModCreativeTabs.ATTACHMENT_SCOPE_TAB.getIcon()),
            Text.translatable("jei.tacz.attachment_query.title"));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(GUN_SMITH_TABLE);
        registry.addCategory(ATTACHMENT_QUERY);

        registry.addWorkstation(GUN_SMITH_TABLE, EmiStack.of(ModItems.GUN_SMITH_TABLE));

        registry.setDefaultComparison(ModItems.AMMO, GunModComparison.getAmmoComparison());
        registry.setDefaultComparison(ModItems.ATTACHMENT, GunModComparison.getAttachmentComparison());
        registry.setDefaultComparison(ModItems.AMMO_BOX, GunModComparison.getAmmoBoxComparison());
        GunItemManager.getAllGunItems().forEach(item -> registry.setDefaultComparison(item, GunModComparison.getGunComparison()));

        for (GunSmithTableRecipe recipe : Lists.newArrayList(TimelessAPI.getAllRecipes().values())) {
            registry.addRecipe(new GunSmithTableEmiRecipe(recipe));
        }
        for (AttachmentQueryEntry entry : AttachmentQueryEntry.getAllAttachmentQueryEntries()) {
            registry.addRecipe(new AttachmentQueryRecipe(entry));
        }
    }
}
