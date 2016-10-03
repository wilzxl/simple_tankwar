/*
 * No overlapping
 * Different eps
 * Pause and continue
 * --Set speed of bullet and tank to 0, no direction change
 * Record scores
 * --File stream
 * Music
 * Record and exit
 * --plots of tanks and restore
 */

package com.test1;
import java.awt.*;
import javax.imageio.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
public class MyTankGame5 extends JFrame implements ActionListener{
	MyPanel mp=null;
	JMenuBar jmb=null;
	JMenu jm1=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	JMenuItem jmi3=null;
	JMenuItem jmi4=null;
	MyStartPanel msp=null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyTankGame5 mtg=new MyTankGame5();
		
	}
	// Constructor 
	public MyTankGame5() {
		jmb=new JMenuBar();
		jm1=new JMenu("Game(G)");
		jm1.setMnemonic('g');
		jmi1=new JMenuItem("Start New Game(S)");
		jmi1.setMnemonic('s');
		jmi1.setActionCommand("start");
		jmi1.addActionListener(this);
		jmi2=new JMenuItem("Exit Game(E)");
		jmi2.setMnemonic('e');
		jmi2.setActionCommand("exit");
		jmi2.addActionListener(this);
		jmi3=new JMenuItem("Save and Exit(R)");
		jmi3.setMnemonic('r');
		jmi3.setActionCommand("save");
		jmi3.addActionListener(this);
		jmi4=new JMenuItem("Continue(C)");
		jmi4.setMnemonic('c');
		jmi4.setActionCommand("continue");
		jmi4.addActionListener(this);
		
		jm1.add(jmi1);
		jm1.add(jmi4);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jmb.add(jm1);
		this.setJMenuBar(jmb);
		msp=new MyStartPanel();
		//mp=new MyPanel();
		// Set up thread
		this.add(msp);
		Thread t=new Thread(msp);
		t.start();
		
		//this.addKeyListener(mp);
		this.setSize(600,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent a) {
		// TODO Auto-generated method stub
		if (a.getActionCommand().equals("start")) {
			mp=new MyPanel("newGame");
			Thread t=new Thread(mp);
			t.start();
			this.remove(msp);
			this.add(mp);
			this.addKeyListener(mp);
			//Refresh, all cannot see new panel
			this.setVisible(true);
		} else if(a.getActionCommand().equals("exit")) {
			
			//Recorder.KeepRecoder();
			System.exit(0);
		} else if (a.getActionCommand().equals("save")) {
			Recorder.setEts(mp.ets);
			Recorder.KeepRecoder();
			System.exit(0);
		} else if (a.getActionCommand().equals("continue")) {
			//Recorder.ReadRecorder();

			mp=new MyPanel("continue");
			
			Thread t=new Thread(mp);
			t.start();
			this.remove(msp);
			this.add(mp);
			this.addKeyListener(mp);
			//Refresh, all cannot see new panel
			this.setVisible(true);
		}
	}
}

class MyStartPanel extends JPanel implements Runnable{
	int time=0;
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		if (time%2==0) {
			Font myfont=new Font("Arial",Font.BOLD,30);
			g.setColor(Color.yellow);
			
			g.setFont(myfont);
			g.drawString("Stage 1",150, 150);
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.time++;
			this.repaint();
		}
	}
	
}



//My Panel
class MyPanel extends JPanel implements KeyListener, Runnable{
	//String flag="newGame";
	//Define my tank
	Hero hero=null;
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	Vector<Node> nodes=new Vector<Node>();
	// Define bomb set
	Vector<Bomb> bombs=new Vector<Bomb>();	
	
	int enSize=Recorder.geteNum();
	
