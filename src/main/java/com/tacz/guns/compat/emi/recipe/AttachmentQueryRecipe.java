package com.tacz.guns.compat.emi.recipe;

import com.google.common.collect.Lists;
import com.tacz.guns.compat.emi.GunModPlugin;
import com.tacz.guns.compat.jei.entry.AttachmentQueryEntry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class AttachmentQueryRecipe extends BasicEmiRecipe {
    private final List<EmiIngredient> extraAllowGunStacks = Lists.newArrayList();

    public AttachmentQueryRecipe(AttachmentQueryEntry entry) {
        super(GunModPlugin.ATTACHMENT_QUERY, convertEMI(entry.getId(), entry), 160 + 2, 145);
        this.inputs.addAll(entry.getAllowGunStacks().stream().map(EmiStack::of).toList());
        this.extraAllowGunStacks.addAll(entry.getExtraAllowGunStacks().stream().map(EmiStack::of).toList());
        this.outputs.add(EmiStack.of(entry.getAttachmentStack()));
    }

    private static Identifier convertEMI(Identifier input, AttachmentQueryEntry entry) {
        return input.withSuffixedPath(String.format("/%s", entry.getType().name()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        EmiStack attachmentStack = outputs.get(0);
        List<EmiIngredient> allowGunStacks = inputs;

        widgets.addSlot(attachmentStack, 72, 0).recipeContext(this);

        // 逐行画枪械，每行 9 个
        int xOffset = 0;
        int yOffset = 20;
        for (int i = 0; i < allowGunStacks.size(); i++) {
            int column = i % 9;
            int row = i / 9;
            xOffset = column * 18;
            yOffset = 20 + row * 18;
            EmiIngredient gun = allowGunStacks.get(i);
            widgets.addSlot(gun, xOffset, yOffset);
        }

        // 如果超出上限，那么最后一格则为来回跳变的物品
        if (!extraAllowGunStacks.isEmpty()) {
            widgets.addText(Text.translatable("jei.tacz.attachment_query.more"), 128, 134, 0x555555, false);
            widgets.addSlot(EmiIngredient.of(extraAllowGunStacks), xOffset + 18, yOffset);
        }
    }
}
