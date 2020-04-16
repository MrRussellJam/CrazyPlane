package com.example.plane;

import android.graphics.*;

public class Player {

	public final static int UP = 0;		//��
	public final static int DOWN = 1;		//��
	public final static int LEFT = 2;		//��
	public final static int RIGHT = 3;		//��
	public final static int NORMAL = 0;		//��ͨ
	public static final int LEVELUP = 1;	//����
	public static final int SKILL = 2;		//ը��
	public static final int RECOVER = 3;	//����
	public static final int DEAD = 4;	//����
	final static int MAX_HP = 1000;
	
	Bitmap plane;
	GameScreen gameScreen;
	int timeCount;
	
	private int gameState;		//״̬
	private int hp;				//Ѫֵ
	private int life;			//����
	private int speed;			//�ٶ�
	private int x, y;			//λ��
	private int width, height;	//���
	private int direction;		//����
	private int level;			//�ȼ�
	private boolean isMove;		//
	private int skillNum;		//ը������
	private int attack;			//����
	
	
	
	public Player(GameScreen gameScreen){
		this.gameScreen = gameScreen;
		plane = BitmapFactory.decodeResource( gameScreen.gameView.getResources(), R.drawable.player);
		width = plane.getWidth()/7;
		height = plane.getHeight()/7;
		this.timeCount = 0;
		init();
	}
	
	void init(){
		this.gameState = NORMAL;
		this.hp = MAX_HP;
		this.life = 3;
		this.speed = 30;
		this.x = GameView.SCREEN_WIDTH/2 - width/2;
		this.y = GameView.SCREEN_HEIGHT - height - 80;
		this.direction = UP;
		this.level = 1;
		this.skillNum = 1;
		this.isMove = false;
		this.attack = 20;
	}
	
	int getDirection(){
		return this.direction;
	}
	
	int getSkill(){
		return this.skillNum;
	}
	
	void addSkill(){
		if(skillNum < 3)
			skillNum++;
	}
	
	int getState(){
		return this.gameState;
	}
	
	void setState(int state){
		switch (state) {
		case LEVELUP:
			if(level < 3){
				this.level += 1;
				this.attack *= 3;	
			}
			this.gameState = NORMAL;
			break;
		case SKILL:
			if(this.skillNum >= 1)
			{
				this.gameState = SKILL;
				this.skillNum--;
				gameScreen.bullet.createPlayerBoom();
			}
			break;
		case RECOVER:
			this.gameState = NORMAL;
			if(hp + 100 < MAX_HP)
				hp += 100;
			else 
				hp = MAX_HP;
			timeCount = 0;
			break;
		case NORMAL:
			this.gameState = NORMAL;
			break;
		}
	}

	int getAttack(){
		return this.attack;
	}
	
	int getMaxhp(){
		return MAX_HP;
	}
	
	int getWidth(){
		return this.width;
	}
	
	int getHeight(){
		return this.height;
	}
	
	int getX(){
		return this.x;
	}
	
	int getY(){
		return this.y;
	}
	
	void setLevelUp(){
		if(level < 2)
			level++;
	}
	
	int getLevel(){
		return level;
	}

	int getLife(){
		return this.life;
	}
	
	int getHp(){
		return this.hp;
	}
	
	void changeDirection(int direction){
		isMove = true;
		this.direction = direction;
	}
	
	void stop(){
		isMove = false;
		this.direction = UP;
	}
	
	void setHp(int attack){
		if(this.hp > attack)
			this.hp -= attack;
		else{
			hp = 0;
			if(life > 0){
				life -= 1;
				hp = MAX_HP;
			}
			else
				this.gameScreen.state = GameScreen.LOSE;
		}
	}
	
	void move(){
		if(isMove)
			switch (this.gameState) {
			case NORMAL:
			case RECOVER:
			case LEVELUP:
				switch(this.direction){
				case UP:
					if(this.y > speed)
						this.y -= speed;
					break;
				case DOWN:
					if(this.y + speed + this.height < GameView.SCREEN_HEIGHT)
						this.y += speed;
					break;
				case LEFT:
					if(this.x > speed)
						this.x -= speed;
					break;
				case RIGHT:
					if(this.x + speed + this.width < GameView.SCREEN_WIDTH)
						this.x += speed;
					break;
				}
				break;
		}
	}
	
	
	protected void paint(Canvas canvas, Paint paint){
		canvas.drawBitmap(plane, 
				new Rect(0, 0, plane.getWidth(), plane.getHeight()),
				new Rect(x, y, x + width, y + height), paint);
	}
	
	protected void logic(){
		move();
		switch (gameState) {
		case NORMAL:
			System.out.println("NORMAL");
			break;
		case RECOVER:
			System.out.println("recover");
			break;
		case SKILL:
			System.out.println("SKILL");
			break;
		}
	}
	
}
