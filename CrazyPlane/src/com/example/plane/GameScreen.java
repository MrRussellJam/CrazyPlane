package com.example.plane;

import android.graphics.*;
import android.view.KeyEvent;

public class GameScreen {

	protected final static int GAME = 0;
	protected final static int WIN = 1;
	protected final static int LOSE = 2;
	protected final static int PAUSE = 5;
	
	
	GameView gameView;
	Bitmap map;			//地图
	Bitmap hp_bg;
	Bitmap hp;
	Bitmap life;
	Bitmap skill;
	Bitmap play_pause;
	protected int state;
	int mapY; 			//图片起始
	int timecount;
	Rect pause;
	Bitmap sec_menu;
	Bitmap font[];
	Bitmap direction[];
	Bitmap skillbtn;
	Bitmap win;
	Bitmap lose;
	
	Rect fontR[][];
	Rect directR[];
	Rect skillBtn;
	Rect stateFont;
	
	Canvas canvas;
	Paint paint;
	
	Player player;
	Enemy enemy;
	Bullet bullet;
	Property property;
	
	/*
	 * 构造方法
	 */
	public GameScreen(GameView view){
		this.gameView = view;
		map = BitmapFactory.decodeResource(view.getResources(), R.drawable.mapy);
		mapY = map.getHeight() - GameView.SCREEN_HEIGHT;
//		score = BitmapFactory.decodeResource(view.getResources(), R.drawable.score);
//		menu = BitmapFactory.decodeResource(view.getResources(), R.drawable.botton_bar);
		hp_bg = BitmapFactory.decodeResource(view.getResources(), R.drawable.hp_p_bg);
		hp = BitmapFactory.decodeResource(view.getResources(), R.drawable.hp_p);
		life = BitmapFactory.decodeResource(view.getResources(), R.drawable.life);
		skill = BitmapFactory.decodeResource(view.getResources(), R.drawable.skill);
		state = GAME;
		play_pause = BitmapFactory.decodeResource(view.getResources(), R.drawable.play);
		pause = new Rect(0,GameView.SCREEN_HEIGHT - 200, 200, GameView.SCREEN_HEIGHT);
		sec_menu = BitmapFactory.decodeResource(view.getResources(), R.drawable.sec_menu);
		font = new Bitmap[2];
		fontR = new Rect[2][2];
		font[0] = BitmapFactory.decodeResource(view.getResources(), R.drawable.menu1);
		font[1] = BitmapFactory.decodeResource(view.getResources(), R.drawable.menu2);

		fontR[0][0] = new Rect(0, (int)(font[0].getHeight()/5), font[0].getWidth(),  (int)(font[0].getHeight()/5)*2);
		fontR[0][1] = new Rect(340, 500, 340 + 400, 500 + 156);
		
		fontR[1][0] = new Rect(0, (int)(font[0].getHeight()/5)*4, font[0].getWidth(),  font[0].getHeight());
		fontR[1][1] = new Rect(340, 1000, 340 + 400, 1000 + 156); 
		
		direction = new Bitmap[4];
		directR = new Rect[4];

		direction[0] = BitmapFactory.decodeResource(view.getResources(), R.drawable.direction0);
		direction[1] = BitmapFactory.decodeResource(view.getResources(), R.drawable.direction1);
		direction[2] = BitmapFactory.decodeResource(view.getResources(), R.drawable.direction2);
		direction[3] = BitmapFactory.decodeResource(view.getResources(), R.drawable.direction3);

		directR[0] = new Rect(100, GameView.SCREEN_HEIGHT - 500, 300, GameView.SCREEN_HEIGHT - 400);
		directR[1] = new Rect(100, GameView.SCREEN_HEIGHT - 300, 300, GameView.SCREEN_HEIGHT - 200);
		directR[2] = new Rect(0, GameView.SCREEN_HEIGHT - 400, 200, GameView.SCREEN_HEIGHT - 300);
		directR[3] = new Rect(200, GameView.SCREEN_HEIGHT - 400, 400, GameView.SCREEN_HEIGHT - 300);		
		
		skillbtn = BitmapFactory.decodeResource(view.getResources(), R.drawable.skillbtn);
		skillBtn = new Rect(GameView.SCREEN_WIDTH - 250, GameView.SCREEN_HEIGHT - 400, GameView.SCREEN_WIDTH - 100, GameView.SCREEN_HEIGHT - 250);
		
		win = BitmapFactory.decodeResource(view.getResources(), R.drawable.win);
		lose = BitmapFactory.decodeResource(view.getResources(), R.drawable.lose);
		
		stateFont = new Rect(0, 400, this.gameView.SCREEN_WIDTH, 800);
		
		timecount = 0;
		
		
		player = new Player(this);
		property = new Property(this);
		enemy = new Enemy(this);
		bullet = new Bullet(this, enemy, property);
	}
	
