package com.builtbroken.mc.testing.debug;


import javax.swing.*;
import java.awt.*;

/**
 * Version of JPanel that can be triggered to update its components
 *
 * @author Darkguardsman
 */
@SuppressWarnings("serial")
public class UpdatePanel extends JPanel implements IGUIUpdate
{
	public UpdatePanel()
	{
	}

	public UpdatePanel(BorderLayout borderLayout)
	{
		super(borderLayout);
	}

	@Override
	public boolean update()
	{
		for (Component component : getComponents())
		{
			if (component instanceof IGUIUpdate)
			{
				((IGUIUpdate) component).update();
			}
		}
        return true;
	}
}
