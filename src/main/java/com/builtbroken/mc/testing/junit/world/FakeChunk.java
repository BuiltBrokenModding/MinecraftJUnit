package com.builtbroken.mc.testing.junit.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

/**
 * Created by Cow Pi on 8/24/2015.
 */
public class FakeChunk extends Chunk
{
    public FakeChunk(World p_i1995_1_, int p_i1995_2_, int p_i1995_3_)
    {
        super(p_i1995_1_, p_i1995_2_, p_i1995_3_);
    }

    public FakeChunk(World p_i45446_1_, Block[] p_i45446_2_, int p_i45446_3_, int p_i45446_4_)
    {
        super(p_i45446_1_, p_i45446_2_, p_i45446_3_, p_i45446_4_);
    }

    public FakeChunk(World p_i45447_1_, Block[] p_i45447_2_, byte[] p_i45447_3_, int p_i45447_4_, int p_i45447_5_)
    {
        super(p_i45447_1_, p_i45447_2_, p_i45447_3_, p_i45447_4_, p_i45447_5_);
    }

    @Override
    public boolean func_150807_a(int x, int y, int z, Block block, int meta)
    {
        debug("setBlock(" + x + ", " + y + ", " + z + ", " + block + ", " + meta + ")");
        int i1 = z << 4 | x;

        if (y >= this.precipitationHeightMap[i1] - 1)
        {
            this.precipitationHeightMap[i1] = -999;
        }

        int j1 = this.heightMap[i1];
        Block block1 = this.getBlock(x, y, z);
        int k1 = this.getBlockMetadata(x, y, z);

        debug("  prev Block: " + block1 + " Meta: " + k1);

        if (block1 == block && k1 == meta)
        {
            debug("  no change in block ignoring set call");
            return false;
        } else
        {
            ExtendedBlockStorage extendedblockstorage = this.getBlockStorageArray()[y >> 4];
            debug("  ExtendedBlockStorage[" + (y >> 4) + "] = " + extendedblockstorage);
            boolean flag = false;

            if (extendedblockstorage == null)
            {
                if (block == Blocks.air)
                {
                    debug("  eb is null and block is air. Ignoring block set call");
                    return false;
                }

                extendedblockstorage = this.getBlockStorageArray()[y >> 4] = new ExtendedBlockStorage(y >> 4 << 4, !this.worldObj.provider.hasNoSky);
                flag = y >= j1;
            }

            int l1 = this.xPosition * 16 + x;
            int i2 = this.zPosition * 16 + z;

            int k2 = block1.getLightOpacity(this.worldObj, l1, y, i2);

            if (!this.worldObj.isRemote)
            {
                block1.onBlockPreDestroy(this.worldObj, l1, y, i2, k1);
            }

            extendedblockstorage.func_150818_a(x, y & 15, z, block);
            extendedblockstorage.setExtBlockMetadata(x, y & 15, z, meta); // This line duplicates the one below, so breakBlock fires with valid worldstate

            if (!this.worldObj.isRemote)
            {
                block1.breakBlock(this.worldObj, l1, y, i2, block1, k1);
                // After breakBlock a phantom TE might have been created with incorrect meta. This attempts to kill that phantom TE so the normal one can be create properly later
                TileEntity te = this.getTileEntityUnsafe(x & 0x0F, y, z & 0x0F);
                if (te != null && te.shouldRefresh(block1, getBlock(x & 0x0F, y, z & 0x0F), k1, getBlockMetadata(x & 0x0F, y, z & 0x0F), worldObj, l1, y, i2))
                {
                    this.removeTileEntity(x & 0x0F, y, z & 0x0F);
                }
            } else if (block1.hasTileEntity(k1))
            {
                TileEntity te = this.getTileEntityUnsafe(x & 0x0F, y, z & 0x0F);
                if (te != null && te.shouldRefresh(block1, block, k1, meta, worldObj, l1, y, i2))
                {
                    this.worldObj.removeTileEntity(l1, y, i2);
                }
            }

            if (extendedblockstorage.getBlockByExtId(x, y & 15, z) != block)
            {
                return false;
            } else
            {
                extendedblockstorage.setExtBlockMetadata(x, y & 15, z, meta);

                if (flag)
                {
                    this.generateSkylightMap();
                } else
                {
                    int j2 = block.getLightOpacity(this.worldObj, l1, y, i2);

                    if (j2 > 0)
                    {
                        if (y >= j1)
                        {
                            //TODO this.relightBlock(x, y + 1, z);
                        }
                    } else if (y == j1 - 1)
                    {
                        //TODO this.relightBlock(x, y, z);
                    }

                    if (j2 != k2 && (j2 < k2 || this.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) > 0 || this.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 0))
                    {
                        //TODO this.propagateSkylightOcclusion(x, z);
                    }
                }

                TileEntity tileentity;

                if (!this.worldObj.isRemote)
                {
                    block.onBlockAdded(this.worldObj, l1, y, i2);
                }

                if (block.hasTileEntity(meta))
                {
                    tileentity = this.func_150806_e(x, y, z);

                    if (tileentity != null)
                    {
                        tileentity.updateContainingBlockInfo();
                        tileentity.blockMetadata = meta;
                    }
                }

                this.isModified = true;
                return true;
            }
        }
    }

    @Override
    public void removeTileEntity(int x, int y, int z)
    {
        debug("removeTileEntity(" + x + ", " + y + ", " + z + ")");
        ChunkPosition chunkposition = new ChunkPosition(x, y, z);

        if (this.isChunkLoaded)
        {
            TileEntity tileentity = (TileEntity) this.chunkTileEntityMap.remove(chunkposition);
            debug(" removed tile = " + tileentity);
            if (tileentity != null)
            {
                tileentity.invalidate();
            }
        }
    }

    protected void debug(String msg)
    {
        if (worldObj instanceof AbstractFakeWorld && ((AbstractFakeWorld) worldObj).debugInfo)
            ((AbstractFakeWorld) worldObj).logger.info(this + " " + msg);
    }

}
