package com.builtbroken.tests.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * JUnit test for {@link FakeWorld}
 * Created by robert on 11/13/2014.
 */
public class TestFakeWorld
{

    private FakeWorld world = FakeWorld.newWorld("fake");

    @Test
    public void testBlockRegistry()
    {
        Object block = Block.REGISTRY.getObject(new ResourceLocation("sand"));
        Assertions.assertNotNull(block);
        Assertions.assertEquals(Block.getIdFromBlock((Block) block), 12);
    }

    @Test
    public void testCreation()
    {
        Assertions.assertNotNull(world, "Failed to create world.");
    }

    @Test
    public void testNullPlacement()
    {
        try
        {
            world.setBlockState(new BlockPos(0, 0, 0), null);
            Assertions.fail("World didn't catch null block.");
        } catch (NullPointerException e)
        {
            //This should be thrown :)
        }
    }

    @Test
    public void testBlockPlacement()
    {
        Assertions.assertNotNull(Blocks.SAND, "Blocks.sand is null.");

        final BlockPos pos = new BlockPos(0, 0, 0);
        world.setBlockState(pos, Blocks.SAND.getDefaultState());

        IBlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        Assertions.assertEquals(Blocks.SAND, block, "World.getBlockState() failed.");
    }

    @Test
    public void testTilePlacement()
    {
        Assertions.assertNotNull(Blocks.CHEST, "Blocks.sand is null.");

        BlockPos pos = new BlockPos(0, 0, 0);
        world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH));
        Block block = world.getBlockState(pos).getBlock();
        Assertions.assertEquals(Blocks.CHEST, block, "World.getBlockState() failed ");
        if (!(world.getTileEntity(pos) instanceof TileEntityChest))
        {
            Assertions.fail("world.getTileEntity() returned the wrong tile\n" + world.getTileEntity(pos) + "  should equal TileEntityChest.");
        }

    }

    @Test
    public void testBlockRemoval()
    {
        BlockPos pos = new BlockPos(0, 0, 0);
        world.setBlockState(pos, Blocks.GRASS.getDefaultState());
        Assertions.assertEquals(Blocks.GRASS, world.getBlockState(pos).getBlock(), "World.getBlockState() failed.");
        world.setBlockToAir(pos);
        Assertions.assertEquals(Blocks.AIR, world.getBlockState(pos).getBlock(), "World.getBlockState() failed.");
    }

    @Test
    public void testTileRemoval()
    {
        BlockPos pos = new BlockPos(0, 0, 0);
        world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH));
        Assertions.assertEquals(Blocks.CHEST, world.getBlockState(pos).getBlock(), "World.getBlockState() failed, should be a chest.");
        Assertions.assertTrue(world.getTileEntity(pos) instanceof TileEntityChest, "World.getTileEntity() should have returned a chest tile.");
        world.setBlockToAir(pos);
        world.updateEntities();
        Assertions.assertEquals(Blocks.AIR, world.getBlockState(pos).getBlock(), "World.getBlock() failed ");
        TileEntity tile = world.getTileEntity(pos);
        Assertions.assertTrue(tile == null, "World.getTile() should be null ");
    }
}
