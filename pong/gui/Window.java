package pong.gui;

import java.awt.Point;
import java.net.Socket;
import javax.swing.JFrame;

import pong.Main;

/**
 * A Window is a Java frame containing an Pong
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Pong component to be displayed
	 */
	private final Pong pong;

	/**
	 * Constructor
	 */
	public Window(Pong pong) {
		this.pong = pong;
		this.addKeyListener(pong);
	}

	/**
	 * Displays the Window using the defined margins, and call the
	 * {@link Pong#animate()} method of the {@link Pong} every 100ms
	 */
	public void displayOnscreen(Socket sock) {
		int fin = -1;//Si la variable passe 1, un joueur a quitte la partie
		add(pong);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//On ferme la fenetre si on appuye sur la croix
		setVisible(true); 
		Reseau r = new Reseau();
		Protected p = new Protected();
		//Gestion du score max
		if(Main.getIndiceSC() == 0){//Serveur
			do{
				r.sendMaxScore(sock, Main.getScoreMax());
			}while(!r.ack(sock, "score_recu"));
		}
		else{//Client
			r.readMaxScore(sock);
		}
		while (true){
			pong.animate();
			if(pong.getScore()[0] == Main.getScoreMax() || pong.getScore()[1] == Main.getScoreMax()){ //Si un des joueurs a atteint le score max
				BoxGameOver boxGameOver = new BoxGameOver(); //On affiche la boite de dialogue BoxGameOver
				if(pong.getScore()[0] == Main.getScoreMax()){
					fin = boxGameOver.BoxGameOverCreate(pong, this, 1);//On met la reponse des joueurs dans fin
				}
				if(pong.getScore()[1] == Main.getScoreMax()){
					fin = boxGameOver.BoxGameOverCreate(pong, this, 2);
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(pong.ballMyTurn()){
				Point[] ballData = new Point[2];
				ballData[0] = pong.getBallPosition();
				ballData[1] = pong.getSpeedPosition();
				//Envoi coordonnees ball, position et vitesse (x puis y) avec demande d'ack
				do{
					r.send(sock, ballData[0], "x_ballpos");
				}while(!r.ack(sock, "x_ballpos_recu"));
				do{
					r.send(sock, ballData[0], "y_ballpos");
				}while(!r.ack(sock, "y_ballpos_recu"));
				do{
					r.send(sock, ballData[1], "x_ballspeed");
				}while(!r.ack(sock, "x_ballspeed_recu"));
				do{
					r.send(sock, ballData[1], "y_ballspeed");
				}while(!r.ack(sock, "y_ballspeed_recu"));
			}
			else{
				//1 read par donnee envoyee
				r.read(sock);
				r.read(sock);
				r.read(sock);
				r.read(sock);
			}
			//Envoi coordonnees de la racket
			do{
				r.send(sock, pong.getCurrentRacketPosition(), "x_racket");
				r.read(sock);
			}while(!r.ack(sock, "x_racket_recu"));

			do{
				r.send(sock, pong.getCurrentRacketPosition(), "y_racket");
				r.read(sock);
			}while(!r.ack(sock, "y_racket_recu"));

			p.verificationRacket(pong);
			try {
				Thread.sleep(Pong.timestep);
			} catch (InterruptedException e) {
				// Rien
			};
			if(fin == 1){
				break;
			}
		}
	}
		
	public void displayOnscreen(){//Mode solo, on enlève la partie reseau, et le score max de la fonction precedente
		add(pong);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		while (true){
			pong.animate();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
