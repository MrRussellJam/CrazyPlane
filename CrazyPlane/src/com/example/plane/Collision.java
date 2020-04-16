package com.example.plane;

public class Collision {
	public static boolean collides(int x,int y,int w,int h,int x1,int y1,int w1,int h1){
		if(y1+h1 < y || y+h<y1 || x1+w1 <x || x+w< x1)
			return false;
		else
			return true;
	}
}
