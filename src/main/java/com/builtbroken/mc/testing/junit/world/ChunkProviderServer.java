package com.builtbroken.mc.testing.junit.world;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;

public class ChunkProviderServer implements IChunkProvider
{
    public IChunkProvider currentChunkProvider;
    public LongHashMap loadedChunkHashMap = new LongHashMap();
    public World worldObj;

    public ChunkProviderServer(World p_i1520_1_, IChunkProvider p_i1520_3_)
    {
        this.worldObj = p_i1520_1_;
        this.currentChunkProvider = p_i1520_3_;
    }

    @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_)
    {
        return this.loadedChunkHashMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(p_73149_1_, p_73149_2_));
    }

    @Override
    public Chunk loadChunk(int par1, int par2)
    {
        return new Chunk(this.worldObj, par1, par2);
    }

    @Override
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_)
    {
        Chunk chunk = (Chunk) this.loadedChunkHashMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_, p_73154_2_));
        return chunk == null ? this.loadChunk(p_73154_1_, p_73154_2_) : chunk;
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
    {
        Chunk chunk = this.provideChunk(p_73153_2_, p_73153_3_);

        if (!chunk.isTerrainPopulated)
        {
            chunk.func_150809_p();

            if (this.currentChunkProvider != null)
            {
                this.currentChunkProvider.populate(p_73153_1_, p_73153_2_, p_73153_3_);
                //TODO GameRegistry.generateWorld(p_73153_2_, p_73153_3_, worldObj, currentChunkProvider, p_73153_1_);
                chunk.setChunkModified();
            }
        }
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
    {
        return true;
    }

    @Override
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    @Override
    public boolean canSave()
    {
        return false;
    }

    @Override
    public String makeString()
    {
        return "ServerChunkCache: " + this.loadedChunkHashMap.getNumHashElements();
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_)
    {
        return this.currentChunkProvider.getPossibleCreatures(p_73155_1_, p_73155_2_, p_73155_3_, p_73155_4_);
    }

    @Override
    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_)
    {
        return this.currentChunkProvider.func_147416_a(p_147416_1_, p_147416_2_, p_147416_3_, p_147416_4_, p_147416_5_);
    }

    @Override
    public int getLoadedChunkCount()
    {
        return this.loadedChunkHashMap.getNumHashElements();
    }

    @Override
    public void recreateStructures(int p_82695_1_, int p_82695_2_) {}

    @Override
    public void saveExtraData()
    {

    }
}