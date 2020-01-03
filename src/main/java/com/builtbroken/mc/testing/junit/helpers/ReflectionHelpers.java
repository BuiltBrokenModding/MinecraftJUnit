package com.builtbroken.mc.testing.junit.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by Dark(DarkGuardsman, Robert) on 12/26/2019.
 */
public final class ReflectionHelpers
{

    public static Field removeFinal(Field field) throws Exception
    {
        //Force to be accessible
        field.setAccessible(true);

        //Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        return field;
    }

    public static void setStaticField(Class clss, String fieldName, Object value) {
        try
        {
            removeFinal(clss.getDeclaredField(fieldName)).set(null, value);
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to set static final field [" + fieldName + "] on class [" + clss + "]", e);
        }
    }

    public static void setPrivateField(Class clss, String fieldName, Object object, Object value)
    {
        try
        {
            Field field = clss.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
