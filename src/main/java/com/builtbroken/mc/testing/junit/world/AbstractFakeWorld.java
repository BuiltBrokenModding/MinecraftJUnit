package com.builtbroken.mc.testing.junit.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Cow Pi on 8/24/2015.
 */
public class AbstractFakeWorld extends World
{
    public boolean debugInfo = true;
    public Logger logger;

    public AbstractFakeWorld(ISaveHandler saveHandler, WorldInfo worldInfo, WorldProvider worldProvider, Profiler profiler, boolean client)
    {
        super(saveHandler, worldInfo, worldProvider, profiler, client);
        worldProvider.setWorld(this);
        logger = LogManager.getLogger("FW-" + worldInfo);
        chunkProvider = new ChunkProviderServer(this, this.saveHandler.getChunkLoader(this.provider), provider.createChunkGenerator());
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean setBlockState(BlockPos pos, IBlockState block, int flags)
    {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        debug("");
        debug("setBlockState(" + pos + ", " + block + ")");
        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
        {
            if (y < 0)
            {
                debug("setBlockState() y level is too low");
                return false;
            } else if (y >= 256)
            {
                debug("setBlockState() y level is too high");
                return false;
            } else
            {
                Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
                debug("setBlock() chunk = " + chunk);
                net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;

                if (this.captureBlockSnapshots && !this.isRemote)
                {
                    blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(this, pos, flags);
                    this.capturedBlockSnapshots.add(blockSnapshot);
                }

                boolean flag = chunk.setBlockState(new BlockPos(x & 15, y, z & 15), block) != null;
                debug("setBlockState() flag = " + flag + " BlockSnapshot = " + blockSnapshot);

                if (!flag && blockSnapshot != null)
                {
                    this.capturedBlockSnapshots.remove(blockSnapshot);
                    blockSnapshot = null;
                }

                this.profiler.startSection("checkLight");
                this.checkLightFor(EnumSkyBlock.BLOCK, pos);
                this.profiler.endSection();

                if (flag && blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    // Modularize client and physic updates
                    this.markAndNotifyBlock(pos, chunk, null, block, flags);
                }

                return flag;
            }
        } else
        {
            debug("setBlock() too far from zero zero");
            return false;
        }
    }

    @Override
    protected IChunkProvider createChunkProvider()
    {
        IChunkProvider provider = new ChunkProviderEmpty(this);
        this.chunkProvider = provider;
        return provider;
    }

    @Override
    public Entity getEntityByID(int p_73045_1_)
    {
        return null;
    }

    protected void debug(String msg)
    {
        if (debugInfo)
            logger.info(msg);
    }

    @Override
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
        return false;
    }
}
