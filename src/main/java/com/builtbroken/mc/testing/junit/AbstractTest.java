package com.builtbroken.mc.testing.junit;

import junit.framework.TestCase;

import java.lang.reflect.Method;

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
    public void setUpForEntireClass()
    {

    }

    /**
     * Cleans up anything that was created for this test
     */
    public void tearDownForEntireClass()
    {

    }

    /**
     * Called before each test is run. Use
     * this to create object that several
     * test recreate. This way you save
     * on writing a lot of code.
     *
     * @param name - name of the method
     */
    public void setUpForTest(String name)
    {

    }

    /**
     * Called after each test is run. Use this
     * to remove any object created before each
     * test.
     *
     * @param name - name of the method
     */
    public void tearDownForTest(String name)
    {

    }

    /**
     * Used to check that the number of declared methods in
     * a class has not changed.
     *
     * @param clazz       - class to check
     * @param methodCount - number of methods to check
     */
    public static void checkNumberOfDeclaredMethods(Class clazz, int methodCount)
    {
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length != methodCount)
        {
            for (Method method : methods)
            {
                //Ignored as bamboo seems to add an extra method for code coverage
                if (method.getName().contains("jacocoInit") && methods.length == methodCount + 1)
                {
                    return;
                }
            }
            fail("There are " + methods.length + " but should be " + methodCount);
        }
    }

    /**
     * Used to generate test methods for large class.
     * <p/>
     * Eg. We devs are very lazy people. Plus, writing
     * unit test methods headers take longer than writing
     * the actual body of the test.
     *
     * @param clazz    - class to pull declared methods from
     * @param nextLine - for those that want { on the next line
     */
    public static void printOutMethodsAsTests(Class clazz, boolean nextLine)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (Method method : clazz.getDeclaredMethods())
        {
            //uppercase first letter in name
            String name = method.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());

            //Print java doc
            builder.append("/** Tests {@link " + clazz.getSimpleName() + "#" + method.getName() + "(");

            //Print method params for java doc
            for (int i = 0; i < method.getParameterTypes().length; i++)
            {
                Class<?> cla = method.getParameterTypes()[i];
                if (cla == Integer.TYPE)
                {
                    builder.append("int");
                }
                else
                {
                    builder.append(cla.getSimpleName());
                }

                if (i != method.getGenericParameterTypes().length - 1)
                {
                    builder.append(", ");
                }
            }
            builder.append(") */\n");

            //Print method
            builder.append("public void test" + name + "()" + (!nextLine ? "" : "\n") + "{" + (nextLine ? "" : "\n") + "\n}\n");
            builder.append("\n");
        }
        System.out.println(builder.toString());
    }

}
