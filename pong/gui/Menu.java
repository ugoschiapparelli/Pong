package pong.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pong.Main;


public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel nomServLabel, nbPlayerLabel, ClSeLabel, endGameLabel; //Les différents menu
	private JComboBox nbPlayer, ClSe; //Type de menu
	private JTextField nomServ, endGame; //Type de menu

  public Menu(){
    this.setTitle("Pong");
    this.setSize(300, 350);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);      
    this.initComponent();
    this.setVisible(true);
  }

  private void initComponent(){
	  
	  	//Solo ou multijoueur
	    JPanel panNbPlayer = new JPanel();
	    panNbPlayer.setBackground(Color.white);
	    panNbPlayer.setPreferredSize(new Dimension(220, 60));
	    panNbPlayer.setBorder(BorderFactory.createTitledBorder("Mode de jeu"));
	    nbPlayer = new JComboBox();
	    nbPlayer.addItem("Solo");
	    nbPlayer.addItem("Multijoueur");
	    nbPlayerLabel = new JLabel("Mode : ");
	    panNbPlayer.add(nbPlayerLabel);
	    panNbPlayer.add(nbPlayer);
	    
	    //Client ou serveur
	    JPanel panClSe = new JPanel();
	    panClSe.setBackground(Color.white);
	    panClSe.setPreferredSize(new Dimension(220, 60));
	    panClSe.setBorder(BorderFactory.createTitledBorder("Reseau"));
	    ClSe = new JComboBox();
	    ClSe.addItem("Serveur");
	    ClSe.addItem("Client");
	    ClSeLabel = new JLabel("Reseau : ");
	    panClSe.add(ClSeLabel);
	    panClSe.add(ClSe);

	    //Le nom du client
	    JPanel panNomServ = new JPanel();
	    panNomServ.setBackground(Color.white);
	    panNomServ.setPreferredSize(new Dimension(220, 60));
	    nomServ = new JTextField();
	    nomServ.setPreferredSize(new Dimension(100, 25));
	    panNomServ.setBorder(BorderFactory.createTitledBorder("Nom du serveur adverse"));
	    nomServLabel = new JLabel("Saisir un nom :");
	    panNomServ.add(nomServLabel);
	    panNomServ.add(nomServ);
	    
	    //score pour finir la partie
	    JPanel panEndGame = new JPanel();
	    panEndGame.setBackground(Color.white);
	    panEndGame.setPreferredSize(new Dimension(220, 60));
	    endGame = new JTextField();
	    endGame.setPreferredSize(new Dimension(100, 25));
	    panEndGame.setBorder(BorderFactory.createTitledBorder("Score max"));
	    endGameLabel = new JLabel("Saisir un chiffre");
	    panEndGame.add(endGameLabel);
	    panEndGame.add(endGame);

	    JPanel content = new JPanel();
	    content.setBackground(Color.white);
	    content.add(panNbPlayer);
	    content.add(panClSe);
	    content.add(panNomServ);
	    content.add(panEndGame);
		
	    JPanel control = new JPanel();
	    JButton okBouton = new JButton("Play");
	    
	    okBouton.addActionListener(new ActionListener(){ //Bouton 
	      public void actionPerformed(ActionEvent arg0) { //si il est appuye, on associe les valeurs aux varialbe dans main
	    	Main.setIndice(1);
	    	Main.setIndiceSC(getClSe());
	    	Main.setNbPlayer(getSoloMulti());
	        if (Main.getIndiceSC() == -1){
	        	System.out.println("Probleme avec indice-Serveur-Client");
	        }
	        Main.setServerName((String)nomServ.getText());
	        String scoreMaxString = (String)endGame.getText();
	        //Serveur
	        if(!scoreMaxString.equals("") && Integer.parseInt(scoreMaxString) != 0){
	        	Main.setScoreMax(Integer.parseInt(scoreMaxString));
	        }
	        setVisible(false);
	      }
    
	    });

	    JButton cancelBouton = new JButton("Annuler"); //Bouton annuler
	    cancelBouton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	        setVisible(false);
	      }      
	    });

	    control.add(okBouton);
	    control.add(cancelBouton);

	    this.getContentPane().add(content, BorderLayout.CENTER);
	    this.getContentPane().add(control, BorderLayout.SOUTH);
	  }
  
  
  	  //Change les string en int 
  	  private int getClSe(){
  		  String stringTest = (String)ClSe.getSelectedItem();
  		  if(stringTest.equals("Serveur")){
  			  return 0;
  		  }
  		  else if (stringTest.equals("Client")){
  			  return 1;
  		  }
  		  return -1;
  	  }
  	  
  	  private int getSoloMulti(){
  		  String test = (String)nbPlayer.getSelectedItem();
  		  if(test.equals("Solo")){
  			  return 1;
  		  }
  		  else if(test.equals("Multijoueur")){
  			  return 2;
  		  }
  		  return -1;
  	  }
	}
