package com.example.plane;

import android.content.Context;
import android.graphics.*;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable,Callback {

	public final static int SPLASH = 0;			//开始界面
	public final static int MAIN_MENU = 1;		//主菜单
	public final static int GAME = 2;			//游戏界面
	public final static int RECORD = 3;			//保存
	public final static int HELP = 4;			//帮助
	public final static int EXIT = 5;			//退出
	public final static int OPTION = 6;			//菜单界面

	protected static int SCREEN_WIDTH;
	protected static int SCREEN_HEIGHT;
	
	MenuScreen menuScreen;
	GameScreen gameScreen;
	OptionScreen optionScreen;

	public MainActivity activity;
	private Canvas canvas;
	private Paint paint;
	SurfaceHolder holder;
	Bitmap[] spImg;

	protected int gameState;							//游戏状态
	boolean isRun;
	protected int timeCount;
	Rect src0;			//加载背景的图片矩阵
	Rect src1;			//加载文字的图片矩阵

	Rect dst0;			//加载背景的屏幕矩阵
	Rect dst1;			//加载文字的屏幕矩阵
	
	
	public GameView(Context context) {
		super(context);
		setFocusable(true);
		gameState = SPLASH;
		isRun = false;
		timeCount = 0;
		holder = this.getHolder();
		holder.addCallback(this);
		canvas = holder.lockCanvas();
		paint = new Paint();
		paint.setAntiAlias(true);
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		SCREEN_WIDTH = this.getWidth();
		SCREEN_HEIGHT = this.getHeight();
		this.menuScreen = new MenuScreen(this);
		this.gameScreen = new GameScreen(this);
		spImg = new Bitmap[2];
		spImg[0] = BitmapFactory.decodeResource(getResources(), R.drawable.splash1);
		spImg[1] = BitmapFactory.decodeResource(getResources(), R.drawable.splashfont);
		
		src0 = new Rect(0,0,spImg[0].getWidth(),spImg[0].getHeight());
		src1 = new Rect(0,0,spImg[1].getWidth(),spImg[1].getHeight());

		
		dst0 = new Rect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
		dst1 = new Rect(0,SCREEN_HEIGHT-300,SCREEN_WIDTH,SCREEN_HEIGHT);
		isRun = true;
		new Thread(this).start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isRun = false;
		this.activity.finish();
	}
	
	public void drawSplash(){
		canvas.drawBitmap(spImg[0], src0, dst0, paint);
		if(timeCount%2==0){
			canvas.drawBitmap(spImg[1], src1, dst1, paint);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (gameState) {
		case SPLASH:
			gameState = MAIN_MENU;
			break;
		case MAIN_MENU:
//		case MUSIC_SET:
		case HELP:
		case RECORD:
			menuScreen.onKeyDown(keyCode);
			break;
		case GAME:
			gameScreen.onKeyDown(keyCode);
			break;

		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (gameState) {

		case GAME:
			gameScreen.onKeyUp(keyCode);
			break;
//		case MUSIC_SET:
//
//			break;
		case HELP:

			break;
		case RECORD:

			break;

		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch(gameState){
		case GAME:
			this.gameScreen.onTouchEvent(x, y);
			break;
		case MAIN_MENU:
		case HELP:
		case RECORD:
			this.menuScreen.onTouchEvent(x, y);
			break;
		case SPLASH:
			this.gameState = MAIN_MENU;
			timeCount = 0;
			break;
		}
		return super.onTouchEvent(event);
	}
	
	//已完成
	void logic(){
		switch(gameState){
		case SPLASH:
			timeCount++;
			if(timeCount==25){
				this.gameState = MAIN_MENU;
				timeCount = 0;
			}
			break;
		case GAME:
			timeCount++;
			this.gameScreen.logic();
			break;
		case RECORD:
			
			break;
		}
	}
	
	//已完成
	void paint(){
		canvas = holder.lockCanvas();
		switch(gameState){
		case SPLASH:
			this.drawSplash();
			break;
		case GAME:
			this.gameScreen.paint(canvas, paint);
			break;
		case MAIN_MENU:
		case HELP:
		case RECORD:
			this.menuScreen.paint(canvas,paint,this.gameState);
			break;
		}
		holder.unlockCanvasAndPost(canvas);
	}

	//已完成
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRun){
			logic();
			paint();
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
