package com.builtbroken.mc.testing.debug;

import java.awt.*;

/**
 * Simple label with an update method
 *
 * @author Darkguardsman
 */
@SuppressWarnings("serial")
public class UpdatedLabel extends Label implements IGUIUpdate
{
	String start_string = "I Am a Label";

	public UpdatedLabel(String start)
	{
		super(start);
		this.start_string = start;
	}

	@Override
	public boolean update()
	{
		this.setText(buildLabel());
        return start_string != null;
	}

	/**
	 * Recreates then returns the label's string value
	 */
	public String buildLabel()
	{
		return start_string;
	}
}
