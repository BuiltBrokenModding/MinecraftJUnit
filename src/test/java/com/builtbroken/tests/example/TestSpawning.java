package com.builtbroken.tests.example;

import com.builtbroken.mc.testing.junit.TestManager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by Dark(DarkGuardsman, Robert) on 1/2/2020.
 */
public class TestSpawning
{
    private static TestManager testManager = new TestManager("spawning", Assertions::fail);

    @AfterEach
    void afterEach() {
        testManager.cleanupBetweenTests();
    }

    @Test
    void spawnBat() {
        final World world = testManager.getWorld();

        //Create bat
        final EntityBat bat = new EntityBat(world);
        bat.forceSpawn = true;
        bat.setPosition(100, 10, 100);

        //Spawn bat, returns true if spawned
        Assertions.assertTrue(world.spawnEntity(bat));

        //Check that bat was added to the world
        Assertions.assertTrue(world.loadedEntityList.contains(bat));
        Assertions.assertEquals(1, world.loadedEntityList.size());
    }

    @Test
    void spawnSheep() {
        final World world = testManager.getWorld();

        //Create bat
        final EntitySheep sheep = new EntitySheep(world);
        sheep.forceSpawn = true;
        sheep.setPosition(100, 10, 100);

        //Spawn bat, returns true if spawned
        Assertions.assertTrue(world.spawnEntity(sheep));

        //Check that bat was added to the world
        Assertions.assertTrue(world.loadedEntityList.contains(sheep));
        Assertions.assertEquals(1, world.loadedEntityList.size());
    }
}
