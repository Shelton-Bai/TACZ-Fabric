package com.tacz.guns.compat.emi;

import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IAmmoBox;
import com.tacz.guns.api.item.IAttachment;
import com.tacz.guns.api.item.IGun;
import dev.emi.emi.api.stack.Comparison;
import net.minecraft.item.ItemStack;

public class GunModComparison {

    public static Comparison getAmmoComparison() {
        return Comparison.compareData(emiStack -> {
            ItemStack stack = emiStack.getItemStack();
            if (stack.getItem() instanceof IAmmo iAmmo) {
                return iAmmo.getAmmoId(stack).toString();
            }
            return "";
        });
    }

    public static Comparison getGunComparison() {
        return Comparison.compareData(emiStack -> {
            ItemStack stack = emiStack.getItemStack();
            if (stack.getItem() instanceof IGun iGun) {
                return iGun.getGunId(stack).toString();
            }
            return "";
        });
    }

    public static Comparison getAttachmentComparison() {
        return Comparison.compareData(emiStack -> {
            ItemStack stack = emiStack.getItemStack();
            if (stack.getItem() instanceof IAttachment iAttachment) {
                return iAttachment.getAttachmentId(stack).toString();
            }
            return "";
        });
    }

    public static Comparison getAmmoBoxComparison() {
        return Comparison.compareData(emiStack -> {
            ItemStack stack = emiStack.getItemStack();
            if (stack.getItem() instanceof IAmmoBox iAmmoBox) {
                if (iAmmoBox.isAllTypeCreative(stack)) {
                    return "all_type_creative";
                }
                if (iAmmoBox.isCreative(stack)) {
                    return "creative";
                }
                return String.format("level_%d", iAmmoBox.getAmmoLevel(stack));
            }
            return "";
        });
    }
}
