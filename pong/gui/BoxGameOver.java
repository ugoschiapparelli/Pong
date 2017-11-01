package pong.gui;

import javax.swing.*;

public class BoxGameOver {
	
	public int BoxGameOverCreate(Pong pong, Window window, int joueur){
		JOptionPane jop = new JOptionPane(); //Boite de dialogue
		String joueurW = Integer.toString(joueur); //Passage en string du joueur gagnant 
		int option = jop.showConfirmDialog(null,"Joueur " + joueurW + " a gagné. Voulez-vous rejouer ?", "Jeu fini", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);			
		if(option == JOptionPane.OK_OPTION){ //Si on clique sur OK
			int[] newTab = new int[2];
			newTab[0] = 0;
			newTab[1] = 0;
			pong.setScore(newTab); //réinitialisation du score
		}
		if(option == JOptionPane.NO_OPTION){ //Si on clique sur non
			window.dispose(); //On ferme la fenêtre 
			return 1; //On retourne un int servant de signal
		}
		return -1;
	}

}
