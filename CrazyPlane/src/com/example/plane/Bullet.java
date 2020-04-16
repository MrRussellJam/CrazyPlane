package com.example.plane;

import android.graphics.*;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bullet {

	final int SHOW = 1;  //�ӵ���ʾ״̬
	final int UN_SHOW = 0;//�ӵ�δ��ʾ״̬
	final int ISVISIABLE = 0;//isvisiable��Ӧ�� ֵ0: ������   ֵ 1������
	final int TYPE = 1;//�ӵ�����
	final int X = 2; // X���� 
	final int Y = 3; // Y ����
	final int SPEED_X = 4; // x�����ϵ��ٶ�
	final int SPEED_Y  = 5; // y�����ϵ��ٶ� 
	final int WIDTH = 6; // �ӵ��Ŀ��
	final int HEIGHT = 7;// �ӵ��ĸ߶�
	final int ATK = 8; // ������ 
	final int FRAME_INDEX = 9; // ֡�±�
	Bitmap bitmapBoom;   //��ըͼƬ 
	int playerBullet[][];//�����ӵ����飬�� :�ӵ���   ��: �ӵ�����
	int enemyBullet[][];//�л��ӵ����� ���� �� �ӵ���  �У� �ӵ�����
	
	Bitmap eImg[]; //�л��ӵ�ͼƬ���� 
	Bitmap pImg[]; //����ӵ�ͼƬ���� 
	GameScreen gameScreen;
	Enemy enemy;
	int timeCount;
	Random random;
	int boomPosition[];//ը�������������� 
	
	public Bullet(GameScreen gameScreen,Enemy enemy,Property property) {
		// TODO Auto-generated constructor stub
		this.gameScreen = gameScreen;
		this.enemy = enemy;
		pImg = new Bitmap[3];
		boomPosition = new int[10];
		pImg[0] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.pbullet1);
		pImg[1] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.pbullet2);
		pImg[2] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.pbullet3);
		eImg = new Bitmap[4];
		eImg[0] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.bullet0);
		eImg[1] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.bullet1);
		eImg[2] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.bullet2);
		eImg[3] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.bullet3);
		bitmapBoom = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.pbomb);
		int w = bitmapBoom.getWidth()/4;
		int h = bitmapBoom.getHeight()/4;
		boomPosition[0] = GameView.SCREEN_WIDTH/4 - w/2;
		boomPosition[1] = GameView.SCREEN_HEIGHT/4 - h/2;
		boomPosition[2] = 3*GameView.SCREEN_WIDTH/4 + w/2;
		boomPosition[3] = GameView.SCREEN_HEIGHT/4 - h /2;
		boomPosition[4] = GameView.SCREEN_WIDTH/2 - w/2;
		boomPosition[5] = GameView.SCREEN_HEIGHT/2 - h/2;
		boomPosition[6] = GameView.SCREEN_WIDTH/4 - w/2;
		boomPosition[7] = 3*GameView.SCREEN_HEIGHT/4 + h/2;
		boomPosition[8] = 3*GameView.SCREEN_WIDTH/4 + w/2;
		boomPosition[9] = 3*GameView.SCREEN_HEIGHT/4 + h/2;

		//isvisiable��Ӧ�� ֵ0: ������   ֵ 1������
		playerBullet = new int[20][10];
		enemyBullet = new int[120][10];
		random = new Random();
		
	}
	
	/**
	 * ��ʼ������ 
	 */
	void init()
	{
		//player�ӵ��ĳ�ʼ��
		for (int i = 0; i < playerBullet.length; i++) {
				playerBullet[i][0] = UN_SHOW;
		}
		
		for (int i = 0; i < enemyBullet.length; i++) {
			enemyBullet[i][ISVISIABLE] = UN_SHOW;
		}
	}
	
	/**
	 * �����ӵ�����Ҫ��
	 * @param canvas
	 * @param paint
	 */
	void  paint(Canvas canvas,Paint paint)
	{
		//Player�ӵ��Ļ���
		for (int i = 0; i < playerBullet.length; i++) {
			switch(playerBullet[i][ISVISIABLE])
			{
				case SHOW:
					int type = playerBullet[i][TYPE];
					int x= playerBullet[i][X];
					int y= playerBullet[i][Y];
					int w= playerBullet[i][WIDTH];
					int h= playerBullet[i][HEIGHT];
					canvas.drawBitmap(pImg[type],
						new Rect(0, 0, pImg[type].getWidth(), pImg[type].getHeight()),
						new Rect(x, y, x+w, y+h),
					paint);
					break;
			}
		}
		 
		if(gameScreen.player.getState() == Player.SKILL)
		{
			if(playerBullet[0][FRAME_INDEX]<= 3)
			{
				for (int i = 0; i < 5; i++) {
					canvas.drawBitmap(bitmapBoom, 
							new Rect((bitmapBoom.getWidth()/4)*(playerBullet[0][FRAME_INDEX]),
									0, 
									(bitmapBoom.getWidth()/4)*(playerBullet[0][FRAME_INDEX]+1),
									bitmapBoom.getHeight()), 
							new Rect(playerBullet[i][X],playerBullet[i][Y],
									playerBullet[i][X]+playerBullet[i][WIDTH],
									playerBullet[i][Y]+playerBullet[i][HEIGHT]),
									paint);
				}
			}
			else if(playerBullet[0][FRAME_INDEX]%2 == 0)
				canvas.drawColor(Color.WHITE);
		
		}
		
		//Enemy �ӵ��Ļ���
		for (int i = 0; i < enemyBullet.length; i++) {
			if(enemyBullet[i][ISVISIABLE] == SHOW)
			{
				int type = enemyBullet[i][TYPE];
				canvas.drawBitmap(eImg[type], 
						new Rect(0, 0, enemyBullet[i][WIDTH], enemyBullet[i][HEIGHT]),
						new Rect(enemyBullet[i][X], enemyBullet[i][Y],
								enemyBullet[i][X]+enemyBullet[i][WIDTH],
								enemyBullet[i][Y]+enemyBullet[i][HEIGHT]),paint);
			}
		}
	}
	
	/**
	 * �ӵ��ƶ� 
	 */
	void  move()
	{
		//Player�ӵ��ƶ�
		for (int i = 0; i < playerBullet.length; i++) {
			if(playerBullet[i][ISVISIABLE] == SHOW)
			{
				playerBullet[i][X]+= playerBullet[i][SPEED_X];
				playerBullet[i][Y]+= playerBullet[i][SPEED_Y];
			}
		}
		
		//Enemy�ӵ��ƶ�
		for (int i = 0; i < enemyBullet.length; i++) {
			if(enemyBullet[i][ISVISIABLE] == SHOW)
			{
				enemyBullet[i][X] += enemyBullet[i][SPEED_X];
				enemyBullet[i][Y] += enemyBullet[i][SPEED_Y];
			}
		}
		
	}
	
	/**
	 * ��������ը������
	 */
	void createPlayerBoom()
	{
		for (int i = 0; i < playerBullet.length; i++) {
			playerBullet[i][ISVISIABLE] = UN_SHOW;		
		}
		for (int i = 0; i < enemyBullet.length; i++) {
			enemyBullet[i][ISVISIABLE] = UN_SHOW;
		}
		for (int i = 0; i < enemy.enemy.length; i++) {

			if(enemy.enemy[i][enemy.TYPE] != Enemy.TYPE_BOSS)
				enemy.enemy[i][enemy.ISVISABLE] = enemy.UN_SHOW;
			else
				enemy.enemy[i][enemy.HP] -= gameScreen.player.getAttack()*5;
		}
		for (int i = 0; i < boomPosition.length/2; i++) {
			playerBullet[i][ISVISIABLE] = Player.SKILL;
			playerBullet[i][X] = boomPosition[2*i];
			playerBullet[i][Y] = boomPosition[2*i+1];
			playerBullet[i][FRAME_INDEX] = 0;
			playerBullet[i][WIDTH] = bitmapBoom.getWidth()/4;
			playerBullet[i][HEIGHT] = bitmapBoom.getHeight();
			playerBullet[i][TYPE] = Player.SKILL;
		}
	}
	
	/**
	 * ����Player�ӵ�
	 */
	void creatPlayerBullet()
	{
		//Player�ӵ�����
		for (int i = 0; i < playerBullet.length; i++) {
			if(gameScreen.player.getState()!= Player.DEAD && playerBullet[i][ISVISIABLE] == UN_SHOW)
			{
				
				playerBullet[i][ISVISIABLE] = SHOW;
				playerBullet[i][TYPE] = gameScreen.player.getLevel() - 1;
				int type = gameScreen.player.getLevel() - 1;
				if(type == 0){
					playerBullet[i][ATK] = 2;//�ݶ� 
					playerBullet[i][WIDTH] = pImg[type].getWidth();
					playerBullet[i][HEIGHT] =  pImg[type].getHeight();	
				}
				else{ 
					playerBullet[i][WIDTH] = pImg[type].getWidth()/2;
					playerBullet[i][HEIGHT] =  pImg[type].getHeight()/2;
				}
				playerBullet[i][X] = gameScreen.player.getX()+(gameScreen.player.getWidth()/2-playerBullet[i][WIDTH]/2);
				playerBullet[i][Y] = gameScreen.player.getY() - playerBullet[i][HEIGHT]+5;
				playerBullet[i][SPEED_X] = 0;
				playerBullet[i][SPEED_Y] = -18;
				break;
			}
		}
		
	}
	
	/**
	 * ����Enemy�ӵ�
	 * @param enemyType  �л�����
	 */
	void creatEnemyBullet(int enemyType)
	{
		
		switch (enemyType) {
		case Enemy.TYPE_YELLOW:
			for (int i = 0; i < enemy.enemy.length; i++) {
				if(enemy.enemy[i][enemy.ISVISABLE] == enemy.SHOW && enemy.enemy[i][enemy.TYPE] == Enemy.TYPE_YELLOW)
				{
					for (int j = 0; j < enemyBullet.length; j++) {
						if(enemyBullet[j][ISVISIABLE] == UN_SHOW)
						{
							enemyBullet[j][ISVISIABLE] = SHOW;
							enemyBullet[j][ATK] = 4;//
							enemyBullet[j][FRAME_INDEX] = 0;
							enemyBullet[j][WIDTH] = eImg[enemyType].getWidth();
							enemyBullet[j][HEIGHT] = eImg[enemyType].getHeight();
							enemyBullet[j][TYPE] = Enemy.TYPE_YELLOW;
							enemyBullet[j][X] = enemy.enemy[i][enemy.X]+enemy.enemy[i][enemy.WIDTH]/2-enemyBullet[j][WIDTH]/2;
							enemyBullet[j][Y] = enemy.enemy[i][enemy.Y]+enemy.enemy[i][enemy.HEIGHT];
							enemyBullet[j][SPEED_X] = (enemyBullet[j][X] > gameScreen.player.getX()? -5 : 5);
							enemyBullet[j][SPEED_Y] = (enemyBullet[j][Y] > gameScreen.player.getY()? -10 : 10);
							
							break;
						}
					}
				}
			}
			break;
		case Enemy.TYPE_RED:
			for (int i = 0; i < enemy.enemy.length; i++) {
				if(enemy.enemy[i][enemy.ISVISABLE] == enemy.SHOW && enemy.enemy[i][enemy.TYPE] ==  Enemy.TYPE_RED)
				{
					
					for (int j = 0; j < enemyBullet.length; j++) {
						if(enemyBullet[j][ISVISIABLE] == UN_SHOW)
						{
							enemyBullet[j][ISVISIABLE] = SHOW;
							enemyBullet[j][ATK] = 2;//
							enemyBullet[j][FRAME_INDEX] = 0;
							enemyBullet[j][WIDTH] = eImg[enemyType].getWidth();
							enemyBullet[j][HEIGHT] = eImg[enemyType].getHeight();
							enemyBullet[j][X] = enemy.enemy[i][enemy.X]+enemy.enemy[i][enemy.WIDTH]/2-enemyBullet[j][WIDTH]/2;
							enemyBullet[j][Y] = enemy.enemy[i][enemy.Y]+enemy.enemy[i][enemy.HEIGHT];
							enemyBullet[j][SPEED_X] = 0;
							enemyBullet[j][SPEED_Y] = 18;
							enemyBullet[j][TYPE] = Enemy.TYPE_RED;
							
							break;
						}
					}
				}
			}
			break;
		case Enemy.TYPE_GREEN:
			for (int i = 0; i < enemy.enemy.length; i++) {
				if(enemy.enemy[i][enemy.ISVISABLE] == enemy.SHOW && enemy.enemy[i][enemy.TYPE] == Enemy.TYPE_GREEN)
				{
					int count = 0;
					for (int j = 0; j < enemyBullet.length; j++) {
						if (enemyBullet[j][ISVISIABLE] == UN_SHOW) {
							enemyBullet[j][ISVISIABLE] = SHOW;
							enemyBullet[j][ATK] = 3;//
							enemyBullet[j][FRAME_INDEX] = 0;
							enemyBullet[j][WIDTH] = eImg[enemyType].getWidth();
							enemyBullet[j][HEIGHT] = eImg[enemyType].getHeight();
							enemyBullet[j][X] = enemy.enemy[i][enemy.X]+2+count*20;
							enemyBullet[j][Y] = enemy.enemy[i][enemy.Y]+enemy.enemy[i][enemy.HEIGHT];
							enemyBullet[j][SPEED_X] = 0;
							enemyBullet[j][SPEED_Y] = 20;
							enemyBullet[j][TYPE] = Enemy.TYPE_GREEN;
							count++;
							if(count ==2)
								break;
						}
					}
				}
			}
			break;
		case Enemy.TYPE_BULE:
			for (int i = 0; i < enemy.enemy.length; i++) {
				if(enemy.enemy[i][enemy.ISVISABLE] == enemy.SHOW && enemy.enemy[i][enemy.TYPE] == Enemy.TYPE_BULE)
				{
					int count = 0;
					for (int j = 0; j < enemyBullet.length; j++) {
						if(enemyBullet[j][ISVISIABLE] == UN_SHOW)
						{
							enemyBullet[j][ISVISIABLE] = SHOW;
							enemyBullet[j][ATK] = 5;
							enemyBullet[j][FRAME_INDEX] = 0;
							enemyBullet[j][WIDTH] = eImg[enemyType].getWidth();
							enemyBullet[j][HEIGHT] = eImg[enemyType].getHeight();
							enemyBullet[j][X] = enemy.enemy[i][enemy.X]+3+count*9;
							enemyBullet[j][Y] = enemy.enemy[i][enemy.Y]+enemy.enemy[i][enemy.HEIGHT];
							enemyBullet[j][SPEED_X] = 8*count-8;
							enemyBullet[j][SPEED_Y] = 16;
							enemyBullet[j][TYPE] = Enemy.TYPE_BULE;
							count++;
							if(count == 3)
								break;
						}
					}
				}
			}
			break;
		case Enemy.TYPE_BOSS:
			int ran = Math.abs(random.nextInt()%8);
			if(enemy.enemy[0][enemy.ISVISABLE] == enemy.SHOW && enemy.enemy[0][enemy.TYPE] == Enemy.TYPE_BOSS)
			{
				if(enemy.enemy[0][enemy.Y] >= 20 )
				if(ran <= 4)
				{
					int count = -5;
					for (int j = 0; j < enemyBullet.length; j++) {
						if(enemyBullet[j][ISVISIABLE] == UN_SHOW)
						{
							enemyBullet[j][ISVISIABLE] = SHOW;
							enemyBullet[j][ATK] = 15;
							enemyBullet[j][FRAME_INDEX] = 0;
							enemyBullet[j][WIDTH] = eImg[Enemy.TYPE_RED].getWidth();
							enemyBullet[j][HEIGHT] = eImg[Enemy.TYPE_RED].getHeight();
							enemyBullet[j][X] = enemy.enemy[0][enemy.X]+15*(count+4)+7;
							int y = -1*count*count-2*count+3;
							enemyBullet[j][Y] = enemy.enemy[0][enemy.Y]+90+y;
							enemyBullet[j][SPEED_X] = (count+1 == 0? 0:(count+1)+(count+1)/Math.abs(count+1)*5);
							enemyBullet[j][SPEED_Y] = 12;
							enemyBullet[j][TYPE] = Enemy.TYPE_RED;
							count++;
							if(count == 4)
								break;
						}
							
					}
				}else 		
				{
					int count = 0;
					for (int j = 0; j < enemyBullet.length; j++) {
						if(enemyBullet[j][ISVISIABLE] == UN_SHOW)
						{
							enemyBullet[j][ISVISIABLE] = SHOW;
							enemyBullet[j][ATK] = 25;
							enemyBullet[j][FRAME_INDEX] = 0;
							enemyBullet[j][WIDTH] = eImg[Enemy.TYPE_GREEN].getWidth();
							enemyBullet[j][HEIGHT] = eImg[Enemy.TYPE_GREEN].getHeight();
							enemyBullet[j][X] = enemy.enemy[0][enemy.X]+40+count*24;
							enemyBullet[j][Y] = enemy.enemy[0][enemy.Y]+85;
							enemyBullet[j][SPEED_X] = 0;
							enemyBullet[j][SPEED_Y] = 14;
							enemyBullet[j][TYPE] = Enemy.TYPE_GREEN;
							count++;
							if(count == 2)
								break;
						}
							
					}
				}
				
			}
			break;
		}
	}
	
	
	/**
	 * �ӵ��س�
	 */
	void setVisiable()
	{
		//Player �ӵ��س�
		for (int i = 0; i < playerBullet.length; i++) {
			if(playerBullet[i][ISVISIABLE] == SHOW && playerBullet[i][Y] <= -playerBullet[i][HEIGHT] )
				playerBullet[i][ISVISIABLE] = UN_SHOW; 
		}
		
		//Enemy�ӵ��س�
		for (int i = 0; i < enemyBullet.length; i++) {
			if(enemyBullet[i][ISVISIABLE] == SHOW
					&&(enemyBullet[i][Y] <= -enemyBullet[i][HEIGHT] ||enemyBullet[i][Y] >= GameView.SCREEN_HEIGHT || enemyBullet[i][X] <= -enemyBullet[i][WIDTH] || enemyBullet[i][X] >= GameView.SCREEN_WIDTH ))
				enemyBullet[i][ISVISIABLE] = UN_SHOW; 
		}
	}
	
	/**
	 * ��ײ����
	 */
	void collidesWith()
	{
		//�л��ӵ���ײ���
		for (int i = 0; i < enemyBullet.length; i++) {
			if(enemyBullet[i][ISVISIABLE] == SHOW)
			{
				switch (gameScreen.player.getState()) {
				case Player.NORMAL:
				case Player.LEVELUP:
					if(Collision.collides(enemyBullet[i][X], enemyBullet[i][Y], enemyBullet[i][WIDTH], enemyBullet[i][HEIGHT], gameScreen.player.getX(), gameScreen.player.getY(), gameScreen.player.getWidth(),  gameScreen.player.getHeight()))
					{
						enemyBullet[i][ISVISIABLE] = UN_SHOW;
						gameScreen.player.setHp(enemyBullet[i][ATK]);
					}
					break;

				default:
					break;
				}
				
					
			}
		}
		
		//Player���ӵ���ײ��� 
		for (int i = 0; i < playerBullet.length; i++) {
			if(playerBullet[i][ISVISIABLE] == SHOW)
			{
				for (int j = 0; j < enemy.enemy.length; j++) {
					if(enemy.enemy[j][enemy.ISVISABLE] == enemy.SHOW)
					{
						if(Collision.collides(enemy.enemy[j][enemy.X], enemy.enemy[j][enemy.Y], enemy.enemy[j][enemy.WIDTH], enemy.enemy[j][enemy.HEIGHT], playerBullet[i][X], playerBullet[i][Y], playerBullet[i][WIDTH], playerBullet[i][HEIGHT]))
						{
							playerBullet[i][ISVISIABLE] = UN_SHOW;
							enemy.enemy[j][enemy.HP] -= gameScreen.player.getAttack();
							if(enemy.enemy[j][enemy.HP] <= 0)
							{
								if(enemy.enemy[j][enemy.TYPE] == Enemy.TYPE_BOSS)
									gameScreen.state = gameScreen.WIN;
								enemy.enemy[j][enemy.ISVISABLE] = enemy.BOOM;
								enemyBullet[j][FRAME_INDEX] = 0;
								
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * �����ӵ��߼�
	 */
	void playerBulletLogic()
	{
		for (int i = 0; i < playerBullet.length; i++) {
			if(playerBullet[i][ISVISIABLE] == Player.SKILL)
			{
				playerBullet[i][FRAME_INDEX]++;
				if(playerBullet[i][FRAME_INDEX] == 8)
				{
					playerBullet[i][ISVISIABLE] = UN_SHOW;
					gameScreen.player.setState(Player.NORMAL);
					break;
				}
				
			}
		}
	}
	
	/**
	 * �л��ӵ��߼�
	 */
	void enemyBulletLogic()
	{
		for (int i = 0; i < enemyBullet.length; i++) {
			if(enemyBullet[i][ISVISIABLE] == SHOW)
			{
				switch (enemyBullet[i][TYPE]) {
				case Enemy.TYPE_YELLOW:
					enemyBullet[i][FRAME_INDEX] = (enemyBullet[i][FRAME_INDEX] == 1? 0:1);
					enemyBullet[i][SPEED_X] = (enemyBullet[i][X]>gameScreen.player.getX()? -5 : 5);
					enemyBullet[i][SPEED_Y] = (enemyBullet[i][Y]>gameScreen.player.getY()? -10:10);
					break;
				case Enemy.TYPE_GREEN:
				case Enemy.TYPE_BULE:
				case Enemy.TYPE_RED:
					enemyBullet[i][FRAME_INDEX] = (enemyBullet[i][FRAME_INDEX] == 1? 0:1);
					break;
				}
			
			}
		}
	}
	
	/**
	 * �ӵ����߼�������Ҫ��
	 */
    void logic()
    {
    	timeCount++;
    	if(timeCount%5 == 0)
    		creatPlayerBullet();
    	if (timeCount%15 == 0) {
			creatEnemyBullet(Enemy.TYPE_YELLOW);
		}
    	if(timeCount%6 ==0)
    		creatEnemyBullet(Enemy.TYPE_RED);
    	if (timeCount%7 == 0) {
			creatEnemyBullet(Enemy.TYPE_GREEN);
		}
    	if(timeCount%9 == 0)
    		creatEnemyBullet(Enemy.TYPE_BULE);
    	if(timeCount%4 == 0)
    		creatEnemyBullet(Enemy.TYPE_BOSS);
    	enemyBulletLogic();
    	playerBulletLogic();
    	move();
    	setVisiable();
    	collidesWith();
    }
	
}
