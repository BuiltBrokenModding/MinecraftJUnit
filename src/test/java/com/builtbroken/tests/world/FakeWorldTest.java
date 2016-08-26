package com.builtbroken.tests.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.builtbroken.mc.testing.junit.AbstractTest;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import org.junit.Test;
import org.junit.runner.RunWith;

/** JUnit test for {@link FakeWorld}
 * Created by robert on 11/13/2014.
 */
@RunWith(VoltzTestRunner.class)
public class FakeWorldTest extends AbstractTest
{
    World world = null;

    public FakeWorldTest()
    {

    }

    @Override
    public void setUpForEntireClass()
    {
        world = FakeWorld.newWorld("FakeWorldTest");
    }

    @Test
    public void testBlockRegistry()
    {
        Object block = Block.blockRegistry.getObject(new ResourceLocation("sand"));
        assertNotNull(block);
        assertEquals(Block.getIdFromBlock((Block) block), 12);
    }

    @Test
    public void testCreation()
    {
        assertNotNull("Failed to create world.", world);
    }

    @Test
    public void testNullPlacement()
    {
        try
        {
            world.setBlockState(new BlockPos(0, 0, 0), null);
            fail("World didn't catch null block.");
        }
        catch (NullPointerException e)
        {
            //This should be thrown :)
        }
    }

    @Test
    public void testBlockPlacement()
    {
        if (Blocks.sand != null)
        {
            BlockPos pos = new BlockPos(0, 0, 0);
            world.setBlockState(pos, Blocks.sand.getDefaultState());
            Block block = world.getBlockState(pos).getBlock();
            assertEquals("World.getBlockState() failed.", Blocks.sand, block);
        } else
        {
            fail("Blocks.sand is null.");
        }
    }

    @Test
    public void testTilePlacement()
    {
        if (Blocks.chest != null)
        {
            BlockPos pos = new BlockPos(0, 0, 0);
            world.setBlockState(pos, Blocks.chest.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH));
            Block block = world.getBlockState(pos).getBlock();
            assertEquals("World.getBlockState() failed ", Blocks.chest, block);
            if (!(world.getTileEntity(pos) instanceof TileEntityChest))
            {
                fail("world.getTileEntity() returned the wrong tile\n" + world.getTileEntity(pos) + "  should equal TileEntityChest.");
            }
        } else
        {
            fail("Blocks.chest is null.");
        }
    }

    @Test
    public void testBlockRemoval()
    {
        BlockPos pos = new BlockPos(0, 0, 0);
        world.setBlockState(pos, Blocks.grass.getDefaultState());
        assertEquals("World.getBlockState() failed.", Blocks.grass, world.getBlockState(pos).getBlock());
        world.setBlockToAir(pos);
        assertEquals("World.getBlockState() failed.", Blocks.air, world.getBlockState(pos).getBlock());
    }

    @Test
    public void testTileRemoval()
    {
        BlockPos pos = new BlockPos(0, 0, 0);
        world.setBlockState(pos, Blocks.chest.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH));
        assertEquals("World.getBlockState() failed, should be a chest.", Blocks.chest, world.getBlockState(pos).getBlock());
        assertTrue("World.getTileEntity() should have returned a chest tile.", world.getTileEntity(pos) instanceof TileEntityChest);
        world.setBlockToAir(pos);
        world.updateEntities();
        assertEquals("World.getBlock() failed ", Blocks.air, world.getBlockState(pos).getBlock());
        TileEntity tile = world.getTileEntity(pos);
        assertTrue("World.getTile() should be null ", tile == null);
    }
}
