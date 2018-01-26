package com.builtbroken.mc.testing.junit.world;

import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.IChunkLoader;

import javax.annotation.Nullable;
import java.io.IOException;

public class FakeChunkLoader implements IChunkLoader {
    @Nullable
    @Override
    public Chunk loadChunk(World worldIn, int x, int z) throws IOException {
        return new FakeChunk(worldIn, x, z);
    }

    @Override
    public void saveChunk(World worldIn, Chunk chunkIn) throws MinecraftException, IOException {

    }

    @Override
    public void saveExtraChunkData(World worldIn, Chunk chunkIn) throws IOException {

    }

    @Override
    public void chunkTick() {

    }

    @Override
    public void flush() {

    }

    @Override
    public boolean isChunkGeneratedAt(int x, int z) {
        return false;
    }
}
