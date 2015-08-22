package com.builtbroken.mc.testing.junit.world;

import com.builtbroken.mc.testing.junit.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Fake world that creates a single chunk to work out of
 * Created by robert on 11/20/2014.
 */
public class FakeWorldWithChunks extends World
{
    public FakeWorldWithChunks()
    {
        super(null, "FakeWorld", new FakeWorldProvider(), new WorldSettings(new WorldInfo(new NBTTagCompound())), new Profiler());
        ModRegistry.init();
    }

    @Override
    protected IChunkProvider createChunkProvider()
    {
        return null;
    }

    @Override
    protected int func_152379_p()
    {
        return 0;
    }

    @Override
    public Entity getEntityByID(int p_73045_1_)
    {
        return null;
    }
}
