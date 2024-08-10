package com.tacz.guns.compat.rei.display;

import com.tacz.guns.GunMod;
import com.tacz.guns.compat.jei.entry.AttachmentQueryEntry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.Collections;
import java.util.Optional;

public class AttachmentQueryDisplay extends BasicDisplay {
    public static final CategoryIdentifier<AttachmentQueryDisplay> ID = CategoryIdentifier.of(GunMod.MOD_ID, "attachment_query");

    private final AttachmentQueryEntry entry;

    public AttachmentQueryDisplay(AttachmentQueryEntry entry) {
        super(entry.getAllowGunStacks().stream().map(EntryIngredients::of).toList(),
                Collections.singletonList(EntryIngredients.of(entry.getAttachmentStack())),
                Optional.ofNullable(entry.getId()));
        this.entry = entry;
    }

    public AttachmentQueryEntry getEntry() {
        return entry;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ID;
    }
}
