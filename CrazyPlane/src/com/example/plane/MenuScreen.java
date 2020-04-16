package com.example.plane;

import android.graphics.*;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MenuScreen {
	Bitmap bg;
	Bitmap option[];
	Rect bgRectsrc;
	Rect bgRectdst;
	Rect opRect[][];			//5,2   一个表示图片的矩阵， 一个表示画在屏幕上的矩阵 
	GameView view;
	int selectItem;
	Bitmap help;
	
	
	//初始化构造方法
	public MenuScreen(GameView view){
		this.view = view;
		bg = BitmapFactory.decodeResource(view.getResources(), R.drawable.menu);
		help = BitmapFactory.decodeResource(view.getResources(), R.drawable.help);
		option = new Bitmap[2];
		option[0] = BitmapFactory.decodeResource(view.getResources(), R.drawable.menu1);
		option[1] = BitmapFactory.decodeResource(view.getResources(), R.drawable.menu2);
		bgRectsrc = new Rect(0,0,bg.getWidth(),bg.getHeight());
		bgRectdst = new Rect(0,0,GameView.SCREEN_WIDTH,GameView.SCREEN_HEIGHT);
		opRect = new Rect[5][2];
		selectItem = 0;
		for(int i = 0; i < 5; i++){
			opRect[i][0] = new Rect( 0, (option[0].getHeight()/5)*i ,option[0].getWidth(), (option[0].getHeight()/5)*(i+1) );
			int x = 340;			//左侧空出的距离
			int y = 300;			//上方空出的距离
			opRect[i][1] = new Rect( x, y*i + 200 ,								//200为第一个选项离顶部的距离
					x + 400, y*i + 200 + 156);	
			System.out.println(opRect[i][0] + "    " + opRect[i][1]);
			System.out.println(x + "   " + y);
		}
	}
	
	//绘制主菜单
	void drawMainMenu(Canvas canvas,Paint paint){
		canvas.drawBitmap(bg, bgRectsrc, bgRectdst, paint);
		for(int i = 0; i < 5; i++){
			if(i != this.selectItem)
				canvas.drawBitmap(option[0], opRect[i][0], opRect[i][1], paint);
			else
				canvas.drawBitmap(option[1], opRect[i][0], opRect[i][1], paint);
		}
	}
	
	//绘制帮助界面
	void drawHelp(Canvas canvas,Paint paint){
		canvas.drawBitmap(help, new Rect(0,0,help.getWidth(), help.getHeight()),
						bgRectdst, paint);
	}
	
	//菜单界面绘制
	void paint(Canvas canvas, Paint paint, int gameState){
		switch (gameState) {
		case GameView.MAIN_MENU:
			drawMainMenu(canvas, paint);
			break;
		case GameView.HELP:
			drawHelp(canvas, paint);
			break;
		default:
			this.view.gameState = GameView.MAIN_MENU;
			break;
		}
	}
	
	void mainMenuInput(int keyCode)
	{
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			selectItem = (selectItem==0?4:selectItem-1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			selectItem = (selectItem==4?0:selectItem+1);
			break;
		case KeyEvent.KEYCODE_SPACE:
			switch (selectItem) {
			case 0://开始游戏
				view.gameState = GameView.GAME;
				view.gameScreen.init();
				
				break;
		//	case 1://读档
			//	boolean readFlag = view.gameStore.getData(view.gameScreen.player);
		//		if(readFlag)
			//		view.gameState = GameView.GAME;
		//		else
		//			view.gameState = GameView.RECORD;
		//		break;
			case 2://帮助
				view.gameState = GameView.HELP;
				break;
			case 4://退出游戏
				view.isRun = false;
				view.activity.finish();
				break;
			}
			break;
		}
	}
	
	void onKeyDown(int keyCode)
	{
		Log.i("test", "keyCode keyCode="+keyCode);
		
		switch (view.gameState) {
		case GameView.MAIN_MENU:
			mainMenuInput(keyCode);
			break;
		case GameView.RECORD:
			view.gameState = GameView.MAIN_MENU;
			break;
		case GameView.HELP:
			view.gameState = GameView.MAIN_MENU;
			break;
		}
		
	}

	protected void onTouchEvent(int x, int y){
		
		switch (this.view.gameState) {
		case GameView.MAIN_MENU:
			if(opRect[0][1].contains(x, y)){
				this.selectItem = 0;
				this.view.gameState = GameView.GAME;
			}
			else if(opRect[1][1].contains(x, y)){
				this.selectItem = 1;
				//this.view.gameState = GameView.RECORD;
				Toast.makeText(view.getContext(), "未实现继续功能", Toast.LENGTH_SHORT).show();
			}
			else if(opRect[2][1].contains(x, y)){
				this.selectItem = 2;
				//this.view.gameState = GameView.OPTION;
				Toast.makeText(view.getContext(), "未实现设置功能", Toast.LENGTH_SHORT).show();
			}
			else if(opRect[3][1].contains(x, y)){
				this.selectItem = 3;
				this.view.gameState = GameView.HELP;
			}
			else if(opRect[4][1].contains(x, y)){
				this.selectItem = 4;
				this.view.isRun = false;
				this.view.activity.finish();
			}
			else{
				this.selectItem = -1;
			}
			break;
		case GameView.HELP:
		case GameView.RECORD:
			this.view.gameState = GameView.MAIN_MENU;
			break;
		}
		
	}
}
