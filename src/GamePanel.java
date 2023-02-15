import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 400;
	static final int SCREEN_HEIGHT = 400;
	static final int DOT_SIZE = 20;

	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(DOT_SIZE*DOT_SIZE); // how many object fit on the screen
	static final int DELAY = 100;
	final int x[] = new int[GAME_UNITS]; // this hold all the x - coordinates body parts of snack
	final int y[] = new int[GAME_UNITS]; // this hold all the y - coordinates body parts of snack
	int bodyParts = 4;
	int applesEaten;
	int appleX;
	int appleY;
	int direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));  //Size for game panel;
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	// Method for start game
	public void startGame() { 
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	// Method for paint
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		draw(g);
	}
	
	// Method for draw
	public void draw(Graphics g) { 
		if(running) {

		    g.setColor(Color.red);
		    g.fillOval(appleX, appleY, DOT_SIZE, DOT_SIZE);
		
		    for(int i = 0; i < bodyParts; i++) {
			    if(i == 0) {
				   g.setColor(Color.green);
				   g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
			    }else {
				    g.setColor(new Color(45, 180, 0));
				    //g.setColor(new Color(random.nextInt(200),random.nextInt(100),random.nextInt(100)));
				    g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
			    }
		    }
		    g.setColor(Color.white); // color of score
			g.setFont(new Font("Courier", Font.PLAIN, 15));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize()); // show the score
		}
		else {
			gameOver(g);
		}
		
	}

	// create a random new apple
	public void newApple() { 
		appleX = random.nextInt((int)(SCREEN_WIDTH / DOT_SIZE)) * DOT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT / DOT_SIZE)) * DOT_SIZE;
	}
	
	// Method for move snake
	public void move() { 
		for(int i = bodyParts; i >0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - DOT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + DOT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - DOT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + DOT_SIZE;
			break;	
		}
	}
	
	public void checkApple() { 
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() { // Method for collisions
		//checks if head collides with body
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	// Method for game over
	public void gameOver(Graphics g) { 
		// show score in end
		g.setColor(Color.white);
		g.setFont(new Font("Courier", Font.PLAIN, 15));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		
		// Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Courier", Font.BOLD, 45));
		FontMetrics metrics2 = getFontMetrics(g.getFont()); // Instant of font matrix
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); // show in center
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) { // for running 180 degree but we want to running 90 degree
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;	
			}
		}
		
	}

}
