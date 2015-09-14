package com.builtbroken.mc.testing.junit.icons;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import org.junit.Assert;

/**
 * Created by Dark on 9/14/2015.
 */
public class SudoIconReg implements IIconRegister
{
    @Override
    public IIcon registerIcon(String name)
    {
        Assert.assertTrue("Icon name can not be null", name != null);
        Assert.assertFalse("Icon name can not be empty", name.isEmpty());
        //TODO check to see the file exists
        return new SudoIcon(name);
    }
}
