package com.builtbroken.tests.example;

import com.builtbroken.mc.testing.junit.TestManager;
import com.builtbroken.mc.testing.junit.testers.TestPlayer;
import com.builtbroken.mc.testing.junit.world.FakeWorldServer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Example test showing how to unit test the minecraft furnace
 *
 * @author Tyler, Dark
 */
public class TestFurnace
{

    static TestManager testManager = new TestManager("TestFurnace", Assertions::fail);

    @BeforeAll
    public static void beforeAllTests()
    {
        testManager.lockToCenterChunk(Assertions::fail);
    }

    @AfterAll
    public static void afterAllTests()
    {
        testManager.tearDownTest();
    }

    @BeforeEach
    public void beforeEachTest()
    {
        Assertions.assertNotNull(Blocks.FURNACE);
    }

    @AfterEach
    public void afterEachTest()
    {
        testManager.cleanupBetweenTests();
    }

    private static Stream<Arguments> provideWorldAndPlayer()
    {
        return Stream.of(Arguments.of(testManager.getWorld(), testManager.getPlayer()));
    }

    @ParameterizedTest
    @MethodSource("provideWorldAndPlayer")
    public void testSetBlock(FakeWorldServer world, TestPlayer player)
    {
        //Set
        world.setBlockState(BlockPos.ORIGIN, Blocks.FURNACE.getDefaultState());

        //Check block
        final IBlockState state = world.getBlockState(BlockPos.ORIGIN);
        final Block block = state.getBlock();
        Assertions.assertEquals(block, Blocks.FURNACE, "Should be a furnace.");

        //check tile
        final TileEntity tile = world.getTileEntity(BlockPos.ORIGIN);
        Assertions.assertSame(tile.getClass(), TileEntityFurnace.class, "World.getTileEntity() should have returned a furnace tile. Actually got " + tile);
    }

    @ParameterizedTest
    @MethodSource("provideWorldAndPlayer")
    public void testRemoveBlock(FakeWorldServer world, TestPlayer player)
    {
        //Goal of this test is to ensure our block cleans up properly when set to air
        //  in rare cases dirty tiles can remain due to bad setups

        //Place block
        world.setBlockState(BlockPos.ORIGIN, Blocks.FURNACE.getDefaultState());

        //Validate placement
        Assertions.assertEquals(world.getBlockState(BlockPos.ORIGIN).getBlock(), Blocks.FURNACE);
        Assertions.assertNotNull(world.getTileEntity(BlockPos.ORIGIN));

        //Cycle tick once just for fun
        world.updateEntities();

        //Remove block
        world.setBlockToAir(BlockPos.ORIGIN);

        //Tick to remove tile
        world.updateEntities();
        Assertions.assertNull(world.getTileEntity(BlockPos.ORIGIN), "Tile should have been removed");
    }

    @ParameterizedTest
    @MethodSource("provideWorldAndPlayer")
    public void testReplaceBlock(FakeWorldServer world, TestPlayer player)
    {
        //Goal of this test is to ensure our block cleans up properly when set to air
        //  in rare cases dirty tiles can remain due to bad setups

        //Place block
        world.setBlockState(BlockPos.ORIGIN, Blocks.FURNACE.getDefaultState());

        //Validate placement
        Assertions.assertEquals(world.getBlockState(BlockPos.ORIGIN).getBlock(), Blocks.FURNACE);
        Assertions.assertNotNull(world.getTileEntity(BlockPos.ORIGIN));

        //Cycle tick once just for fun
        world.updateEntities();

        //Remove block
        world.setBlockState(BlockPos.ORIGIN, Blocks.DIRT.getDefaultState());

        //Tick to remove tile
        world.updateEntities();
        Assertions.assertNull(world.getTileEntity(BlockPos.ORIGIN), "Tile should have been removed");
    }

    @ParameterizedTest
    @MethodSource("provideWorldAndPlayer")
    public void testPlacement(FakeWorldServer world, TestPlayer player)
    {
        //Place something to click
        world.setBlockState(BlockPos.ORIGIN, Blocks.DIRT.getDefaultState());

        //Setup player
        player.setPosition(0, 1, 2);
        player.setGameType(GameType.SURVIVAL);
        player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Blocks.FURNACE));

        //Place block through player
        EnumActionResult actionResult = player.interactionManager.processRightClickBlock(player, world,
                player.getHeldItem(EnumHand.MAIN_HAND), EnumHand.MAIN_HAND,
                BlockPos.ORIGIN, EnumFacing.UP, 0.5f, 0f, 0.5f);

        //Validate success state, keep in mind there are more than 1 reason success happened
        Assertions.assertEquals(EnumActionResult.SUCCESS, actionResult);

        //Check we consumed the item, closer to a check that we placed but not yet there
        Assertions.assertTrue(player.getHeldItem(EnumHand.MAIN_HAND).isEmpty());

        //Check block
        final IBlockState state = world.getBlockState(BlockPos.ORIGIN.up()); //Block should be 1 above dirt
        final Block block = state.getBlock();
        Assertions.assertEquals(block, Blocks.FURNACE, "Should be a furnace.");
    }
}
