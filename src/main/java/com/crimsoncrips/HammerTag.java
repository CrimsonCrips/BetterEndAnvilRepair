package com.crimsoncrips;//


import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import org.betterx.betterend.BetterEnd;
import org.betterx.worlds.together.tag.v3.TagManager;

public class HammerTag {
    public static final TagKey<Item> HAMMER = TagManager.ITEMS.makeTag(BEAnvilRepair.MOD_ID, "hammers");
    public HammerTag() {
    }

}
