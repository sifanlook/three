package com.cy.dafeiji;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
//实现SurfaceHolder，调用Callback回调方法，通知你什么时候可以画
public class TestGameView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
	private SurfaceHolder holder=null;
	private boolean runing=false;
	//创建一张照片
	private Bitmap gameBitmap=null;
	public TestGameView(Context context) {
		super(context);
		this.getHolder().addCallback(this);//在GameView中注册Callback回调方法
		gameBitmap=Bitmap.createBitmap(500, 500, Config.ARGB_8888);//最高质量
	}

	public void run(){
		try {
			while(runing){
				//1.锁定一个视图
				//2.创建一个新视图，在新视图上画一张照片
				//3.把这张照片返回给锁定的视图
				//4.解锁视图

				
				
				Random ran=new Random();
				//获得已锁定的视图(放置在canvas中)
				Canvas canvas=holder.lockCanvas();//参数锁定指定范围holder.lockCanvas(new Rect(0, 0, 100, 100));
				Paint p=new Paint();
				
				//在新创建的视图中画一张照片
				Canvas c=new Canvas(gameBitmap);
				//p.setColor(Color.WHITE);
				//c.drawRect(new Rect(0, 0, 500, 500), p);
				//给笔设置随机颜色
				p.setColor(Color.rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
				c.drawLine(ran.nextInt(1000), ran.nextInt(1000), ran.nextInt(1000), ran.nextInt(1000), p);
				
				//把画好的照片还原到锁定的（第一个视图中去）
				canvas.drawBitmap(gameBitmap, 0, 0, new Paint());
				//解锁视图
				holder.unlockCanvasAndPost(canvas);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			Log.e("run", "run方法异常");
			e.printStackTrace();
		}
	}

	// 视图创建的时候通知你
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//传入画板图纸
		this.holder=holder;
		runing = true;
		new Thread(this).start();
		Log.i("Surface", "视图创建");
	}
	//界面发生改变的时候通知你
	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
		Log.i("Surface", "视图改变");
	}
	//界面销毁的时候通知你
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		runing=false;
		Log.i("Surface", "视图销毁");
	}

	
}
