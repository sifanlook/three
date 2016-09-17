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
//ʵ��SurfaceHolder������Callback�ص�������֪ͨ��ʲôʱ����Ի�
public class TestGameView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
	private SurfaceHolder holder=null;
	private boolean runing=false;
	//����һ����Ƭ
	private Bitmap gameBitmap=null;
	public TestGameView(Context context) {
		super(context);
		this.getHolder().addCallback(this);//��GameView��ע��Callback�ص�����
		gameBitmap=Bitmap.createBitmap(500, 500, Config.ARGB_8888);//�������
	}

	public void run(){
		try {
			while(runing){
				//1.����һ����ͼ
				//2.����һ������ͼ��������ͼ�ϻ�һ����Ƭ
				//3.��������Ƭ���ظ���������ͼ
				//4.������ͼ

				
				
				Random ran=new Random();
				//�������������ͼ(������canvas��)
				Canvas canvas=holder.lockCanvas();//��������ָ����Χholder.lockCanvas(new Rect(0, 0, 100, 100));
				Paint p=new Paint();
				
				//���´�������ͼ�л�һ����Ƭ
				Canvas c=new Canvas(gameBitmap);
				//p.setColor(Color.WHITE);
				//c.drawRect(new Rect(0, 0, 500, 500), p);
				//�������������ɫ
				p.setColor(Color.rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
				c.drawLine(ran.nextInt(1000), ran.nextInt(1000), ran.nextInt(1000), ran.nextInt(1000), p);
				
				//�ѻ��õ���Ƭ��ԭ�������ģ���һ����ͼ��ȥ��
				canvas.drawBitmap(gameBitmap, 0, 0, new Paint());
				//������ͼ
				holder.unlockCanvasAndPost(canvas);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			Log.e("run", "run�����쳣");
			e.printStackTrace();
		}
	}

	// ��ͼ������ʱ��֪ͨ��
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//���뻭��ͼֽ
		this.holder=holder;
		runing = true;
		new Thread(this).start();
		Log.i("Surface", "��ͼ����");
	}
	//���淢���ı��ʱ��֪ͨ��
	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
		Log.i("Surface", "��ͼ�ı�");
	}
	//�������ٵ�ʱ��֪ͨ��
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		runing=false;
		Log.i("Surface", "��ͼ����");
	}

	
}
