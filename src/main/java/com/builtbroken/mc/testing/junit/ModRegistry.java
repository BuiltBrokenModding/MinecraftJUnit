package com.builtbroken.mc.testing.junit;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * In-Between registry for anything that a mod normally needs to register with MC or Forge.
 * This should only be used for JUnit tests and never for actual mod.
 * Created by robert on 11/23/2014.
 */
public class ModRegistry
{
    private static List<IFuelHandler> fuelHandlers = Lists.newArrayList();
    private static boolean blocksInit = false;
    private static int nextID = 500;
    /**
     * Register a block with the specified mod specific name
     *
     * @param block The block to register
     * @param name  The mod-unique name to register it as, will get prefixed by your modid.
     */
    public static <B extends Block> B registerBlock(B block, String name)
    {
        return registerBlock(block, ItemBlock.class, name);
    }

    /**
     * Register a block with the world, with the specified item class and block name
     *
     * @param block     The block to register
     * @param itemclass The item type to register with it : null registers a block without associated item.
     * @param name      The mod-unique name to register it as, will get prefixed by your modid.
     */
    public static <B extends Block> B registerBlock(B block, Class<? extends ItemBlock> itemclass, String name)
    {
        ResourceLocation location = new ResourceLocation(name);
        if (!Block.REGISTRY.containsKey(location))
        {
            int id = nextID++;
            Block.REGISTRY.register(id, location, block);
            try
            {
                Constructor con = itemclass.getConstructor(Block.class);
                ItemBlock itemBlock = (ItemBlock) con.newInstance(block);
                if (itemBlock != null)
                {
                    Item.REGISTRY.register(id, location, itemBlock);
                }
            } catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            } catch (InvocationTargetException e)
            {
                e.printStackTrace();
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            throw new IllegalArgumentException("Block is already registered to " + name);
        }
        return (B) Block.getBlockFromName(name);
    }
}
