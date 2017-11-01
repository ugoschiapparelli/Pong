package pong.gui;


public class Tests {

	Pong pong;
	Window window;

	public Tests(){
		pong = new Pong(1, 2);
		window = new Window(pong);
	}

	public int TestsCollision(){
		this.InitialisationPosition();
		//Parcours des 2 raquettes
		for(int i = 0; i < 2; i++){
			for(int y_racket = 0; y_racket < Pong.getSizePongY() - this.pong.getRacket()[0].height; y_racket++){
				//Positionnement de la racket courante en y
				this.pong.getRacket()[i].position.setLocation(0, y_racket);
				for(int x_ball = 0; x_ball < Pong.getSizePongX() - this.pong.getBall().width; x_ball++){
					for(int y_ball = 0; y_ball < Pong.getSizePongY() - this.pong.getBall().height; y_ball++){
						//Positionnement de la balle
						this.pong.getBall().position.setLocation(x_ball, y_ball);
						/*
						 * Si la balle n'est pas cense collisionner, selon nos valeurs est que collision
						 * retourne true (ou l'inverse), alors il y a une erreur
						 */
						if(((this.pong.getRacket()[i].getX() >= x_ball + this.pong.getBall().width)
								|| (this.pong.getRacket()[i].getX() + this.pong.getRacket()[i].width <= x_ball)
								|| (y_racket >= y_ball + this.pong.getBall().height) 
								|| (y_racket + this.pong.getRacket()[i].height <= y_ball))
								== (this.pong.collision(this.pong.getBall(), this.pong.getRacket()[i]))){
							System.out.println("ERREUR TEST COLLISION");
							return -1;
						}
					}
				}
			}
		}
		//Si le test s'est bien termine
		return 0;	
	}

	public int TestsRebound(){
		this.InitialisationPosition();
		int lastSpeedY = Ball.BALL_SPEED;
		int lastSpeedX = Ball.BALL_SPEED;
		int lastPositionY = pong.getBall().position.y;
		pong.getRacket()[0].speed = Pong.RACKET_SPEED_JG;
		pong.getRacket()[1].speed = Pong.RACKET_SPEED_JD;
		//Parcours des raquettes
		for(int i = 0; i < 2; i++){
			for(int y_racket = 0; y_racket < Pong.getSizePongY() - this.pong.getRacket()[0].height; y_racket++){
				//Deplacement raquette courante
				this.pong.getRacket()[i].position.setLocation(0, y_racket);
				for(int x_ball = 0; x_ball < Pong.getSizePongX() - this.pong.getBall().width; x_ball++){
					for(int y_ball = 0; y_ball < Pong.getSizePongY() - this.pong.getBall().height; y_ball++){					
						//Deplacement balle courante
						this.pong.getBall().position.setLocation(x_ball, y_ball);
						//La fonction collision a ete verifiee precedemment et est donc utilisable
						if(pong.collision(this.pong.getBall(), this.pong.getRacket()[i])){
							lastSpeedY = this.pong.getBall().speed.y;
							lastSpeedX = this.pong.getBall().speed.x;
							lastPositionY = this.pong.getBall().position.y;
							this.pong.rebound(i);
							if((x_ball + this.pong.getBall().width <= this.pong.getRacket()[i].position.x) ||
									(x_ball >= this.pong.getRacket()[i].position.x + this.pong.getRacket()[i].width) ){
								int sp = this.pong.getBall().position.y + this.pong.getBall().height/2;
								int rk = this.pong.getRacket()[i].position.y + this.pong.getRacket()[i].height/5;       
								if(sp <= rk){
									if(this.pong.getBall().speed.y != lastSpeedY - 1 && this.pong.getBall().speed.x != - lastSpeedX){
										return -1;
									}
								}
								else{
									if(sp <= 2*rk){
										if(this.pong.getBall().speed.y != lastSpeedY - 1/2 && this.pong.getBall().speed.x != - lastSpeedX){
											return -1;
										}
									}
									else{
										if(sp <= 4*rk){
											if(this.pong.getBall().speed.y != lastSpeedY + 1/2 && this.pong.getBall().speed.x != - lastSpeedX){
												return -1;
											}
										}
										else{
											if(sp <= 5*rk){
												if(this.pong.getBall().speed.y != lastSpeedY + 1 && this.pong.getBall().speed.x != - lastSpeedX){
													return -1;
												}
											}
										}
									}
								}
							}
							else{
								if(Math.signum(this.pong.getRacket()[i].speed) != Math.signum(this.pong.getBall().speed.y)){
									if(this.pong.getBall().speed.y != -lastSpeedY){
										return -1;
									}
								}
								else{
									if(this.pong.getBall().position.y != lastPositionY + this.pong.getBall().speed.y){
										return -1;
									}
								}
							}
						}
					}
				}
			}
		}
		return 0;
	}

	public void InitialisationPosition(){
		//Initialisation de la position des raquettes 
		this.pong.getRacket()[0].position.setLocation(50, 0);
		this.pong.getRacket()[1].position.setLocation(930, 0);
		//Initialisation de la position de la balle
		this.pong.getBall().position.setLocation(0, 0);
	}

}
