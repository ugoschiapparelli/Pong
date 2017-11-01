package pong.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.Math;


import javax.swing.JPanel;

import pong.Main;

/**
 * An Pong is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */
public class Pong extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	/**
	 * Constant (c.f. final) common to all Pong instances (c.f. static)
	 * defining the background color of the Pong
	 */
	private static final Color backgroundColor = new Color(0); 

	/**
	 * Width of pong area
	 */
	private static final int SIZE_PONG_X = 1000;
	/**
	 * Height of pong area
	 */
	private static final int SIZE_PONG_Y = 600;
	/**
	 * Time step of the simulation (in ms)
	 */
	public static final int timestep = 10;
	/**
	 * Pixel data buffer for the Pong rendering
	 */
	private Image buffer = null;
	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;
	/**
	 * Speed of racket (in pixels per second)
	 */
	public static int RACKET_SPEED_JD = 15;//Vitesse de la raquette droite
	
	public static int RACKET_SPEED_JG = 15;//VItesse de la raquette gauche
	
	private int nbPlayer = 2;

	private Racket[] racket = new Racket[2];
	
	private int posOtherRacket = 0; 
	
	private int nbBonus = 4;
	
	private BallBonus[] tabBallBonus = new BallBonus[nbBonus];
	
	private Ball ball;	
	
	private Net net;

	
	/*
	 * score[0] = score du joueur 1
	 * score[1] = score du joueur 2
	 */
	private int[] score;
	
	public Pong(int currentRacketIndice, int otherRacketIndice){
		this.getRacket()[0] = new Racket(currentRacketIndice);
		this.getRacket()[1] = new Racket(otherRacketIndice);
		this.setBall(new Ball());
		this.setPreferredSize(new Dimension(SIZE_PONG_X, SIZE_PONG_Y));
		this.addKeyListener(this);
		this.setScore(new int[2]);
		this.getScore()[0] = 0;
		this.getScore()[1] = 0;
		this.net = new Net();
		this.tabBallBonus[0] = new BallBonusLarger();
		this.tabBallBonus[1] = new BallBonusMini();
		this.tabBallBonus[2] = new BallBonusAccel();
		this.tabBallBonus[3] = new BallBonusSlow();
		if(otherRacketIndice == 0){
			this.nbPlayer = 1;
		}
	}

	public int getPlayerScore(int player){
		return this.getScore()[player - 1];
	}
	
	protected Point getPositionBall(){
		return this.getBall().position;
	}
	
	public Point getCurrentRacketPosition(){
		return this.getRacket()[0].position;
	}
	
	public Point getBallPosition(){
		return getBall().position;
	}
	
	public Point getSpeedPosition(){
		return getBall().speed;
	}
	
	public int getSpeedRacket(){
		return this.getRacket()[0].speed;
	}
	
	public int[] getScore() {
		return score;
	}

	public void setScore(int[] score) {
		this.score = score;
	}
	
	public boolean ballMyTurn(){
		//Si la raquette courrante est la raquette de gauche
		if(getRacket()[0].position.getX() == 50){
			return getBallPosition().getX() <= SIZE_PONG_X / 2;
		}
		else{
			return getBallPosition().getX() > SIZE_PONG_X / 2;
		}
	}
	/**
         * Proceeds to the movement of the ball and updates the screen
	 */
	public void animate() {
		/* Mise à jour de la position des raquettes */
		if(!collision(getBall().NextPosition(), getRacket()[0].NextPosition())){ //Notre raquette
			getRacket()[0].position.y += getRacket()[0].speed;
			/* Limite de l'écran pour la raquette*/
			if (getRacket()[0].position.y < 0)								
				getRacket()[0].position.y = 0;
			if (getRacket()[0].position.y > SIZE_PONG_Y - getRacket()[0].height)	
				getRacket()[0].position.y = SIZE_PONG_Y - getRacket()[0].height;
		}
		 
		if(this.nbPlayer == 2){				
			/*Mise à jour de la raquette adverse*/
			getRacket()[1].position.x = (int) Main.otherRacket.getX();
			getRacket()[1].position.y = (int) Main.otherRacket.getY();
			
			/* Mise à jour de la balle*/
			if(ballMyTurn()){ //Si c'est mon tour
				for(int i = 0; i < 2; i++){
					if(collision(getBall().NextPosition(), getRacket()[i])){ //Et qu'il y a collision
						this.rebound(i); //on fait rebondir la balle
					}
				}
			}
			/*Si ce n'est pas notre tour*/
				else{
				getBall().position.x = (int) Main.ballPosition.getX();
				getBall().position.y = (int) Main.ballPosition.getY();
				getBall().speed.x = (int) Main.ballSpeed.getX();
				getBall().speed.y = (int) Main.ballSpeed.getY();
			}
		}
		
		if(this.nbPlayer == 1){//Mode solo
			/*Mise à jour de la position de la raquette ennemie*/
			getRacket()[1].position.y = getBall().NextPosition().position.y - this.posOtherRacket + 1;
			if(getRacket()[1].position.y < 0){
				getRacket()[1].position.y = 0;
			}
			if(getRacket()[1].position.y > 530){
				getRacket()[1].position.y = 530;
			}
			for(int i = 0; i < 2; i++){
				if(collision(getBall().NextPosition(), getRacket()[i])){
					this.rebound(i);
					if(i == 1){
						//Aléatoire de la position de la balle sur la raquette (raquette droite)
						this.posOtherRacket = getPosOtherRacket(); 
					}
					else{
						//Le score augmente si on tape la balle (raquette gauche)
						if(getBall().position.x >= getRacket()[i].position.x + getRacket()[i].width)
							this.getScore()[0]++; 
					}
				}
			}
		}
		
		getBall().position.translate(getBall().speed.x, getBall().speed.y);
		if (getBall().position.x < 0){ //Si la balle tape le bord gauche de l'écran
			try {
				//Mode multijoueur
				if(nbPlayer > 1){ 
					this.getScore()[1]++; //le score du joueur droit augmente
					for(int i = 0; i < 2; i++){
						if(getRacket()[i].bonus > -1){
							//Si un bonus existe, on augmente de 1 sa variable bonus (disparaît après 2 tours)
							getRacket()[i].updateBonus(); 
						}
					}
				}
				else{
					this.getScore()[0] = 0; //En mode solo, le score du joueur repart à 0
				}
				this.getBall().setLastPositionY(this.getBall().position.y);
				this.getBall().setLastTouch(2);
				Thread.sleep(1000);
				getBall().position.x = 500;
				getBall().position.y = 300;
				getBall().speed.x = -getBall().BALL_SPEED;
				getBall().speed.y = -getBall().BALL_SPEED;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Rebond sur le bord haut de l'écran
		if (getBall().position.y < 0){ 
			getBall().position.y = 0;
			getBall().speed.y = -getBall().speed.y;
		}
		//Si la balle tape le bord droit de l'écran
		if (getBall().position.x > SIZE_PONG_X){ 
			try { 
				if (nbPlayer > 1){ //On fait la même chose que pour le bord gauche
					this.getScore()[0]++;
					for(int i = 0; i < 2; i++){
						if(getRacket()[i].bonus > -1){
							getRacket()[i].updateBonus();
						}
					}
					this.getBall().setLastTouch(1);
					this.getBall().setLastPositionY(this.getBall().position.y);
				}
				Thread.sleep(1000);
				getBall().position.x = 500;
				getBall().position.y = 300;
				getBall().speed.x = getBall().BALL_SPEED;
				getBall().speed.y = getBall().BALL_SPEED;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (getBall().position.y > SIZE_PONG_Y - getBall().height){ //Rebond sur le bas de l'écran
			getBall().position.y = SIZE_PONG_Y - getBall().height;
			getBall().speed.y = -getBall().speed.y;
		}
		if(this.nbPlayer > 1){//Multi
			animateBallBonus(); //On anime les bonus
		}
		
		
		/* Et on met à jour l'écran */
		updateScreen(); 
	}
	
	public void animateBallBonus(){
		for(int j = 0; j < nbBonus; j++){ //Pour les bonus du tableau tabBallBonus
			
			if(this.tabBallBonus[j].getInstance()){//S'il existe on le fait avancer
				this.tabBallBonus[j].position.translate(this.tabBallBonus[j].getBonusSpeed(), 0);
			}
			
			if( this.tabBallBonus[j].apparition(this.getScore()[0], this.getScore()[1]) && !this.tabBallBonus[j].getInstance() && !this.tabBallBonus[j].isHist(this.getScore()[0], this.getScore()[1])){ 
				this.tabBallBonus[j].setInstance(true); //Initialisation de la balle, si condition d'apparition (fonction) et si il n'en existe pas déjà un et si le score n'as pas déjà creer une balle
				this.tabBallBonus[j].position.x = 500;
				this.tabBallBonus[j].position.y = getBall().lastPositionY + this.tabBallBonus[j].getPositionY();
				this.tabBallBonus[j].setHist(this.getScore()[0], this.getScore()[1]);
				if(getBall().getLastTouch() == 1) //Définition de l'orientation de la ballBonus
					this.tabBallBonus[j].setBonusSpeedLeft();
				else
					this.tabBallBonus[j].setBonusSpeedRight();
			}

			for(int i = 0; i < 2; i++){
				if(collision(this.tabBallBonus[j], getRacket()[i]) && this.tabBallBonus[j].getInstance()){
					this.tabBallBonus[j].setInstance(false); 
					this.tabBallBonus[j].deformationRacket(this.getRacket()[i]);
					this.getRacket()[i].bonus = 0;
				}
			}

			for(int i = 0; i < 2; i++){
				if(getRacket()[i].bonus == 2){//Au bout de deux tours, les bonus disparaissent
					getRacket()[i].bonus = -1;
					getRacket()[i].setImage("image/racket.png");
					if(getRacket()[0].position.x == 50)
						this.RACKET_SPEED_JG = 15;
					else
						this.RACKET_SPEED_JD = 15;
				}
			}
		}
	}

	public int getPosOtherRacket(){ //Donne un chiffre aléatoire, correspondant à une raquette coupe en 5 
		int lower = 1;
		int higher = 6;
		int random = (int)(Math.random() * (higher-lower)) + lower;
		return getRacket()[1].height/random;
	}
	
	public boolean collision(Ball ball, Racket racket){
		if((racket.position.x >= ball.position.x + ball.width)// trop à droite
	    || (racket.position.x + racket.width <= ball.position.x) // trop à gauche
	    || (racket.position.y >= ball.position.y + ball.height) // trop en bas
	    || (racket.position.y + racket.height <= ball.position.y))  // trop en haut
	          return false; 
	   else
		   	  return true;
	}
	
	public void rebound(int i){ 
		if((ball.position.x + ball.width <= racket[i].position.x) ||
			(ball.position.x >= racket[i].position.x + racket[i].width) ){
			
			int sp = this.getBall().position.y + this.getBall().height/2;
			int rk = this.getRacket()[i].position.y + this.getRacket()[i].height/5;       
		
			if(sp <= rk){
				getBall().speed.y = getBall().speed.y - 1;
			}
			else{
				if(sp <= 2*rk){
					getBall().speed.y = getBall().speed.y -1/2;
				}
				else{
					if(sp <= 4*rk){
						getBall().speed.y = getBall().speed.y +1/2;
					}
					else{
						if(sp <= 5*rk){
							getBall().speed.y = getBall().speed.y +1;
						}
					}
				}
			}
			ball.speed.x = - ball.speed.x;
				
		}
		else{			
			if(Math.signum(racket[i].speed) != Math.signum(ball.speed.y)){
				ball.speed.y = -ball.speed.y;
			}
			else{
				ball.position.y += ball.speed.y;
			}
		}
	}

	/*Contrôle quand le joueur presse un bouton*/
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				if(getRacket()[0].position.x == 50)
					getRacket()[0].speed = -RACKET_SPEED_JG;
				else
					getRacket()[0].speed = -RACKET_SPEED_JD;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				if(getRacket()[0].position.x == 50)
					getRacket()[0].speed = RACKET_SPEED_JG;
				else
					getRacket()[0].speed = RACKET_SPEED_JD;
				break;
			default:
				System.out.println("got press "+e);
		}
	}
	
	/*Controle quand le joueur relache un bouton*/
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				getRacket()[0].speed = 0;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				getRacket()[0].speed = 0;
				break;
			default:
				System.out.println("got release "+e);
		}
	}
	
	public void keyTyped(KeyEvent e) { }

	/*
	 * (non-Javadoc) This method is called by the AWT Engine to paint what
	 * appears in the screen. The AWT engine calls the paint method every time
	 * the operative system reports that the canvas has to be painted. When the
	 * window is created for the first time paint is called. The paint method is
	 * also called if we minimize and after we maximize the window and if we
	 * change the size of the window with the mouse.
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	
	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}

	/**
	 * Draw each Pong item based on new positions
	 */
	public void updateScreen() {
		if (buffer == null) {
			/*Au premier appel on initialise window*/
			buffer = createImage(SIZE_PONG_X, SIZE_PONG_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}
		/* Colorie la fond avec la couleur backgroundColor, ici noir*/
		graphicContext.setColor(backgroundColor);
		graphicContext.fillRect(0, 0, SIZE_PONG_X, SIZE_PONG_Y); /*dimension de la fenètre*/

		/* Dessine les différent objets*/
		graphicContext.drawImage(getBall().image, getBall().position.x, getBall().position.y, getBall().width, getBall().height, null);
		for(int i = 0; i < 2; i++){
			graphicContext.drawImage(getRacket()[i].image, getRacket()[i].position.x, getRacket()[i].position.y, getRacket()[i].width, getRacket()[i].height, null);
		}
		
		for(int i = 0; i < 6; i++){
			graphicContext.drawImage(net.image, 500, 1*(i*106), null);
		}
		
		for(int j = 0; j < nbBonus; j++){
			if(this.tabBallBonus[j].getInstance()){
				graphicContext.drawImage(this.tabBallBonus[j].image, this.tabBallBonus[j].position.x, this.tabBallBonus[j].position.y, this.tabBallBonus[j].width, this.tabBallBonus[j].height, null);
			}
		}
		
		Font font = new Font("Courier", Font.BOLD, 100);
		graphicContext.setFont(font);
		graphicContext.setColor(Color.white);
		String scoreLeft = Integer.toString(this.getScore()[0]);
		if(this.nbPlayer > 1){
			String scoreRight = Integer.toString(this.getScore()[1]);
			graphicContext.drawString(scoreRight, 550, 100);
		}
		graphicContext.drawString(scoreLeft, 400, 100);
		this.repaint();
	}

	public Racket[] getRacket() {
		return racket;
	}

	public void setRacket(Racket[] racket) {
		this.racket = racket;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}
	
	public static int getSizePongY(){
		return SIZE_PONG_Y;
	}
	
	public static int getSizePongX(){
		return SIZE_PONG_X;
	}

}
