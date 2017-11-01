package pong.gui;



import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class Racket extends PongItem{
	
	protected Image image;
	protected int speed;
	protected int bonus = -1;
	
	public Racket(int joueur){
		this.setImage("image/racket.png");
		if(joueur == 1){
			this.position = new Point(50, 0);
		}
		//Mode solo ou 2 joueurs
		if (joueur == 0 || joueur == 2){
			this.position = new Point(930 ,0);
		}
	}
	
	public Racket(int x, int y){
		this.position = new Point(x, y);
	}
	
	public void setImage(String im){
		ImageIcon icon;
		this.image = Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource(im));
		icon = new ImageIcon(image);
		this.width = icon.getIconWidth();
		this.height = icon.getIconHeight();
	}
	
	public int getX(){
		return this.position.x;
	}
	
	public int getY(){
		return this.position.y;
	}
	
	public int getBonus(){
		return this.bonus;
	}
	
	public void setBonus(int i){
		this.bonus = i;
	}
	
	public void updateBonus(){
		this.bonus++;
	}
	
	public int getSpeed(){
		return this.speed;
	}
	
	public void setSpeed(int i){
		this.speed = i;
	}
	
	public Racket NextPosition(){
		return new Racket(this.position.x, this.position.y+this.speed);
	}
}
