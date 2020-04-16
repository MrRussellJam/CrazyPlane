package com.example.plane;

import java.util.Random;

import android.graphics.*;

public class Enemy {
	
	// 敌机类型
	static final int TYPE_YELLOW = 0;
	static final int TYPE_RED = 1;
	static final int TYPE_BULE = 2;
	static final int TYPE_GREEN =3;
	static final int TYPE_BOSS = 4;
	
	//敌机属性下标值
	final int UN_SHOW = 0;
	final int SHOW = 1;
	final int BOOM = 2;
	final int ISVISABLE = 0;//下标对应的值： 0 不可视  1可视
	final int TYPE = 1;
	final int X = 2;
	final int Y = 3;
	final int WIDTH = 4;
	final int HEIGHT = 5;
	final int SPEED_X = 6;
	final int SPEED_Y = 7;
	final int HP = 8;
	final int SETP_COUNT = 9;
	final int SETP_INDEX = 10;
	final int ATK = 11;
	final int FRAME = 12;
	
	GameScreen gameScreen;
	Property property;
	int enemy[][];//敌机对象池数组
	Bitmap eImg[];//敌机图片数组
	Bitmap boomImg;//爆炸图片
	int timeCount;
	Random random;
	//不同类型敌机的初始位置
	int randomX[][] ={{50,350, 550,850},
					  {425,755},
					  {400,680},
					  {100,600,300,900},
					  {}
					  };
	//紫色敌机飞行路径设定数组
	int buleStep[] ={15,5,10,5,8};
	Bitmap bossbomb;
	
