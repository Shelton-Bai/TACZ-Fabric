package com.tacz.guns.compat.rei.category;

import com.google.common.collect.Lists;
import com.tacz.guns.compat.jei.entry.AttachmentQueryEntry;
import com.tacz.guns.compat.rei.display.AttachmentQueryDisplay;
import com.tacz.guns.init.ModCreativeTabs;
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

import java.util.List;

public class AttachmentQueryCategory implements DisplayCategory<AttachmentQueryDisplay> {
    private static final Text TITLE = Text.translatable("jei.tacz.attachment_query.title");
    private static final Renderer ICON = EntryStack.of(VanillaEntryTypes.ITEM, ModCreativeTabs.ATTACHMENT_SCOPE_TAB.getIcon());

    @Override
    public List<Widget> setupDisplay(AttachmentQueryDisplay display, Rectangle bounds) {
        int x = bounds.x + 5;
        int y = bounds.y + 5;

        List<Widget> widgets = Lists.newArrayList();

        widgets.add(Widgets.createRecipeBase(bounds));

        AttachmentQueryEntry entry = display.getEntry();

        EntryStack<ItemStack> attachmentStack = of(entry.getAttachmentStack());
        List<ItemStack> allowGunStacks = entry.getAllowGunStacks();
        List<EntryStack<ItemStack>> extraAllowGunStacks = entry.getExtraAllowGunStacks().stream().map(this::of).toList();

        // 先把配件放在正中央
        widgets.add(Widgets.createSlot(new Point(x + 72, y)).entry(attachmentStack));


        // 逐行画枪械，每行 9 个
        int xOffset = 0;
        int yOffset = 20;
        for (int i = 0; i < allowGunStacks.size(); i++) {
            int column = i % 9;
            int row = i / 9;
            xOffset = column * 18;
            yOffset = 20 + row * 18;
            EntryStack<ItemStack> gun = of(allowGunStacks.get(i));
            widgets.add(Widgets.createSlot(new Point(x + xOffset, y + yOffset)).entry(gun));
        }

        // 如果超出上限，那么最后一格则为来回跳变的物品
        if (!extraAllowGunStacks.isEmpty()) {
            widgets.add(Widgets.createLabel(new Point(x + 128, y + 134), Text.translatable("jei.tacz.attachment_query.more")).color(0x555555).noShadow());
            widgets.add(Widgets.createSlot(new Point(x + xOffset, y + yOffset)).entries(extraAllowGunStacks));
        }

        return widgets;
    }

    private EntryStack<ItemStack> of(ItemStack stack) {
        return EntryStack.of(VanillaEntryTypes.ITEM, stack);
    }

    @Override
    public CategoryIdentifier<? extends AttachmentQueryDisplay> getCategoryIdentifier() {
        return AttachmentQueryDisplay.ID;
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
    public int getDisplayWidth(AttachmentQueryDisplay display) {
        return 160 + 10;
    }

    @Override
    public int getDisplayHeight() {
        return 145;
    }
}
