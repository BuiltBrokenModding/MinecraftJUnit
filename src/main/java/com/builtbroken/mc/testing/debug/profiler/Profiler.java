package com.builtbroken.mc.testing.debug.profiler;


import com.builtbroken.jlib.lang.StringHelpers;

import java.util.HashMap;

/**
 * Collections several profile runs
 * Created by robert on 12/10/2014.
 */
public class Profiler
{
    public final String name;
    public HashMap<String, RunProfile> profileRuns = new HashMap();

    public Profiler(String name)
    {
        this.name = name;
    }

    public synchronized RunProfile run(String runName)
    {
        profileRuns.put(runName, new RunProfile(runName));
        return profileRuns.get(runName);
    }

    public StringBuilder getOutputSimple()
    {
        StringBuilder string = new StringBuilder();
        String head = "=== " + name + " ===";
        string.append(head);

        String end = StringHelpers.padLeft("", head.length()).replace(" ", "=");
        string.append(end);
        return string;
    }
}