	//Define 3 images
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	public MyPanel(String flag) {
		//Recorder.ReadRecorder();
	
		//System.out.println(score+" "+eNum+" "+life);
		hero=new Hero(200,200);
		
		if(flag.equals("newGame")) {
			//System.out.println("this is test1");
			//Initialization should be in Constructor Function
			for (int i=0; i<enSize; i++) {
				//Create one enemy
				EnemyTank et=new EnemyTank((i+1)*50, 0);
				
				// Pass enemytanks vector to to enemytank to pass to Member.java
				et.setEts(ets);
				
				et.setColor(0);
				et.setDirect(2);
				Thread t=new Thread(et);
				t.start();
				// Add bullet
				Shot s=new Shot(et.x+10,et.y+30,2);
				et.ss.add(s);
				Thread t2=new Thread(s);
				t2.start();
				
				//Add to vector
				ets.add(et);			
			}
		} else{
			Recorder.ReadRecorder();
			System.out.println("this is test1");
			nodes=Recorder.getNodes();
			for (int i=0; i<nodes.size(); i++) {
				Node node=nodes.get(i);
				System.out.println("this is test");
				//Create one enemy
				EnemyTank et=new EnemyTank(node.x,node.y);
				
				// Pass enemytanks vector to to enemytank to pass to Member.java
				et.setEts(ets);
				
				et.setColor(0);
				et.setDirect(node.direct);
				Thread t=new Thread(et);
				t.start();
				// Add bullet
				Shot s=new Shot(et.x+10,et.y+30,2);
				et.ss.add(s);
				Thread t2=new Thread(s);
				t2.start();
				
				//Add to vector
				ets.add(et);			
			}
		}
		
		
		try {
			image1=ImageIO.read(new File("boom1.gif"));
			image2=ImageIO.read(new File("boom2.gif"));
			image3=ImageIO.read(new File("boom3.gif"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom1.gif"));
//		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom2.gif"));
//		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/boom3.gif"));
		
		
	}
	
	//Draw info function
	public void ShowInfo(Graphics g) {
		this.DrawTank(80, 330, g, 0, 0);
		this.DrawTank(135, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString("*"+Recorder.geteNum(), 105, 350);
		g.drawString("*"+Recorder.getLife(), 160, 350);
		
		// Score
		Font myfont=new Font("Times New Roma", Font.BOLD,20);
		g.setFont(myfont);
		g.drawString("Score", 510, 20);
		this.DrawTank(515, 40, g, 0, 0);
		g.setColor(Color.black);
		g.drawString("*"+Recorder.getScore(), 540, 60);
		
	}

	
	public void paint (Graphics g) {
		super.paint(g);
		//Draw my tank
		//left rectangle
		g.fillRect(0, 0, 500, 300);	
		this.ShowInfo(g);
		if(hero.isLive)
			this.DrawTank(hero.getX(), hero.getY(), g, hero.direct, 1);
		else if(Recorder.getLife()>0) {
			//this.DrawTank(200, 200, g, 0, 1);
			hero.isLive=true;		
			hero.setX(200);
			hero.setY(200);
		}
		
		//System.out.println("begin");
		//Draw bomb
		for (int i=0; i<bombs.size();i++) {
			//Get bomb
			Bomb b=bombs.get(i);
			if (b.life>6) {
				g.drawImage(image1, b.x, b.y, 30, 30, this);
			}
			else if(b.life>3) {
				g.drawImage(image2, b.x, b.y, 30, 30, this);
			}
			else {
				g.drawImage(image3, b.x, b.y, 30, 30, this);
			}
			b.lifeDown();//MyPanel是线程，在不停地重复画，就不停地调用lifedown，每画一次都会减少生命
			if (b.life==0)
				bombs.remove(b);
		}
		
		//Draw Enemy
		for (int i=0;i<ets.size();i++) {
			EnemyTank et=ets.get(i);
			if (et.isLive) {
				this.DrawTank(et.getX(), ets.get(i).getY(), g, et.getDirect(), 0);
				// Draw bullet for enemy
				for (int j=0; j<et.ss.size();j++) {
					Shot enemyShot=et.ss.get(j);
					if (enemyShot.IsLive) {
						g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
					}
					else
						et.ss.remove(enemyShot);
				}
			}
			
		}
		
		// Get every bullet in ss
		for (int i=0; i< hero.ss.size();i++) {
			
			Shot myShot=hero.ss.get(i);
			System.out.println(i);
			// Draw bullet
			if(myShot!=null&&myShot.IsLive==true) {
				g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
			}
			
			if (myShot.IsLive==false) {
				System.out.println(i+"dead!");
				hero.ss.remove(myShot);
			}
				
		}
		
		 
	}
	
	// Judge whether hit the enemy
	public boolean hitTank(Shot s, Tank et) {
		boolean b1=false;
		// Judge direction of tank
		switch(et.direct) {
		//Up and Down the same
		case 0: 
		case 2:
			if (s.x>et.x&&s.x<et.x+20&&s.y>et.y&&s.y<et.y+30) {
				//hit
				//bullet dies
				s.IsLive=false;
				//tank dies
				et.isLive=false;
				//Reduce total num of enemy
				b1=true;
				// Create bomb, and put into vector
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
		case 1:
		case 3:
			if (s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20) {
				//hit
				s.IsLive=false;
				//tank dies
				et.isLive=false;
				b1=true;
//				Recorder.ReduceeNum();
//				Recorder.GrowScore();
//				Recorder.ReduceLife();
				// Create bomb, and put into vector
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
		}
		return b1;
	}
	
	//Hit my tank
	public void hitMe() {
		//Get every enemy
		for (int i=0;i<ets.size();i++) {
			EnemyTank et=ets.get(i);
			//Get every bullet
			for (int j=0;j<et.ss.size();j++) {
				Shot enemyShot=et.ss.get(j);
				
				if(hero.isLive)
					if (this.hitTank(enemyShot, hero)) {
						Recorder.ReduceLife();
					}
			}
		}
	}
	
	//Hit enemy tank
	public void hitEnemyTank() {
		for (int i=0; i<hero.ss.size();i++) {
			//get bullet
			Shot myShot=hero.ss.get(i);
			
			if(myShot.IsLive=true) {
				for (int j=0;j<ets.size();j++) {
					EnemyTank et=ets.get(j);
					if(et.isLive)
						if (this.hitTank(myShot, et)) {
							Recorder.ReduceeNum();
							Recorder.GrowScore();
						}
				}
			}
		}
	}
	
	public void DrawTank(int x, int y, Graphics g, int direct, int type) {
		//Judge type
		switch (type) {
		case 0: g.setColor(Color.cyan);
				break; 
		case 1: g.setColor(Color.yellow);
				break;
		}
		//Judge direction of head
		switch (direct) {
		//head up
		case 0:
			g.fill3DRect(x, y, 5, 30,true);
			//Right
			g.fill3DRect(x+15,y,5,30,true);
			//Middle
			g.fill3DRect(x+5, y+5, 10, 20,false);
			g.fillOval(x+4, y+10, 10, 10);
			g.drawLine(x+9, y+15, x+9, y);
			break;
		//head right
		case 1:
			g.fill3DRect(x, y, 30, 5,true);
			//Right
			g.fill3DRect(x,y+15,30,5,true);
			//Middle
			g.fill3DRect(x+5, y+5, 20, 10,false);
			g.fillOval(x+10, y+4, 10, 10);
			g.drawLine(x+15, y+9, x+30, y+9);
			break;
		//head down
		case 2: 
			g.fill3DRect(x, y, 5, 30,true);
			//Right
			g.fill3DRect(x+15,y,5,30,true);
			//Middle
			g.fill3DRect(x+5, y+5, 10, 20,false);
			g.fillOval(x+4, y+10, 10, 10);
			g.drawLine(x+9, y+15, x+9, y+30);
			break;
		//head left
		case 3:
			g.fill3DRect(x, y, 30, 5,true);
			//Right
			g.fill3DRect(x,y+15,30,5,true);
			//Middle
			g.fill3DRect(x+5, y+5, 20, 10,false);
			g.fillOval(x+10, y+4, 10, 10);
			g.drawLine(x+15, y+9, x, y+9);
			break;
		}
	}
//a up, s down, a left, d right
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode()==KeyEvent.VK_W) {
			//set direction of tank
			this.hero.setDirect(0);
			this.hero.moveUp();
		}
		else if(e.getKeyCode()==KeyEvent.VK_D) {
			this.hero.setDirect(1);
			this.hero.moveRight();
		}
		else if (e.getKeyCode()==KeyEvent.VK_S) {
			this.hero.setDirect(2);
			this.hero.moveDown();
		}
		else if (e.getKeyCode()==KeyEvent.VK_A) {
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		
		// Judge Bullet
		if (e.getKeyCode()==KeyEvent.VK_J) {
			if (this.hero.ss.size()<=5)
				this.hero.ShotEnemy();
		}
		//REPAINT!!!
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	// For judge at any time
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//Judge whether hit enemy
			this.hitEnemyTank();
			
			// Judge whether hit me
			this.hitMe();
			
			//repaint()在run中导致paint（）函数不断进行
			this.repaint();
		}
	}
}

