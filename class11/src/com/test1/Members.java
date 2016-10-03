package com.test1;

import java.io.*;
import java.util.Vector;


class Node {
	int direct;
	int x;
	int y;
	public Node(int x, int y, int direct) {
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}

//Recorder class
class Recorder {
	private static int eNum=8;
	private static int life=3;
	private static int score=0;
	private static Vector<Node>nodes=new Vector<Node>();
	public static Vector<Node> getNodes() {
		return nodes;
	}


	public static void setNodes(Vector<Node> nodes) {
		Recorder.nodes = nodes;
	}

	//Write record
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	private static Vector<EnemyTank> ets=new Vector<EnemyTank>();
	public static Vector<EnemyTank> getEts() {
		return ets;
	}


	public static void setEts(Vector<EnemyTank> ets) {
		Recorder.ets = ets;
	}


	public static void KeepRecoder() {
		try {
			fw=new FileWriter("c:/users/zhangx48/desktop/record.txt");
			bw=new BufferedWriter(fw);
			bw.write(Recorder.getScore()+"\r\n");
			bw.write(Recorder.geteNum()+"\r\n");
			bw.write(Recorder.getLife()+"\r\n");
			
			for (int i=0; i<ets.size();i++) {
				EnemyTank et=ets.get(i);
				if (et.isLive)
					bw.write(et.x+" "+et.y+" "+et.direct+"\r\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Read record
	static FileReader fr=null;
	static BufferedReader br=null;
	public static void ReadRecorder() {
		try {
			fr=new FileReader("c:/users/zhangx48/desktop/record.txt");
			br=new BufferedReader(fr);
			String[] sArr=new String[3];
			int i=0;
			while ((sArr[i]=br.readLine())!=null) {
				i++;
				if (i==3)
					break;
			}
			score=Integer.parseInt(sArr[0]);
			eNum=Integer.parseInt(sArr[1]);
			life=Integer.parseInt(sArr[2]);
			
			System.out.println(score+" "+eNum+" "+life);
			
			String s="";
			while ((s=br.readLine())!=null) {
				String []plot=s.split(" ");
				Node node=new Node(Integer.parseInt(plot[0]),Integer.parseInt(plot[1]),Integer.parseInt(plot[2]));
				nodes.add(node);
				
			}
			//Recorder.setNodes(nodes);
			//for (int r=0;r<nodes.size();r++)
				//System.out.println(nodes.get(5).x);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	
	public static int getScore() {
		return score;
	}
	public static void setScore(int score) {
		Recorder.score = score;
	}
	public static int geteNum() {
		return eNum;
	}
	public static void seteNum(int eNum) {
		Recorder.eNum = eNum;
	}
	public static int getLife() {
		return life;
	}
	public static void setLife(int life) {
		Recorder.life = life;
	}
	//Reduce number of tank
	public static void ReduceeNum() {
		eNum--;
	}
	
	public static void ReduceLife() {
		life--;
	}
	
	public static void GrowScore() {
		score++;
	}
	
	
	
}

class Bomb {
	int x,y;
	int life=9;
	boolean isLive=true;
	public Bomb(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public void lifeDown() {
		if(life>0)
			life--;
		else
			isLive=false;
	}
}


class Shot implements Runnable {
	int x=0;
	int y=0;
	int direct;
	int speed=3;
	//dead or live?????
	boolean IsLive=true;
	
	public Shot(int x, int y, int direct) {
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	@Override
	public void run() {
		
		// TODO Auto-generated method stub
		while(true) {
			//立刻发射会导致机器受不了
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(direct) {
			case 0:
				y-=speed;
				break;
			case 1:
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;
			}
			//System.out.println("x="+x+" y="+y);
			// Bullet need to die
			if(x<0||x>400||y<0||y>300) {
				this.IsLive=false;
				//System.out.println("hi"+this.IsLive);
				break;
			}
				
			
		}
	}
}



class Tank {
	//set speed
	int speed=1;
	int color;
	boolean isLive=true;
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	//plot
	int x=0;
	int y=0;
	// Direction
	//0 up, 1 right, 2 down, 3 left
	int direct=0;
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Tank (int x, int y) {
		this.x=x;
		this.y=y;
	}
}

//enemy,采用vector而不用arraylist因为vector线程安全
class EnemyTank extends Tank implements Runnable {
	int time=0;
	//Define a vector to visit all enemy tanks on panel
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
	public EnemyTank(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	//Get enemytanks vector on Mypanel
	public void setEts(Vector<EnemyTank>vv) {
		this.ets=vv;
	}
	
	// Judge whether be in collision with other tanks
	public boolean isTouchOtherEnemy() {
		boolean b=false;
		switch(this.direct) {
		case 0:
			for (int i=0;i<ets.size();i++) {
				EnemyTank et=ets.get(i);
				if (et!=this) {
					if (et.direct==0||et.direct==2) {
						// Judge two points
						if (this.x>et.x&&this.x<et.x+20&&this.y>et.y&&this.y<et.y+30)
							b=true;
						
						if (this.x+20>et.x&&this.x+20<et.x+20&&this.y>et.y&&this.y<et.y+30)
							b=true;
					}
					
					if (et.direct==1||et.direct==3) {
						if (this.x>et.x&&this.x<et.x+30&&this.y>et.y&&this.y<et.y+20) 
							b=true;
						
						if (this.x+20>et.x&&this.x+20<et.x+30&&this.y>et.y&&this.y<et.y+20)
							b=true;
					}
				}
			}
			break;
		case 1://right
			for (int i=0;i<ets.size();i++) {
				EnemyTank et=ets.get(i);
				if (et!=this) {
					if (et.direct==0||et.direct==2) {
						if (this.x+30>et.x&&this.x+30<et.x+20&&this.y>et.y&&this.y<et.y+30)
							b=true;
						
						if (this.x+30>et.x&&this.x+30<et.x+20&&this.y+20>et.y&&this.y+20<et.y+30)
							b=true;
					}
					
					if (et.direct==1||et.direct==3) {
						if (this.x+30>et.x&&this.x+30<et.x+30&&this.y+20>et.y&&this.y+20<et.y+20) 
							b=true;
						
						if (this.x+30>et.x&&this.x+30<et.x+30&&this.y>et.y&&this.y<et.y+20)
							b=true;
					}
				}
			}
			break;
		case 2:
			for (int i=0;i<ets.size();i++) {
				EnemyTank et=ets.get(i);
				if (et!=this) {
					if (et.direct==0||et.direct==2) {
						if (this.x>et.x&&this.x<et.x+20&&this.y+30>et.y&&this.y+30<et.y+30)
							b=true;
						
						if (this.x+20>et.x&&this.x+20<et.x+20&&this.y+30>et.y&&this.y+30<et.y+30)
							b=true;
					}
					
					if (et.direct==1||et.direct==3) {
						if (this.x>et.x&&this.x<et.x+30&&this.y+30>et.y&&this.y+30<et.y+20) 
							b=true;
						
						if (this.x+20>et.x&&this.x+20<et.x+30&&this.y+30>et.y&&this.y+30<et.y+20)
							b=true;
					}
				}
			}
			break;
		case 3:
			for (int i=0;i<ets.size();i++) {
				EnemyTank et=ets.get(i);
				if (et!=this) {
					if (et.direct==0||et.direct==2) {
						if (this.x>et.x&&this.x<et.x+20&&this.y>et.y&&this.y<et.y+30)
							b=true;
						
						if (this.x>et.x&&this.x<et.x+20&&this.y+20>et.y&&this.y+20<et.y+30)
							b=true;
					}
					
					if (et.direct==1||et.direct==3) {
						if (this.x>et.x&&this.x<et.x+30&&this.y>et.y&&this.y<et.y+20) 
							b=true;
						
						if (this.x>et.x&&this.x<et.x+30&&this.y+20>et.y&&this.y+20<et.y+20)
							b=true;
					}
				}
			}
			break;
		
		}
		return b;
	}
	
	// Define bullet for enemies 
	Vector<Shot> ss=new Vector<Shot>();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

			switch(this.direct){
			
			case 0:
				for (int i=0;i<30;i++) {
					if (y>0&&!this.isTouchOtherEnemy())
						y-=speed;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 1:
				for (int i=0;i<30;i++) {
					if (x<400&&!this.isTouchOtherEnemy())
						x+=speed;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
				break;
			case 2:
				for (int i=0;i<30;i++) {
					if(y<300&&!this.isTouchOtherEnemy())
						y+=speed;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 3:
				for (int i=0;i<30;i++) {
					if (x>0&&!this.isTouchOtherEnemy())
						x-=speed;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
			
			this.time++;
			if (time%1==0) {
				if (this.isLive) {
					if (ss.size()<5) {
						Shot s=null;
						switch (direct) {
						case 0:
							s=new Shot(x+8,y,0);
							ss.add(s);
							break;
						case 1:
							s=new Shot(x+30, y+8,1);
							ss.add(s);
							break;
						case 2:
							s=new Shot(x+8, y+30,2);
							ss.add(s);
							break;
						case 3:
							s=new Shot(x-2, y+8,3);
							ss.add(s);
							break;
						}
						Thread t=new Thread(s);
						t.start(); 
					}	
				}
			}
			this.direct=(int) (Math.random()*4);
			
			//Are enemies die?
			if (this.isLive==false)
				//exit thread
				break;
		}
	}
	
}





class Hero extends Tank {
	
	Shot s=null; 
	Vector<Shot> ss=new Vector<Shot>();
	public Hero(int x,int y) {
		super(x,y);
	} 
	
	// Fire Function
	public void ShotEnemy() {
		
		switch (this.direct) {
		case 0:
			s=new Shot(x+8,y,0);
			ss.add(s);
			break;
		case 1:
			s=new Shot(x+30, y+8,1);
			ss.add(s);
			break;
		case 2:
			s=new Shot(x+8, y+30,2);
			ss.add(s);
			break;
		case 3:
			s=new Shot(x-2, y+8,3);
			ss.add(s);
			break;
		}
		Thread t=new Thread(s);
		t.start();
		
	}
	
	//move up
	public void moveUp() {
		y-=this.getSpeed();
	}
	//move down
	public void moveDown() {
		y+=speed;
	}
	//move right
	public void moveRight() {
		x+=speed;
	}
	//move left
	public void moveLeft() {
		x-=speed;
	}
}
