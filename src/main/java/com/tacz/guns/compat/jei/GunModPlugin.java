package com.tacz.guns.compat.jei;

import com.google.common.collect.Lists;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.gun.GunItemManager;
import com.tacz.guns.compat.jei.category.AttachmentQueryCategory;
import com.tacz.guns.compat.jei.category.GunSmithTableCategory;
import com.tacz.guns.compat.jei.entry.AttachmentQueryEntry;
import com.tacz.guns.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.util.Identifier;

@JeiPlugin
public class GunModPlugin implements IModPlugin {
    private static final Identifier UID = new Identifier(GunMod.MOD_ID, "jei");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GunSmithTableCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AttachmentQueryCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(GunSmithTableCategory.GUN_SMITH_TABLE, Lists.newArrayList(TimelessAPI.getAllRecipes().values()));
        registration.addRecipes(AttachmentQueryCategory.ATTACHMENT_QUERY, AttachmentQueryEntry.getAllAttachmentQueryEntries());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModItems.GUN_SMITH_TABLE.getDefaultStack(), GunSmithTableCategory.GUN_SMITH_TABLE);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.AMMO, GunModSubtype.getAmmoSubtype());
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.ATTACHMENT, GunModSubtype.getAttachmentSubtype());
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.AMMO_BOX, GunModSubtype.getAmmoBoxSubtype());
        GunItemManager.getAllGunItems().forEach(item -> registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, GunModSubtype.getGunSubtype()));
    }

    @Override
    public Identifier getPluginUid() {
        return UID;
    }
}
