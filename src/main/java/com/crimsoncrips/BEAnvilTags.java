package com.crimsoncrips;//


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.betterx.worlds.together.tag.v3.TagManager;

public class BEAnvilTags {
    public static final TagKey<Item> HAMMER = TagManager.ITEMS.makeTag(BEAnvil.MOD_ID, "hammers");
    public static final TagKey<Block> BETTERANVILS = TagManager.BLOCKS.makeTag(BEAnvil.MOD_ID, "betteranvils");
    public BEAnvilTags() {
    }

}
