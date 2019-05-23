package com.builtbroken.tests.example;

import com.builtbroken.mc.testing.junit.server.FakeDedicatedServer;
import com.builtbroken.mc.testing.junit.testers.TestPlayer;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import com.builtbroken.mc.testing.junit.prefabs.AbstractTest;
import net.minecraft.world.GameType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Example test showing how to unit test the minecraft furnace
 *
 * @author Tyler, Dark
 */
public class TestFurnace extends AbstractTest
{

    static FakeWorldServer world;
    static TestPlayer player;
    static FakeDedicatedServer server;

    @BeforeAll
    public static void beforeAllTests()
    {
        //Setup game
        Bootstrap.register();

        //Create server for world for player
        server = FakeWorldServer.createServer("TestFurnace");
        world = FakeWorldServer.newWorld(server, 0, "TestFurnace");
        world.init(); //Must run after world creation in order to create a player without crashing

        player = new TestPlayer(server, world);


        //Limits block placements to inside our clean area so we can ensure each test case is valid
        world.setBlockCallback = (pos, state) ->
        {
            if (pos.getX() < 0 || pos.getX() > 15 || pos.getZ() < 0 || pos.getZ() > 15)
            {
                Assertions.fail("Placed block outside cleanup area: " + pos);
            }
            else if (pos.getY() < 0 || pos.getY() >= world.getHeight())
            {
                Assertions.fail("Placed block outside map area: " + pos);
            }
        };
    }

    @BeforeEach
    public void beforeEachTest()
    {
        Assertions.assertNotNull(Blocks.FURNACE);
    }

    @AfterEach
    public void afterEachTest()
    {
        //Clear chunk to make sure each test is valid
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(0, 0, 0);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                for (int y = 0; y < 256; y++)
                {
                    pos.setPos(x, y, z);
                    world.setBlockToAir(pos);
                }
            }
        }

        //Reset player
        player.reset();
    }

    @Test
    public void testSetBlock()
    {
        //Set
        world.setBlockState(ZERO_POS, Blocks.FURNACE.getDefaultState());

        //Check block
        final IBlockState state = world.getBlockState(ZERO_POS);
        final Block block = state.getBlock();
        Assertions.assertEquals(block, Blocks.FURNACE, "Should be a furnace.");

        //check tile
        final TileEntity tile = world.getTileEntity(ZERO_POS);
        Assertions.assertSame(tile.getClass(), TileEntityFurnace.class, "World.getTileEntity() should have returned a furnace tile. Actually got " + tile);
    }

    @Test
    public void testRemoveBlock()
    {
        //Goal of this test is to ensure our block cleans up properly when set to air
        //  in rare cases dirty tiles can remain due to bad setups

        //Place block
        world.setBlockState(ZERO_POS, Blocks.FURNACE.getDefaultState());

        //Validate placement
        Assertions.assertEquals(world.getBlockState(ZERO_POS).getBlock(), Blocks.FURNACE);
        Assertions.assertNotNull(world.getTileEntity(ZERO_POS));

        //Cycle tick once just for fun
        world.updateEntities();

        //Remove block
        world.setBlockToAir(ZERO_POS);

        //Tick to remove tile
        world.updateEntities();
        Assertions.assertNull(world.getTileEntity(ZERO_POS), "Tile should have been removed");
    }

    @Test
    public void testReplaceBlock()
    {
        //Goal of this test is to ensure our block cleans up properly when set to air
        //  in rare cases dirty tiles can remain due to bad setups

        //Place block
        world.setBlockState(ZERO_POS, Blocks.FURNACE.getDefaultState());

        //Validate placement
        Assertions.assertEquals(world.getBlockState(ZERO_POS).getBlock(), Blocks.FURNACE);
        Assertions.assertNotNull(world.getTileEntity(ZERO_POS));

        //Cycle tick once just for fun
        world.updateEntities();

        //Remove block
        world.setBlockState(ZERO_POS, Blocks.DIRT.getDefaultState());

        //Tick to remove tile
        world.updateEntities();
        Assertions.assertNull(world.getTileEntity(ZERO_POS), "Tile should have been removed");
    }

    @Test
    public void testPlacement()
    {
        //Place something to click
        world.setBlockState(ZERO_POS, Blocks.DIRT.getDefaultState());

        //Setup player
        player.setPosition(0, 1, 2);
        player.setGameType(GameType.SURVIVAL);
        player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Blocks.FURNACE));

        //Place block through player
        EnumActionResult actionResult = player.interactionManager.processRightClickBlock(player, world,
                player.getHeldItem(EnumHand.MAIN_HAND), EnumHand.MAIN_HAND,
                ZERO_POS, EnumFacing.UP, 0.5f, 0f, 0.5f);

        //Validate success state, keep in mind there are more than 1 reason success happened
        Assertions.assertEquals(EnumActionResult.SUCCESS, actionResult);

        //Check we consumed the item, closer to a check that we placed but not yet there
        Assertions.assertTrue(player.getHeldItem(EnumHand.MAIN_HAND).isEmpty());

        //Check block
        final IBlockState state = world.getBlockState(ZERO_POS.up()); //Block should be 1 above dirt
        final Block block = state.getBlock();
        Assertions.assertEquals(block, Blocks.FURNACE, "Should be a furnace.");
    }
}
