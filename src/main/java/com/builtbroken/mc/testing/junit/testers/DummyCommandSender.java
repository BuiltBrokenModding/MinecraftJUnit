package com.builtbroken.mc.testing.junit.testers;

import com.builtbroken.mc.testing.junit.TestManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

/** Dummy version of {@link ICommandSender} that can be used for tests
 *
 * Created by Dark(DarkGuardsman, Robert) on 1/2/20.
 */
public class DummyCommandSender implements ICommandSender
{
    public World world;
    public MinecraftServer server;
    public Vec3d position = Vec3d.ZERO;

    public final Queue<ITextComponent> messages = new LinkedList();

    public DummyCommandSender()
    {

    }

    public DummyCommandSender(TestManager testManager)
    {
        world = testManager.getWorld();
        server = testManager.getServer();
    }

    @Override
    public String getName()
    {
        return "dummy";
    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName)
    {
        return true;
    }

    @Override
    public World getEntityWorld()
    {
        return world;
    }

    @Nullable
    @Override
    public MinecraftServer getServer()
    {
        return server;
    }

    @Override
    public void sendMessage(ITextComponent component)
    {
        messages.offer(component);
    }

    public String pollLastMessage() {
        return messages.poll().getUnformattedText();
    }

    @Override
    public BlockPos getPosition()
    {
        return new BlockPos(position);
    }

    @Override
    public Vec3d getPositionVector()
    {
        return position;
    }
}
