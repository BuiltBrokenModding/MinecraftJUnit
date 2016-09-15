package com.builtbroken.mc.testing.junit.world;

<<<<<<< HEAD
=======
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
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
<<<<<<< HEAD
    public Chunk getLoadedChunk(int x, int z) {
        return provideChunk(x, z); // TODO test this
=======
    public Chunk provideChunk(BlockPos pos)
    {
        return this.provideChunk(pos.getX(), pos.getZ());
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
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
<<<<<<< HEAD
=======
     * Checks to see if a chunk exists at x, y
     */
    @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_)
    {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
    {

    }

    @Override
    public boolean populateChunk(IChunkProvider chunkProvider, Chunk p_177460_2_, int x, int z) {
        return true;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
    {
        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    @Override
    public void saveExtraData() {}

    /**
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
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
<<<<<<< HEAD
=======

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
        return biomegenbase.getSpawnableList(creatureType);
    }

    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }

    @Override
    public int getLoadedChunkCount()
    {
        return 0;
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {

    }
>>>>>>> 2a65d299071a448d5ffa467bb4d4936953005450
}