	/*
	 * 初始化
	 */
	void init(){
		state = 0;
		mapY = GameView.SCREEN_HEIGHT-map.getHeight();
		enemy.un_Show();
		player.init();
		bullet.init();
		property.init();
		timecount = 0;
		gameView.timeCount = 0;
	}
	
	//画游戏地图
	void drawMap(Canvas canvas, Paint paint){
		if(mapY<0){
			canvas.drawBitmap(map, 
					new Rect(0,map.getHeight() + mapY, map.getWidth(), this.map.getHeight()),				//上半部分的图片矩阵
					new Rect(0, 0, GameView.SCREEN_WIDTH, -mapY),	//上半部分的屏幕矩阵
					paint);
			
			canvas.drawBitmap(map, 
						new Rect(0, 0, map.getWidth(), GameView.SCREEN_HEIGHT + mapY),	//上半部分的图片矩阵
						new Rect(0, -mapY, GameView.SCREEN_WIDTH, GameView.SCREEN_HEIGHT),							//上半部分在屏幕中的矩阵
						paint);
			
		}
		else
			canvas.drawBitmap(map, 
					new Rect(0, mapY, map.getWidth(), mapY + GameView.SCREEN_HEIGHT), 
					new Rect(0, 0, GameView.SCREEN_WIDTH, GameView.SCREEN_HEIGHT), paint);
	}
	
	//绘制状态栏、菜单、保存
	void drawState(Canvas canvas, Paint paint){
		float act_hp = (float)player.getHp()/(float)Player.MAX_HP;
		canvas.drawBitmap(hp_bg, 
				new Rect(0, 0, hp_bg.getWidth(), hp_bg.getHeight()), 
				new Rect(0, 0, 385*2, 61*2),
				paint);
		
		canvas.drawBitmap(hp, 
				new Rect(0, 0, hp.getWidth(), hp.getHeight()), 
				new Rect(55*2, 20*2, (int)((380*2)*act_hp), 45*2), paint);

		for(int i = 0; i < player.getLife(); i++)
			canvas.drawBitmap(life, 
					new Rect(0, 0, life.getWidth(), life.getHeight()), 
					new Rect(50 + i*75, 125, 50 + (i+1)*75, 125 + 75),
					paint);
		for(int i = 0; i < player.getSkill(); i++)
			canvas.drawBitmap(skill, 
					new Rect(0, 0, skill.getWidth(), skill.getHeight()), 
					new Rect(50 + i*75, 200, 50 + (i+1)*75, 200 + 75),
					paint);
		
		canvas.drawBitmap(play_pause, 
				new Rect(0, 0, play_pause.getWidth(), play_pause.getHeight()/2), 
				pause, paint);
		
		canvas.drawBitmap(skillbtn, new Rect(0, 0, skillbtn.getWidth(), skillbtn.getHeight()), skillBtn, paint);
		}
	/*
	 * 绘制方向
	 */
	public void drawDirection() {
		for(int i = 0; i < 4; i++){
			canvas.drawBitmap(direction[i], new Rect(0, 0, direction[i].getWidth(), direction[i].getHeight()),
					directR[i], paint);
		}
	}
	