	public Enemy(GameScreen gameScreen){
		this.gameScreen = gameScreen;
		random = new Random();
		property = gameScreen.property;
		enemy = new int[40][13];
		eImg = new Bitmap[5];
		eImg[0] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.enemy0);
		eImg[1] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.enemy1);
		eImg[2] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.enemy2);
		eImg[3] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.enemy3);
		eImg[4] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.boss);
		boomImg = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.boomimg);
		bossbomb = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.boss_bomb);
		un_Show();
	}
	
	/*
	 * 所有敌机不可见
	 */
	void un_Show(){
		for(int i = 0; i < enemy.length; i++)
			enemy[i][ISVISABLE] = UN_SHOW;
		timeCount = 0;
	}
	
	protected void paint(Canvas canvas, Paint paint) {
		for(int i = 0; i < enemy.length; i++){
			switch(enemy[i][ISVISABLE]){
			case SHOW:
				int type = enemy[i][TYPE];
				canvas.drawBitmap(eImg[type],
						new Rect(0, 0, eImg[type].getWidth(), eImg[type].getHeight()), 
						new Rect(enemy[i][X], enemy[i][Y], enemy[i][X] + enemy[i][WIDTH], enemy[i][Y] + enemy[i][HEIGHT]), paint);
				break;
			case BOOM:
				if(enemy[i][TYPE]==TYPE_BOSS){
					canvas.drawBitmap(bossbomb, 
							new Rect(0, (4-enemy[i][FRAME])*bossbomb.getHeight()/5, bossbomb.getWidth(), (5-enemy[i][FRAME])*bossbomb.getHeight()/5 ),
							new Rect(enemy[i][X], enemy[i][Y], enemy[i][X]+enemy[i][WIDTH], enemy[i][Y]+enemy[i][HEIGHT]), paint);
					System.out.println("Boss Bomb  "+new Rect(0, (4-enemy[i][FRAME])*bossbomb.getHeight(), bossbomb.getWidth(), (5-enemy[i][FRAME])*bossbomb.getHeight()/5 ));
				}
				else
					canvas.drawBitmap(boomImg, 
							new Rect(enemy[i][FRAME]* (boomImg.getWidth()/6),
									0,
									(enemy[i][FRAME] + 1)* (boomImg.getWidth()/6),
									boomImg.getHeight()), 
							new Rect(enemy[i][X],
									enemy[i][Y],
									enemy[i][X] + enemy[i][WIDTH],
									enemy[i][Y] + enemy[i][HEIGHT]), paint);
				enemy[i][FRAME]++;	
				break;
			}
		}
	}
	
	void move(){
		if(gameScreen.state == GameScreen.GAME)
		for(int i = 0; i < enemy.length; i++){
			if(enemy[i][ISVISABLE] == SHOW){
				enemy[i][X] += enemy[i][SPEED_X];
				enemy[i][Y] += enemy[i][SPEED_Y];
				if(enemy[i][TYPE] == TYPE_BOSS)
					System.out.println(enemy[i][HP]);
			}
		}
	}
	
	/*
	 * 判断敌机有没有超出屏幕范围
	 */
	void setVisiable(){
		for(int i = 0; i < this.enemy.length; i++){
			if(enemy[i][ISVISABLE] == SHOW){
				if( enemy[i][X] + enemy[i][WIDTH] < 0 || enemy[i][X] > GameView.SCREEN_WIDTH || enemy[i][Y] > GameView.SCREEN_HEIGHT)
					enemy[i][ISVISABLE] = UN_SHOW;
			}
			else if(enemy[i][ISVISABLE] == BOOM && enemy[i][FRAME] > 5){
				int ranNum = Math.abs(random.nextInt()%10)+1;
				switch (enemy[i][TYPE]) {
				case Enemy.TYPE_GREEN:
					if(ranNum<=4)	
						property.creatProperty(Property.POWER, enemy[i][X], enemy[i][Y]);
					break;
				case Enemy.TYPE_BULE:
					if(ranNum >= 5 && ranNum<=8)
						property.creatProperty(Property.BOOM, enemy[i][X], enemy[i][Y]);
					break;
				case Enemy.TYPE_RED:
					if(ranNum >=0 && ranNum<=10)
						property.creatProperty(Property.LEVER_UP, enemy[i][X], enemy[i][Y]);
					break;
				}
				enemy[i][ISVISABLE] = UN_SHOW;
			}
			else if(enemy[i][ISVISABLE] == BOOM && enemy[i][TYPE] == TYPE_BOSS && enemy[i][FRAME] > 4)
				enemy[i][ISVISABLE] = UN_SHOW;
		}
	}
	
	/*
	 * 碰撞检测
	 */
	void collidesWith(){
		for(int i = 0; i < enemy.length; i++){
			switch (enemy[i][ISVISABLE]) {
			case SHOW:
				Player p = gameScreen.player;
				switch (p.getState()) {
				case Player.NORMAL:
					int x = enemy[i][X];
					int y = enemy[i][Y];
					int w = enemy[i][WIDTH];
					int h = enemy[i][HEIGHT];
					if(Collision.collides(x, y, w, h, p.getX(), p.getY(), p.getWidth(), p.getHeight())){
						if(enemy[i][TYPE] != TYPE_BOSS)
							enemy[i][ISVISABLE] = BOOM;
						else
							enemy[i][HP] -= p.getAttack()*2;
						p.setHp(enemy[i][ATK]);
					}
					break;
				} 
				break;
			}
		}
	}
	/*
	 * 创建敌机
	 */
	void createEnemy(int type){
		int index = 0;
		switch (type) {
		case TYPE_YELLOW:
			int num = random.nextInt()%4;
			for(int i = 0; i < enemy.length; i++)
				if(enemy[i][ISVISABLE] == UN_SHOW){
					enemy[i][ISVISABLE] = SHOW;
					enemy[i][TYPE] = TYPE_YELLOW;
					enemy[i][WIDTH] = eImg[TYPE_YELLOW].getWidth()/2;
					enemy[i][HEIGHT] = eImg[TYPE_YELLOW].getHeight()/2;
					enemy[i][X] = randomX[TYPE_YELLOW][Math.abs(num)];
					enemy[i][Y] = -eImg[TYPE_YELLOW].getHeight()/2;
					enemy[i][SPEED_X] = 0;
					enemy[i][SPEED_Y] = 32;
					enemy[i][HP] = 18;
					enemy[i][ATK] = 36;
					enemy[i][FRAME] = 0;
					break;
				}
			break;
		case TYPE_GREEN:
			int count = 0;
			index = Math.abs(random.nextInt(50)%2);
			for (int i = 0; i < enemy.length; i++) {
				if(enemy[i][ISVISABLE] == UN_SHOW)
				{
					enemy[i][ISVISABLE] = SHOW;
					enemy[i][TYPE] = TYPE_GREEN;
					enemy[i][HP] = 20;
					enemy[i][WIDTH]  = eImg[TYPE_GREEN].getWidth()/2;
					enemy[i][HEIGHT] = eImg[TYPE_GREEN].getHeight()/2;
					if(index == 0)
						enemy[i][X]  = randomX[TYPE_GREEN][count];
					else
						enemy[i][X]  = randomX[TYPE_GREEN][count+2];
					enemy[i][Y]  = -enemy[i][HEIGHT];
					enemy[i][SPEED_X]  = 10*(count==0?1:-1);
					enemy[i][SPEED_Y] = 25;
					enemy[i][ATK] = 30;
					enemy[i][FRAME] = 0;
					count++;
					if(count == 2)
						break;
				}
			}
			break;
		case TYPE_RED:
			index = Math.abs(random.nextInt()%2);
			for (int i = 0; i < enemy.length; i++) {
				if(enemy[i][ISVISABLE] == UN_SHOW)
				{
					enemy[i][ISVISABLE] = SHOW;
					enemy[i][TYPE] = TYPE_RED;
					enemy[i][HP] = 24;
					enemy[i][WIDTH] = eImg[type].getWidth()/2;
					enemy[i][HEIGHT] = eImg[type].getHeight()/2;
					enemy[i][X] = randomX[TYPE_RED][index];
					enemy[i][Y] = -enemy[i][HEIGHT];
					enemy[i][SPEED_X] = 14+index*-28;
					enemy[i][SPEED_Y] = 6;
					enemy[i][ATK] = 2*enemy[i][HP];
					enemy[i][FRAME] = 0;
					break;
				}
			}			
			break;
		case TYPE_BULE:
			count = 0;
			for (int i = 0; i < enemy.length; i++) {
				if(enemy[i][ISVISABLE] == UN_SHOW)
				{
					enemy[i][ISVISABLE] = SHOW;
					enemy[i][TYPE] = TYPE_BULE;
					enemy[i][HP] = 35;
					enemy[i][WIDTH] = eImg[type].getWidth()/2;
					enemy[i][HEIGHT] = eImg[type].getHeight()/2;
					enemy[i][X] = randomX[TYPE_BULE][count];
					enemy[i][Y] = -enemy[i][HEIGHT];
					enemy[i][SPEED_X] = 0;
					enemy[i][SPEED_Y] = 12;
					enemy[i][SETP_INDEX] = 0;
					enemy[i][SETP_COUNT] = 0;
					enemy[i][ATK] = 2*enemy[i][HP];
					enemy[i][FRAME] = 0;
					count++;
					if(count == 2)
						break;
				}
			}			
			break;
		case TYPE_BOSS:
			for(int i = 0;i<enemy.length;i++)
				enemy[i][ISVISABLE] = UN_SHOW;
			int i = 0;
			enemy[i][ISVISABLE] = SHOW;
			enemy[i][TYPE] = TYPE_BOSS;
			enemy[i][HP] = 1000;
			enemy[i][WIDTH] = eImg[type].getWidth()/2;
			enemy[i][HEIGHT] = eImg[type].getHeight()/2;
			enemy[i][X] = GameView.SCREEN_WIDTH/2-enemy[i][WIDTH]/2;
			enemy[i][Y] = -enemy[i][HEIGHT];
			enemy[i][SPEED_X] = 0;
			enemy[i][SPEED_Y] = 50;
			enemy[i][SETP_INDEX] = 0;
			enemy[i][SETP_COUNT] = 0;
			enemy[i][ATK] = enemy[i][HP]/2;
			enemy[i][FRAME] = 0;
			break;
		}
	}

	/*
	 * 设置路径
	 */
	void setPath()
	{
		for(int i = 0;i<enemy.length ;i++)
		{
			if(enemy[i][ISVISABLE] == SHOW)
			{
				switch (enemy[i][TYPE]) {
				case TYPE_RED:
					if(enemy[i][X] >= GameView.SCREEN_WIDTH-enemy[i][WIDTH])
						enemy[i][SPEED_X] = -14;
					else if(enemy[i][X] <= 8)
						enemy[i][SPEED_X] = 14;
					break;
				
				case TYPE_BULE:
					enemy[i][SETP_COUNT]++;
					switch (enemy[i][SETP_INDEX]) {
					case 0:// 下
						if(enemy[i][SETP_COUNT] == buleStep[0])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 1;
							enemy[i][SPEED_X] = -8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					case 1://左
						if(enemy[i][SETP_COUNT] == buleStep[1])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 2;
							enemy[i][SPEED_X] = 8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					case 2://右
						if(enemy[i][SETP_COUNT] == buleStep[2])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX]= 3;
							enemy[i][SPEED_X] = -8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					
					case 3://左
						if(enemy[i][SETP_COUNT] == buleStep[3])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 4;
							enemy[i][SPEED_X] = 0;
							enemy[i][SPEED_Y] = -10;
						}
						break;
					case 4://上
						if(enemy[i][SETP_COUNT] == buleStep[4])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 0;
							enemy[i][SPEED_X] = 0;
							enemy[i][SPEED_Y] = 12;
						}
						break;
					}
					break;
				case TYPE_BOSS:
					enemy[i][SETP_COUNT]++;
					switch (enemy[i][SETP_INDEX]) {
					case 0:// 下
						if(enemy[i][SETP_COUNT] == 10)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 1;
							enemy[i][SPEED_X] = -8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					case 1://左
						if(enemy[i][SETP_COUNT] == 5)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 2;
							enemy[i][SPEED_X] = 8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					case 2://右
						if(enemy[i][SETP_COUNT] ==10)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX]= 3;
							enemy[i][SPEED_X] = -8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					
					case 3://左
						if(enemy[i][SETP_COUNT] == 5)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 4;
							enemy[i][SPEED_X] = 0;
							enemy[i][SPEED_Y] = -10;
						}
						break;
					case 4://上
						if(enemy[i][SETP_COUNT] == 8)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 0;
							enemy[i][SPEED_X] = 0;
							enemy[i][SPEED_Y] = 20;
						}
						break;
					}
					break;
				}
			}
		}
	}
	
	
	
	protected void logic() {
		timeCount++;
		if(gameScreen.player.getState() != Player.SKILL)
		{
			if(timeCount < 400)
			{
				if(timeCount%13 == 0)
					createEnemy(TYPE_YELLOW);
				if (timeCount%29 == 0)
					createEnemy(TYPE_GREEN);
				if(timeCount%47 == 0)
					createEnemy(TYPE_RED);
				if (timeCount %119 == 0)
					createEnemy(TYPE_BULE);
			}else if(timeCount == 400)
				createEnemy(TYPE_BOSS);
		}
		move();
		setPath();
		setVisiable();
		collidesWith();
	}
	
}
