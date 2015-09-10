package com.builtbroken.mc.testing.junit;

import junit.framework.Assert;

/**
 * Created by robert on 1/6/2015.
 */
public class AbstractTest extends Assert
{
    /**
     * Builds anything that all tests for this class need
     */
    public void setUpForEntireClass(){}

    /**
     * Cleans up anything that was created for this test
     */
    public void tearDownForEntireClass(){}

    public void setUpForTest(String name)
    {

    }

    public void tearDownForTest(String name)
    {

    }
}
