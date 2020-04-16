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
	Rect opRect[][];			//5,2   һ����ʾͼƬ�ľ��� һ����ʾ������Ļ�ϵľ��� 
	GameView view;
	int selectItem;
	Bitmap help;
	
	
	//��ʼ�����췽��
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
			int x = 340;			//���ճ��ľ���
			int y = 300;			//�Ϸ��ճ��ľ���
			opRect[i][1] = new Rect( x, y*i + 200 ,								//200Ϊ��һ��ѡ���붥���ľ���
					x + 400, y*i + 200 + 156);	
			System.out.println(opRect[i][0] + "    " + opRect[i][1]);
			System.out.println(x + "   " + y);
		}
	}
	
	//�������˵�
	void drawMainMenu(Canvas canvas,Paint paint){
		canvas.drawBitmap(bg, bgRectsrc, bgRectdst, paint);
		for(int i = 0; i < 5; i++){
			if(i != this.selectItem)
				canvas.drawBitmap(option[0], opRect[i][0], opRect[i][1], paint);
			else
				canvas.drawBitmap(option[1], opRect[i][0], opRect[i][1], paint);
		}
	}
	
	//���ư�������
	void drawHelp(Canvas canvas,Paint paint){
		canvas.drawBitmap(help, new Rect(0,0,help.getWidth(), help.getHeight()),
						bgRectdst, paint);
	}
	
	//�˵��������
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
			case 0://��ʼ��Ϸ
				view.gameState = GameView.GAME;
				view.gameScreen.init();
				
				break;
		//	case 1://����
			//	boolean readFlag = view.gameStore.getData(view.gameScreen.player);
		//		if(readFlag)
			//		view.gameState = GameView.GAME;
		//		else
		//			view.gameState = GameView.RECORD;
		//		break;
			case 2://����
				view.gameState = GameView.HELP;
				break;
			case 4://�˳���Ϸ
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
				Toast.makeText(view.getContext(), "δʵ�ּ�������", Toast.LENGTH_SHORT).show();
			}
			else if(opRect[2][1].contains(x, y)){
				this.selectItem = 2;
				//this.view.gameState = GameView.OPTION;
				Toast.makeText(view.getContext(), "δʵ�����ù���", Toast.LENGTH_SHORT).show();
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
