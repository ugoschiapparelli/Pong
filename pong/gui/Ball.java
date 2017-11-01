package pong.gui;


import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class Ball extends PongItem{

	/**
	 * Speed of ball (in pixels per second)
	 */
	public static final int BALL_SPEED = 3;
	protected Image image;
	protected Point speed = new Point(BALL_SPEED, BALL_SPEED);
	protected int lastPositionY = 0;
	private int lastTouch = 1; //Dernière raquette ayant touché la balle
	
	
	public Ball NextPosition(){
		return new Ball(this.position.x+this.speed.x, this.position.y+this.speed.y);
	}
	
	public int getLastPositionY(){
		return this.lastPositionY;
	}
	
	public void setLastPositionY(int p){
		this.lastPositionY = p;
	}
	
	//Trois constructeurs différents pour 3 paramètres différents image et position (x, y) 
	
	public Ball(){
		ImageIcon icon;
		this.image = Toolkit.getDefaultToolkit().createImage( 
				ClassLoader.getSystemResource("image/ball.png")); //Chargement d'image
		icon = new ImageIcon(image);
		this.width = icon.getIconWidth();
		this.height = icon.getIconHeight();
		this.position = new Point(500, 300);
	}

	public Ball(int x, int y){
		ImageIcon icon;

		this.image = Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource("image/ball.png"));
		icon = new ImageIcon(image);
		this.width = icon.getIconWidth();
		this.height = icon.getIconHeight();
		this.position = new Point(x, y);
	}
	
	public Ball(int x, int y, String im){
		ImageIcon icon = null;
		this.image = Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource(im));
		icon = new ImageIcon(this.image);
		this.width = icon.getIconWidth();
		this.height = icon.getIconHeight();
		this.position = new Point(x, y);
	}
	
	public int getLastTouch(){
		return this.lastTouch;
	}
	
	public void setLastTouch(int player){
		this.lastTouch = player;
	}
}
