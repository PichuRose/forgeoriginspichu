package anticookiestudios.forgeoriginspichu;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;


public class TagUtils {
    public static ITag.INamedTag<Block> getBlockTag(ResourceLocation name) {
        for (ITag.INamedTag<Block> allTag : BlockTags.getAllTags()) {
            if (allTag.getName().equals(name)) {
                return allTag;
            }
        }
        return BlockTags.createOptional(name);
    }

    public static boolean isBlockOfTag(Item item, String tagResourceLoc){
        if(!(item instanceof BlockItem)){
            return false;
        }
        //ITag.INamedTag<Block> tagsOfBlock = getBlockTag(item.getRegistryName());
        //if(((BlockItem)item).getBlock().isIn(tagsOfBlock))
            //return true;
        return ((BlockItem)item).getBlock().isIn(TagUtils.getBlockTag(new ResourceLocation(tagResourceLoc)));
        //System.out.println(tagsOfBlock.toString());
        //return false;
    }
}