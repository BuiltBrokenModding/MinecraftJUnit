package com.builtbroken.mc.testing.junit;

import junit.framework.TestCase;

/**
 * Prefab for running tests using the VoltzTestRunner. Must be implemented in order to
 * run the tests as these methods will be called.
 * Created by robert on 1/6/2015.
 */
public class AbstractTest extends TestCase
{
    /**
     * Builds anything that all tests for this class need
     */
    public void setUpForEntireClass() {}

    /**
     * Cleans up anything that was created for this test
     */
    public void tearDownForEntireClass() {}

    public void setUpForTest(String name)
    {

    }

    public void tearDownForTest(String name)
    {

    }
}
