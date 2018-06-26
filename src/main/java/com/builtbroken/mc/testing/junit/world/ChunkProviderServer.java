package com.builtbroken.mc.testing.junit.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChunkProviderServer implements IChunkProvider
{
    public final Long2ObjectMap<Chunk> MAP = new Long2ObjectOpenHashMap<Chunk>(8192);
    private IChunkGenerator generator;
    private IChunkLoader loader;
    private World world;

    public ChunkProviderServer(World world, IChunkLoader loader, IChunkGenerator generator)
    {
        this.world = world;
        this.loader = loader;
        this.generator = generator;
    }

    @Nullable
    @Override
    public Chunk getLoadedChunk(int x, int z)
    {
        long l = ChunkPos.asLong(x, z);
        Chunk chunk = MAP.get(l);
        if (chunk != null && chunk.isLoaded())
        {
            chunk.markLoaded(true);
        }
        return chunk;
    }

    @Override
    @Nonnull
    public Chunk provideChunk(int x, int z)
    {
        Chunk chunk = getLoadedChunk(x, z);
        if (chunk == null)
        {
            long l = ChunkPos.asLong(x, z);

            chunk = generator.generateChunk(x, z);
            MAP.put(l, chunk);
            chunk.onLoad();
            chunk.populate(this, generator);
        }
        return chunk;
    }

    @Override
    public boolean tick()
    {
        return false;
    }

    @Override
    @Nonnull
    public String makeString()
    {
        return "ServerChunkCache: " + MAP.size();
    }

    @Override
    public boolean isChunkGeneratedAt(int x, int z)
    {
        return false;
    }
}