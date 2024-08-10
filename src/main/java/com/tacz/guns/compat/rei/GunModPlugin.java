package com.tacz.guns.compat.rei;

import com.tacz.guns.api.item.gun.GunItemManager;
import com.tacz.guns.init.ModItems;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REIPlugin;

public class GunModPlugin implements REIPlugin<GunModPlugin> {

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.register(GunModComparator.getAmmoComparator(), ModItems.AMMO);
        registry.register(GunModComparator.getAttachmentComparator(), ModItems.ATTACHMENT);
        registry.register(GunModComparator.getAmmoBoxComparator(), ModItems.AMMO_BOX);
        GunItemManager.getAllGunItems().forEach(item ->
                registry.register(GunModComparator.getGunComparator(), item));
    }


    @Override
    public Class<GunModPlugin> getPluginProviderClass() {
        return GunModPlugin.class;
    }
}
