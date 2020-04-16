package com.example.plane;

import android.graphics.*;

import java.util.Random;

public class Property {

	static final int BOOM = 0;
	static final int POWER = 1;
	static final int LEVER_UP = 2;
	final int UN_SHOW = 0;
	final int SHOW = 1;
	final int ISVISIABLE = 0;
	final int X = 1;
	final int Y = 2;
	final int WIDTH = 3;
	final int HEIGHT = 4;
	final int TYPE = 5;
	final int FRAME = 6;
	final int SPEED_X = 7;
	final int SPEED_Y = 8;
	
	Random random;
	GameScreen gameScreen;
	Bitmap image[];
	int property[][];//道具对象池
	
	public Property(GameScreen gameScreen) {
		// TODO Auto-generated constructor stub
		this.gameScreen = gameScreen;
		property = new int[4][9];
		random = new Random();
		image = new Bitmap[3];
		image[0] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.boom);
		image[1] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.receive);
		image[2] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.level);
		
		
	}
	
	/**
	 *初始化方法 
	 */
	void init()
	{
		for (int i = 0; i < property.length; i++) {
			property[i][ISVISIABLE] = UN_SHOW;
		}
	}
	
	
	/**
	 * 道具创建方法
	 * @param type  道具类型
	 * @param x   道具的初始化x坐标 
	 * @param y   道具的初始化y坐标 
	 */
	void creatProperty(int type,int x,int y)
	{
		switch (type) {
		case BOOM:
			for (int i = 0; i < property.length; i++) {
				if(property[i][ISVISIABLE] == UN_SHOW)
				{
					property[i][ISVISIABLE] = SHOW;
					property[i][X] = x;
					property[i][Y] = y;
					property[i][SPEED_X] = random.nextInt()%10;
					property[i][SPEED_Y] = random.nextInt()%10;
					property[i][WIDTH] = image[0].getWidth()/2;
					property[i][HEIGHT] = image[0].getHeight()/2;
					property[i][TYPE] = type;
					property[i][FRAME] = 0;
					break;
				}
				
			}
			break;

		case POWER:
			for (int i = 0; i < property.length; i++) {
				if(property[i][ISVISIABLE] == UN_SHOW)
				{
					property[i][ISVISIABLE] = SHOW;
					property[i][X] = x;
					property[i][Y] =  y;
					property[i][SPEED_X] = (random.nextInt()>=0?8:-8);
					property[i][SPEED_Y] = (random.nextInt()>=0?10:-10);
					property[i][WIDTH] = image[1].getWidth()/2;
					property[i][HEIGHT] = image[1].getHeight()/2;
					property[i][TYPE] = type;
					property[i][FRAME] = 0;
					break;
				}
			}
			break;
		case LEVER_UP:
			for (int i = 0; i < property.length; i++) {
				if(property[i][ISVISIABLE] == UN_SHOW)
				{
					property[i][ISVISIABLE] = SHOW ;
					property[i][X] = x;
					property[i][Y] = y;
					property[i][SPEED_X]= random.nextInt()%9;
					property[i][SPEED_Y]= random.nextInt()%9;
					property[i][WIDTH]= image[2].getWidth()/2;
					property[i][HEIGHT]= image[2].getHeight()/2;
					property[i][TYPE]= type;
					property[i][FRAME]= 0;
					break;
				}
			}
			break;
		}
	}
	
	/**
	 * 道具绘制
	 * @param canvas
	 * @param paint
	 */
	void paint(Canvas canvas,Paint paint)
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW){
				int type = property[i][TYPE];
				int x = property[i][X];
				int y = property[i][Y];
				int w = property[i][WIDTH];
				int h = property[i][HEIGHT];
				canvas.drawBitmap(image[type], 
						new Rect(0, 0, image[type].getWidth(), image[type].getHeight()), 
						new Rect(x, y, x + w, y + h), paint);
				System.out.println(new Rect(0, 0, image[type].getWidth(), image[type].getHeight()));
				System.out.println(new Rect(x, y, x + w, y + h));
			}		
		}
	}
	
	/**
	 * 道具移动 
	 */
	void move()
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW)
			{
				property[i][X] += property[i][SPEED_X];
				property[i][Y] += property[i][SPEED_Y];
				System.out.println("property" + property[i][X] + "   " + property[i][Y]);
			}
		}
	}
	
	/**
	 * 道具回池方法
	 */
	void  setVisiable()
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW)
			{
				if(property[i][X] <= -property[i][WIDTH] || property[i][X]>= GameView.SCREEN_WIDTH || property[i][Y]<= -property[i][HEIGHT] || property[i][Y]>= GameView.SCREEN_HEIGHT )
					property[i][ISVISIABLE] = UN_SHOW;
			}
		}
	}
	
	/**
	 * 道具碰撞检测
	 * @param p  飞机对象
	 */
	void collodesWith(Player p)
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW && p.getState() == Player.NORMAL)
			{
				if(Collision.collides(property[i][X], property[i][Y], property[i][WIDTH], property[i][HEIGHT], p.getX(), p.getY(), p.getWidth(), p.getHeight()))
				{
					System.out.println("Collision   property " + property[i][X] + "   " + property[i][Y]);
					switch (property[i][TYPE]) {
					case BOOM:
						gameScreen.player.addSkill();
						property[i][ISVISIABLE] =UN_SHOW;
						break;
					case POWER:
						gameScreen.player.setState(Player.RECOVER);
						property[i][ISVISIABLE] =UN_SHOW;
						break;
					case LEVER_UP:
						gameScreen.player.setState(Player.LEVELUP);
						property[i][ISVISIABLE] =UN_SHOW;
						break;
					}
				}
					
						
			}
		}
	}
	
	/**
	 * 修改路径
	 */
	void setPath()
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW )
			{
				switch (property[i][TYPE]) {
				case BOOM:
					property[i][SPEED_X] = random.nextInt()%10;
					property[i][SPEED_Y] = random.nextInt()%10;
					break;

				case LEVER_UP:
				case POWER:
					break;
				}
			}
		}
	}
	
	void logic()
	{
		move();
		setVisiable();
		collodesWith(gameScreen.player);
		if(gameScreen.gameView.timeCount%15 == 0)
			setPath();
	}
	
}
