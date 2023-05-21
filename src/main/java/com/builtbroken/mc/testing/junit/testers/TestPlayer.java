package com.builtbroken.mc.testing.junit.testers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.mojang.authlib.GameProfile;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

/**
 * Version of the player designed for JUnit testing. Needs a server instance, server world, and profile data to be created.
 */
public class TestPlayer extends EntityPlayerMP
{

    private static GameProfile PROFILE_DEFAULT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE76"), "[UNIT_TESTER]");

    public final Queue<ImmutablePair<StatBase, Integer>> statsReceived = new LinkedList();
    public final Queue<ITextComponent> messages = new LinkedList();

    /**
     * Only constructor for this class
     *
     * @param server  - valid server instance, you should only create one for all of your tests
     * @param world   - server world, needs its own dimID and needs to be registered
     * @param profile = profile data, UUID is not required but name is required
     */
    public TestPlayer(MinecraftServer server, WorldServer world, GameProfile profile)
    {
        super(server, world, profile != null ? profile : PROFILE_DEFAULT, new PlayerInteractionManager(world));
    }

    @Deprecated
    public TestPlayer(MinecraftServer server, WorldServer world)
    {
        super(server, world, PROFILE_DEFAULT, new PlayerInteractionManager(world));
    }

    @Override
    public void addStat(StatBase par1StatBase, int par2)
    {
        statsReceived.offer(new ImmutablePair(par1StatBase, par2));
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z)
    {

    }

    @Override
    public void sendMessage(ITextComponent component)
    {
        this.messages.offer(component);
    }

    public String pollLastMessage() {
        return messages.poll().getUnformattedText();
    }

    @Override
    public void setGameType(GameType gameType)
    {
        this.interactionManager.setGameType(gameType);

        if (gameType == GameType.SPECTATOR)
        {
            this.spawnShoulderEntities();
            this.dismountRidingEntity();
        }
        else
        {
            this.setSpectatingEntity(this);
        }

        this.sendPlayerAbilities();
        this.markPotionsDirty();
    }

    /**
     * Clears the data assigned to the test player.
     *
     * Set location to 0x 0y 0z 0yaw 0 pitch
     * Clears inventory
     * Clears armor
     * Set current item to 0
     */
    public void reset()
    {
        this.setLocationAndAngles(0, 0, 0, 0, 0);
        for (EntityEquipmentSlot slotIn : EntityEquipmentSlot.values())
        {
            this.setItemStackToSlot(slotIn, ItemStack.EMPTY);
        }
        inventory.clear();
        this.inventory.currentItem = 0;
        setGameType(GameType.NOT_SET);
    }
}
