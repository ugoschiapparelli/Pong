package pong.gui;

public class BallBonus extends Ball{
	
	protected int bonus_speed = -5;
	private boolean instance = false;
	private int[] scoreHist; //Variable indiquant le score des joueurs au moment où une BallBonus est crée 
	private int positionY = 0;
	private int frequence;
	private boolean malus;
	private int numBonus;
	
	public BallBonus(String im, int frequence, boolean malus, int numBonus){
		super(500, 20, im); 
		scoreHist = new int[2];
		scoreHist[0] = 0;
		scoreHist[1] = 0;
		this.frequence = frequence;
		this.malus = malus;
		this.numBonus = numBonus;
	}
	
	public int getSpeed(){
		return this.bonus_speed;
	}
	
	public boolean getInstance(){
		return this.instance;
	}
	
	public void setInstance(boolean b){
		this.instance = b;
	}
	
	public int getBonusSpeed(){
		return this.bonus_speed;
	}
	
	public void setBonusSpeedLeft(){
		if (!malus)
			this.bonus_speed = -5;
		else
			this.bonus_speed = 5;
	}
	
	public void setBonusSpeedRight(){
		if(!malus)
			this.bonus_speed = 5;
		else
			this.bonus_speed = -5;
	}
	
	public int getPositionY(){
		return this.positionY;
	}
	
	public boolean isHist(int x, int y){
		return this.scoreHist[0] == x && this.scoreHist[1] == y;
	}
	
	public void setHist(int p1, int p2){
		this.scoreHist[0] = p1;
		this.scoreHist[1] = p2;
	}
	
	public void deformationRacket(Racket rack){ //indique ce qu'il faut faire à la raquette lorqu'elle entre en collision avec le bonus
		if(this.numBonus == 0)//BallBonusLarger
			rack.setImage("./image/racketLarger.png");
		if(this.numBonus == 1)//BallBonusMini
			rack.setImage("./image/racketMini.png");
		if(this.numBonus == 2){//BallBonusAccel
			if(rack.position.x == 50)//On différencie raquette droite et gauche 
				Pong.RACKET_SPEED_JG = Pong.RACKET_SPEED_JD * 2;
			else
				Pong.RACKET_SPEED_JD = Pong.RACKET_SPEED_JD * 2;
		}
		if(this.numBonus == 3){//BallBonusSlow
			if(rack.position.x == 50)//On différencie raquette droite et gauche
				Pong.RACKET_SPEED_JG = Pong.RACKET_SPEED_JG / 4;
			else
				Pong.RACKET_SPEED_JD = Pong.RACKET_SPEED_JG / 4;
		}
	}
	
	public boolean apparition(int score1, int score2){ //indique quand apparait la balle bonus
		return (score1 + score2) % this.frequence == 0;
	}
}
