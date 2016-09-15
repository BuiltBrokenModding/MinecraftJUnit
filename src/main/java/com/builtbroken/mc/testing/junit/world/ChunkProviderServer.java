package com.builtbroken.mc.testing.junit.world;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import javax.annotation.Nullable;

public class ChunkProviderServer implements IChunkProvider
{
    public final Long2ObjectMap<Chunk> MAP = new Long2ObjectOpenHashMap<Chunk>(8192);
    private IChunkGenerator generator;
    private IChunkLoader loader;
    private World world;

    public ChunkProviderServer(World world, IChunkLoader loader, IChunkGenerator generator) {
        this.world = world;
        this.loader = loader;
        this.generator = generator;
    }

    @Nullable
    @Override
    public Chunk getLoadedChunk(int x, int z) {
        long l = ChunkPos.chunkXZ2Int(x, z);
        Chunk chunk = MAP.get(l);
        if (chunk != null) {
            chunk.unloaded = false;
        }
        return chunk;
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        Chunk chunk = getLoadedChunk(x, z);
        if (chunk == null) {
            long l = ChunkPos.chunkXZ2Int(x, z);

            chunk = generator.provideChunk(x, z);
            MAP.put(l, chunk);
            chunk.onChunkLoad();
            chunk.populateChunk(this, generator);
        }
        return chunk;
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public String makeString() {
        return "ServerChunkCache: " + MAP.size();
    }
}