package game;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

public class Screen {
	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	int width = gd.getDisplayMode().getWidth();
	int height = gd.getDisplayMode().getHeight();
	//WIDTH = width;		-- auto sizing
	//HEIGHT = height;
	// height of the task bar
	Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());
	int taskBarSize =scnMax.bottom;
}
