package pong;

public class MyThread extends Thread{
	
	public void run(){
		while(Main.getIndice() == 0){//Tant que la variable indice dans Main vaut 0, le thread existe (celui -ci est modifié dans menu.java)
			try {
				Thread.sleep(500);
			}
			catch (InterruptedException ex) {}
		}
	}
}
