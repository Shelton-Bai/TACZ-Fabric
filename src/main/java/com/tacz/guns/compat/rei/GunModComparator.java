package com.tacz.guns.compat.rei;

import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IAmmoBox;
import com.tacz.guns.api.item.IAttachment;
import com.tacz.guns.api.item.IGun;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import net.minecraft.item.ItemStack;

public class GunModComparator {

    public static EntryComparator<ItemStack> getAmmoComparator() {
        return (context, stack) -> {
            if (stack.getItem() instanceof IAmmo iAmmo) {
                return iAmmo.getAmmoId(stack).hashCode();
            }
            return 0;
        };
    }

    public static EntryComparator<ItemStack> getGunComparator() {
        return (context, stack) -> {
            if (stack.getItem() instanceof IGun iGun) {
                return iGun.getGunId(stack).hashCode();
            }
            return 0;
        };
    }

    public static EntryComparator<ItemStack> getAttachmentComparator() {
        return (context, stack) -> {
            if (stack.getItem() instanceof IAttachment iAttachment) {
                return iAttachment.getAttachmentId(stack).hashCode();
            }
            return 0;
        };
    }

    public static EntryComparator<ItemStack> getAmmoBoxComparator() {
        return (context, stack) -> {
            if (stack.getItem() instanceof IAmmoBox iAmmoBox) {
                if (iAmmoBox.isAllTypeCreative(stack)) {
                    return "all_type_creative".hashCode();
                }
                if (iAmmoBox.isCreative(stack)) {
                    return "creative".hashCode();
                }
                return String.format("level_%d", iAmmoBox.getAmmoLevel(stack)).hashCode();
            }
            return 0;
        };
    }
}
