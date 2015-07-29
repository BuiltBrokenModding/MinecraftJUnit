package com.builtbroken.mc.testing.debug.profiler;


import com.builtbroken.jlib.lang.StringHelpers;

import java.util.HashMap;
import java.util.Map;

/** Used to log a single iteration of a complex operation for
 * performance checking.
 * Created by robert on 12/10/2014.
 */
public class RunProfile
{
    public String name;
    public boolean enabled = System.getProperty("runProfiler") != null && System.getProperty("runProfiler").equalsIgnoreCase("true");
    public HashMap<String, TimeSpan> times = new HashMap();

    protected int maxNameLength = 0;

    public RunProfile(String name)
    {
        this.name = name;
    }

    public void startSection(String name)
    {
        if(enabled)
        {
            //Used for generating output data with space padding
            if (name.length() > maxNameLength)
                maxNameLength = name.length();

            times.put(name, new TimeSpan(System.nanoTime()));
        }
    }

    public void endSection(String name)
    {
        if(enabled)
        {
            if (times.containsKey(name))
            {
                times.get(name).end = System.nanoTime();
            }
        }
    }

    public StringBuilder getOutputSimple()
    {
        StringBuilder string = new StringBuilder();
        String head = "=== " + name + " ===";
        string.append(head +"\n");
        //Run data header
        addRunData(string);
        string.append("\n\n");

        for(Map.Entry<String, TimeSpan> section: times.entrySet())
        {
            string.append(StringHelpers.padRight(section.getKey() + ":", maxNameLength + 2) + StringHelpers.formatNanoTime(section.getValue().getDelta()) +"\n");
        }

        String end = StringHelpers.padLeft("", head.length()).replace(" ", "=");
        string.append(end +"\n");
        return string;
    }

    public void addRunData(StringBuilder string)
    {

    }
}
