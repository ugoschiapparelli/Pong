package pong.gui;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pong.Main;

public class Reseau{

	/*
	 * Envoyer une coordonnee
	 */
	public void send(Socket sock, Point point, String coor){
		ObjectOutputStream outToServer;
		double pos;
		try {
			outToServer = new ObjectOutputStream(sock.getOutputStream());
			//On ecrit la coordonnee String
			outToServer.writeObject(coor);
			//S'il s'agit de la coordonnee x du point courant
			if(coor.equals("x_racket") || coor.equals("x_ballpos") || coor.equals("x_ballspeed"))
				pos = point.getX();
			else
				pos = point.getY();
			//On ecrit cette coordonnee
			outToServer.writeDouble(pos);
			//On envoie le tout vers le reseau
			outToServer.flush();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Recevoir une coordonnee
	 */
	public void read(Socket sock){          
		ObjectInputStream inServer;
		ObjectOutputStream outToServer;
		String coor = null;
		//Lecture
		try {
			inServer = new ObjectInputStream(sock.getInputStream());
			outToServer = new ObjectOutputStream(sock.getOutputStream());
			try {
				//Cast la coordonnee d'Object en String
				coor = (String) inServer.readObject();
				/*
				 * Selon la valeur de coor, on recupère ecrit la coordonnee
				 * suivante dans le reseau a l'emplacement correspondant
				 * puis on envoie un ack sous forme de String
				 */
				if(coor.equals("x_racket")){
					Main.otherRacket.setLocation(inServer.readDouble(), Main.otherRacket.getY());
					outToServer.writeObject("x_racket_recu");
				}
				else{
					if(coor.equals("y_racket")){
						Main.otherRacket.setLocation(Main.otherRacket.getX(), inServer.readDouble());
						outToServer.writeObject("y_racket_recu");
					}
					else{
						if(coor.equals("x_ballpos")){
							Main.ballPosition.setLocation(inServer.readDouble(), Main.ballPosition.getY());
							outToServer.writeObject("x_ballpos_recu");
						}
						else{
							if(coor.equals("y_ballpos")){
								Main.ballPosition.setLocation(Main.ballPosition.getX(), inServer.readDouble());
								outToServer.writeObject("y_ballpos_recu");
							}
							else{
								if(coor.equals("x_ballspeed")){
									Main.ballSpeed.setLocation(inServer.readDouble(), Main.ballSpeed.getY());
									outToServer.writeObject("x_ballspeed_recu");
								}
								else{
									if(coor.equals("y_ballspeed")){
										Main.ballSpeed.setLocation(Main.ballSpeed.getX(), inServer.readDouble());
										outToServer.writeObject("y_ballspeed_recu");
									}
								}
							}
						}
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}               
	}
	
	/*
	 * même principe que send mais avec un int
	 */
	public void sendMaxScore(Socket sock, int scoreMax){
		ObjectOutputStream outToServer;
		try {
			outToServer = new ObjectOutputStream(sock.getOutputStream());
			outToServer.writeObject("score");
			outToServer.writeInt(scoreMax);
			outToServer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * même principe que read, avec un int
	 */
	public void readMaxScore(Socket sock){
		ObjectInputStream inServer;
		ObjectOutputStream outToServer;
		try {
			inServer = new ObjectInputStream(sock.getInputStream());
			outToServer = new ObjectOutputStream(sock.getOutputStream());
			String response;
			try {
				response = (String) inServer.readObject();
				if(response.equals("score")){
					Main.setScoreMax(inServer.readInt());
					outToServer.writeObject("score_recu");
					outToServer.flush();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Traitement d'un ack
	 */
	public boolean ack(Socket sock, String message){
		ObjectInputStream inServer;
		String response;
		try {
			inServer = new ObjectInputStream(sock.getInputStream());
			try {
				//On recupère le String souhaite
				response = (String) inServer.readObject();
				//Si la reponse correspond au message attendu
				if(response.equals(message))
					return true;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//sinon
		return false;
	}
}