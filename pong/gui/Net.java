package pong.gui;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class Net extends PongItem{
	/*
	 * Filet apparaissant au milieu de l'écran
	 */
	
	
	protected final Image image;
	
	public Net(){ //Image apparaissant au milieu de l'écran 
		ImageIcon icon;
		
		this.image = Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource("image/net.png"));
		icon = new ImageIcon(image);
		this.width = icon.getIconWidth();
		this.height = icon.getIconHeight();
	}
}
