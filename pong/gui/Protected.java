package pong.gui;

import pong.Main;

public class Protected {
	
	public int ancientPositionBall = 0;
	public int ancientPositionRacket = 0;
	
	
	public void verificationRacket(Pong pong){
		if(pong.getRacket()[0].position.x == 50){ //On différencie la raquette droite de la raquette gauche
			if(pong.getRacket()[0].bonus == 0 && pong.getRacket()[1].bonus == 0){ //Si les bonus ne sont pas activé
				if (((int) Main.otherRacket.getY() != this.ancientPositionRacket + Pong.RACKET_SPEED_JG && (int) Main.otherRacket.getY() != this.ancientPositionRacket - Pong.RACKET_SPEED_JG) && (int) Main.otherRacket.getY() != this.ancientPositionRacket){ 
					System.out.println("Il y a triche"); //Et que la position reçue pour la raquette adverse ne correspond pas à ce que l'on attendais, il y a triche
				}
			}
			this.ancientPositionRacket = Main.otherRacket.y; //Sinon l'ancienne position de la raquette adverse et retenue pour le calcul suivant
		}
		else{//Même chose si on est à drote dans le jeu
			if(pong.getRacket()[0].bonus == 0 && pong.getRacket()[1].bonus == 0){ 
				if (((int) Main.otherRacket.getY() != this.ancientPositionRacket + Pong.RACKET_SPEED_JD && (int) Main.otherRacket.getY() != this.ancientPositionRacket - Pong.RACKET_SPEED_JD) && (int) Main.otherRacket.getY() != this.ancientPositionRacket){ 
					System.out.println("Il y a triche"); 
				}
			}
			this.ancientPositionRacket = Main.otherRacket.y; 			
		}
	}

}