	/**
	 * 游戏状态下键盘输入 
	 * @param keyCode 键值
	 */
	void onKeyDown(int keyCode)
	{
		switch (state) {
		case GAME:
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_UP:
					player.changeDirection(Player.UP);
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					player.changeDirection(Player.DOWN);
					break;
				case KeyEvent.KEYCODE_DPAD_LEFT:
					player.changeDirection(Player.LEFT);
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					player.changeDirection(Player.RIGHT);
					break;
				case KeyEvent.KEYCODE_SPACE:
					player.setState(Player.SKILL);
					break;
				case KeyEvent.KEYCODE_C:
					player.stop();
					break;
				}
			break;
			
		case LOSE:
		case WIN:
			gameView.gameState = GameView.MAIN_MENU;
			break;
		}
		
	}
	
	/**
	 * 按键按下处理
	 * @param keyCode 键值
	 */
	void onKeyUp(int keyCode)
	{
		switch (state) {
		case GAME:
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				//player.stop();
				break;
			}
			
			break;

		default:
			break;
		}
	}
	/*
	 * 触摸事件
	 */
	void onTouchEvent(int x, int y){
		if(state == GAME)
		{
			if(pause.contains(x, y)){	
				this.state = PAUSE;
			}
			if(directR[0].contains(x, y))
				player.changeDirection(Player.UP);
			else if(directR[1].contains(x, y))
				player.changeDirection(Player.DOWN);
			else if(directR[2].contains(x, y))
				player.changeDirection(Player.LEFT);
			else if(directR[3].contains(x, y))
				player.changeDirection(Player.RIGHT);
			else if(skillBtn.contains(x, y))
				player.setState(player.SKILL);
		}
		else if(state == PAUSE){
			if(fontR[0][1].contains(x, y)){
				canvas.drawBitmap(font[1], fontR[0][0], fontR[0][1], paint);
				this.state = GAME;
			}
			else if(fontR[1][1].contains(x, y)){
				canvas.drawBitmap(font[1], fontR[1][0], fontR[1][0], paint);
				this.gameView.activity.finish();
			}
		}
		
	}
	
	
	/*
	 * 绘制暂停菜单
	 * 
	 */
	void drawSecMenu(Canvas canvas, Paint paint){
		canvas.drawBitmap(sec_menu, 
				new Rect(0, 0, sec_menu.getWidth(), sec_menu.getHeight()),
				new Rect(0, 0, GameView.SCREEN_WIDTH, GameView.SCREEN_HEIGHT), 
				paint);
		canvas.drawBitmap(font[1], fontR[0][0], fontR[0][1], paint);

		canvas.drawBitmap(font[0], fontR[1][0], fontR[1][1], paint);
	}
	
	void paint(Canvas canvas, Paint paint){
		this.canvas = canvas;
		this.paint = paint;
		drawMap(canvas, paint);
		player.paint(canvas, paint);
		enemy.paint(canvas, paint);
		bullet.paint(canvas, paint);
		property.paint(canvas, paint);
		drawDirection();
		drawState(canvas, paint);
		switch (state) {
		case GAME:
			break;
		case WIN:
			canvas.drawBitmap(win, new Rect(0, 0, win.getWidth(), win.getHeight()), stateFont, paint);
			break;
		case LOSE:
			canvas.drawBitmap(lose, new Rect(0, 0, lose.getWidth(), lose.getHeight()), stateFont, paint);
			break;
		case PAUSE:
			drawSecMenu(canvas, paint);
			break;
		}
	}
	
	/*
	 * 游戏逻辑方法
	 */
	void logic(){
		switch (state) {
		case GAME:
			mapY -= 20;
			if(-mapY > GameView.SCREEN_HEIGHT)
				mapY = map.getHeight() - GameView.SCREEN_HEIGHT;
			player.logic();
			enemy.logic();
			bullet.logic();
			property.logic();
			break;
		case WIN:
		case LOSE:
			enemy.logic();
			if(timecount > 6)
				this.gameView.gameState = GameView.MAIN_MENU;
			timecount++;
		}
	}
	
}
