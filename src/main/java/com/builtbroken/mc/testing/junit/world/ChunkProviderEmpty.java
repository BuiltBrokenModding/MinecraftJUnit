package com.builtbroken.mc.testing.junit.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import javax.annotation.Nullable;

public class ChunkProviderEmpty implements IChunkProvider
{
    private World worldObj;

    public ChunkProviderEmpty(World p_i2004_1_)
    {
        this.worldObj = p_i2004_1_;
    }

    @Nullable
    @Override
    public Chunk getLoadedChunk(int x, int z) {
        return provideChunk(x, z); // TODO test this
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);


        //Not sure if we need to call this before biome
        chunk.generateSkylightMap();

        //Set biome ID for chunk
        for (int l = 0; l < chunk.getBiomeArray().length; ++l) {
            chunk.getBiomeArray()[l] = (byte) 1;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    @Override
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    /**
     * Converts the instance data to a readable string.
     */
    @Override
    public String makeString()
    {
        return "sigVoidSource";
    }
}