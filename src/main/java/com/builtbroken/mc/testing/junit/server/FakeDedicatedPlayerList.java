package com.builtbroken.mc.testing.junit.server;

import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.management.PlayerList;

/**
 * Created by Dark on 9/3/2015.
 */
public class FakeDedicatedPlayerList extends DedicatedPlayerList
{
    public FakeDedicatedPlayerList(DedicatedServer p_i1503_1_)
    {
        super(p_i1503_1_);
    }
}
