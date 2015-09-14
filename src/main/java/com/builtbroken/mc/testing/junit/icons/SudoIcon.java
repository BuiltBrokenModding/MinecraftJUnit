package com.builtbroken.mc.testing.junit.icons;

import net.minecraft.util.IIcon;

/**
 * Created by Dark on 9/14/2015.
 */
public class SudoIcon implements IIcon
{
    private String name;

    public SudoIcon(String name)
    {
        this.name = name;
    }

    @Override
    public int getIconWidth()
    {
        return 16;
    }

    @Override
    public int getIconHeight()
    {
        return 16;
    }

    @Override
    public float getMinU()
    {
        return 0;
    }

    @Override
    public float getMaxU()
    {
        return 0;
    }

    @Override
    public float getInterpolatedU(double p_94214_1_)
    {
        return 0;
    }

    @Override
    public float getMinV()
    {
        return 16;
    }

    @Override
    public float getMaxV()
    {
        return 16;
    }

    @Override
    public float getInterpolatedV(double p_94207_1_)
    {
        return 0;
    }

    @Override
    public String getIconName()
    {
        return name;
    }
}
