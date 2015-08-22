package com.builtbroken.mc.testing.junit.world;

import com.builtbroken.mc.testing.junit.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.WorldInfo;

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
    public boolean setBlock(int x, int y, int z, Block block, int meta, int notify)
    {
        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
        {
            if (y < 0)
            {
                return false;
            }
            else if (y >= 256)
            {
                return false;
            }
            else
            {
                Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                Block prev_block = null;
                if ((notify & 1) != 0)
                {
                    prev_block = chunk.getBlock(x & 15, y, z & 15);
                }

                boolean flag = chunk.func_150807_a(x & 15, y, z & 15, block, meta);

                this.theProfiler.startSection("checkLight");
                this.func_147451_t(x, y, z);
                this.theProfiler.endSection();

                if (flag)
                {
                    this.markAndNotifyBlock(x, y, z, chunk, prev_block, block, notify);
                }
                return flag;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    protected IChunkProvider createChunkProvider()
    {
        return new ChunkProviderServer(this, new ChunkProviderEmpty(this));
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
