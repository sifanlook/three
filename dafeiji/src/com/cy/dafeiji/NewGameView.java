package com.cy.dafeiji;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.cy.dafeiji.R.raw;
/*
 * run()�߳�ÿ10����ִ��һ��
 * ����interface GameImage�ӿڹ淶��
 * 		 public Bitmap getBitmap();
		 public int getX();
		 public int getY();��
		 ���ʵ�ַ���������ÿ��ͼƬ�ķ����̳���������ͼƬ��
      ���ص�����ͼƬ�������Ž�ͼƬ���ӿ�List<GAmeImage>��
   run()�������ж���
 * ����run()����(GameImage)image.getBitmap(),getX(),getY()������Щ����ѭ��ִ��
 * �����ӵ���������Ҫ��Ҫѭ����ӵĶ����磺���˵ķɻ�����ӵ�����Ҫ��run()��̬����У���Ϊֻ��run()��һֱִ�У������ͼƬֻ����init()�г�ʼ��ִ��һ��
 */
public class NewGameView extends SurfaceView implements SurfaceHolder.Callback,Runnable,android.view.View.OnTouchListener{
	private int fenshu=0;
	private boolean runing=false;
	private SurfaceHolder holder=null;
	private Bitmap erjihuancun;//����������Ƭ
	private Bitmap myPlane;//�Լ��ķɻ�
	private Bitmap direnPlane;//���˵ķɻ�
	private Bitmap bullet;//�ӵ�
	private Bitmap flyPlane;//���зɻ�
	private Bitmap bullet_goods;//����ը��
	private Bitmap bang;//��ը
	private Bitmap bgIMG;//����1
	private Bitmap bgIMG2;//����2
	private SoundPool pool;//������
	private int sound_mallbang;
	private int sound_bigbang;
	private int sound_middlebang;
	private int sound_shoot;
	private int sound_getgoods;
	private int w;//��Ļ�Ŀ��
	private int h;
	int i1=0;
	int i2=0;
	private ArrayList<GameImage> gameImages=new ArrayList<GameImage>();
	private ArrayList<Bullet>bullets=new ArrayList<Bullet>();
	public NewGameView(Context context) {
		super(context);
		this.getHolder().addCallback(this);
		this.setOnTouchListener(this);
			}

	private interface GameImage{
		 public Bitmap getBitmap();
		 public int getX();
		 public int getY();
	}
	//������Ƭ����
	private class BeijingImage implements GameImage{
		private Bitmap bg;
		private Bitmap newBitmap;
		private int height=0;
		private BeijingImage(Bitmap bg){
			this.bg=bg;
			//��һ����Ļ��С����Ƭ��������ʾ����
			newBitmap=Bitmap.createBitmap(w,h,Config.ARGB_8888);
		}
		@Override
		public Bitmap getBitmap() {
			Paint  p=new Paint();
			Canvas canvas=new Canvas(newBitmap);
			//ͼƬ�Ŀ�ߣ���ʼ��Ļ�Ŀ��(�ڶ���new Rect�ǣ����Ǹ���ߣ������Ǹ����)
			canvas.drawBitmap(bg,new Rect(0,0,bg.getWidth(),bg.getHeight()),
								 new Rect(0,height,w,h+height),p);
			//�ڶ��ű���ͼƬ�ӵ�һ��
			canvas.drawBitmap(bg,new Rect(0,0,bg.getWidth(),bg.getHeight()),
					             new Rect(0, -h+height, w , height),p);
			height+=3;
			if(height>=h){
				height=0;
			}
			return newBitmap;
		}

		@Override
		public int getX() {
			return 0;
		}

		@Override
		public int getY() {
			return 0;
		}
		
	}
	
	//�ҵķɻ�ͼƬ�Ĵ���
	public class MyPlane implements GameImage{
		private List<Bitmap>bitmaps=new ArrayList<Bitmap>();
		private int x;
		private int y;
		//ÿһ��СͼƬ�Ŀ��
		private int width;
		private int height;
		private int index=0;
		private MyPlane(Bitmap zijiPlane){
			width=zijiPlane.getWidth()/4;
			height=zijiPlane.getHeight();
			//�Ѵ����ͼƬ ��ȡ������
			bitmaps.add(Bitmap.createBitmap(zijiPlane, width*0, 0, width, height));
			bitmaps.add(Bitmap.createBitmap(zijiPlane, width*1, 0, width, height));
			bitmaps.add(Bitmap.createBitmap(zijiPlane, width*2, 0, width, height));
			bitmaps.add(Bitmap.createBitmap(zijiPlane, width*3, 0, width, height));
			x=(w-width)/2;
			y=h-height-20;
		}
		
		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		@Override
		public Bitmap getBitmap() {
			Bitmap bitmap=bitmaps.get(index);
			index++;
			if(index==bitmaps.size()){
				index=0;
			}
			return bitmap;
		}

		
		/*
		 * �õ�ͼƬ����Ļ�е�λ��
		 */
		@Override
		public int getX() {
			// TODO Auto-generated method stub
			return x;
		}

		@Override
		public int getY() {
			// TODO Auto-generated method stub
			return y;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}
		
	}
	//�ӵ�ͼƬ����
	private class Bullet implements GameImage{
		Bitmap bullet;
		int x;
		int y;
		private Bullet(MyPlane mp,Bitmap bullet){
			this.bullet=bullet;
			//�ӵ�����λ��
			x=mp.getX()+mp.getWidth()/2-bullet.getWidth()/2;
			y=mp.getY()+30;
		}
		//run()����getBitmap(),getX(),getY()������Щ����ѭ��ִ��
		@Override
		public Bitmap getBitmap() {
			Bitmap bitmap=this.bullet;
			y-=66;
			if(y<0){
				gameImages.remove(this);
			}
			return bitmap;
		}

		@Override
		public int getX() {
			// TODO Auto-generated method stub
			return x;
		}

		@Override
		public int getY() {
			// TODO Auto-generated method stub
			return y;
		}
		
	}
	//���˵ķɻ�ͼƬ����
	private class SmallDiren implements GameImage{
		List<Bitmap>smallsNo=new ArrayList<Bitmap>();//δ��ը���󼯺�
		List<Bitmap>smallsHave=new ArrayList<Bitmap>();//��ը���󼯺�
		private int x;
		private int y;
		int index=0;
		int width;//�л�Ŀ��
		int height;
		int width1;//��ը�Ŀ��
		int height1;
		boolean state=false;//�Ƿ񱻹�����
		private SmallDiren(Bitmap small,Bitmap bang){
			width=small.getWidth()/4;
			height=small.getHeight();	
			width1=bang.getWidth()/4;
			height1=bang.getHeight()/2;	
			//��ȡ�ɻ�δ��ը��ϵͼƬ����ӵ�����
			for (int i = 0; i < 4; i++) {
				smallsNo.add(Bitmap.createBitmap(small, width*i, 0, width, height));
			}
			//��ȡ��ըϵͼƬ����ӵ�����
			for (int i = 0; i < 4; i++) {
				smallsHave.add(Bitmap.createBitmap(bang, width1*i, 0, width1, height1));
			}
			for (int i = 0; i < 4; i++) {
				smallsHave.add(Bitmap.createBitmap(bang, width1*i, height1, width1, height1));
			}
			Random random=new Random();
			x=random.nextInt(w-width);
			//�õл�һ��ʼ����Ļ�������
			y=-small.getHeight();
		}
		@Override
		public Bitmap getBitmap() {
			Bitmap bitmap=smallsNo.get(index);
			index++;
			//�л���٣�������ʾ�����һ��,�Ƴ�����ɻ�
			if(index==smallsNo.size()&&state){
				gameImages.remove(this);
			}
			if(index==smallsNo.size()){
				index=0;
			}
			y+=12;
			if(y>=h){
				i2++;
				gameImages.remove(this);
				//Log.i("smallDiren", "�����"+i2+"�ܵл�");
			}
			return bitmap;
		}
		
		//�ܵ���������(�ӵ����ϴ���)
		public void attacket(ArrayList<Bullet>bullets){
			if(!state){//������һ�κ�����ӵ��㲻���ٻ�����
			//�����ӵ����ϣ��õ�ÿ���ӵ�����
			for(GameImage bullet:(List<GameImage>)bullets.clone()){
				if(bullet.getX()>x&&bullet.getX()<x+width//�ӵ�x����ڵл����࣬С�ڵл���Ҳࣨ��������
				 &&bullet.getY()<y&&bullet.getY()>height){//�ӵ�y��С�ڵл���²࣬���ڵл��ϲࣨ�������
					//�л����
					bullets.remove(bullet);//�Ƴ��ӵ�
					smallsNo=smallsHave;//���Ҫ��ʾ��ͼƬ����
					state=true;//������״̬Ϊ��
					fenshu+=100;
					new play(sound_mallbang,2).start();
					break;
				}
			}
		}
		}
		@Override
		public int getX() {
			return x;
		}

		@Override
		public int getY() {
			return y;
		}
		
	}
	//�ս�ը��ͼƬ
	private class bulletGoods implements GameImage{
		Bitmap good;
		int x;
		int y;
		private bulletGoods(Bitmap good){
			this.good=good;
			Random r=new Random();
			y=r.nextInt(h-2*good.getHeight());
			x=w;
		}
		@Override
		public Bitmap getBitmap() {
				x-=10;
				y-=36;
				if(x<4*w/7){
					x-=5;
					y+=86;
					//y=(int)(2*Math.pow(x, 2));
				}
				if(x<0||y>h){
					gameImages.remove(this);
				}
			
			return this.good;
		}

		@Override
		public int getX() {
			// TODO Auto-generated method stub
			return x;
		}

		@Override
		public int getY() {
			// TODO Auto-generated method stub
			return y;
		}
		
	}
	//�滭����
	@Override
	public void run() {
		int num=0;
		int num1=0;
		try {
			while(runing){
				Paint p=new Paint();
				Paint p1=new Paint();
				p1.setTextSize(50);
				p1.setDither(true);
				p1.setAntiAlias(true);
				p1.setColor(Color.YELLOW);
				//��ͼ
				Canvas canvas=holder.lockCanvas();
				Canvas newCanvas=new Canvas(erjihuancun);
				
				
				for ( GameImage image:(List<GameImage>) gameImages.clone()) {
					//�ҵ�ͼƬ���еĵл����
					if(image instanceof SmallDiren){
						((SmallDiren) image).attacket(bullets);
					}
					newCanvas.drawBitmap(image.getBitmap(), image.getX(), image.getY(),p);
				}
				//���Myplane�����£������һ���ӵ������һ�������߳�
				if(selectPlane!=null){
				bullets.add(new Bullet(selectPlane, bullet));
				new play(sound_shoot,0).start();
				}
				//�����ӵ�����
				for(GameImage image:(List<GameImage>)bullets.clone()){
					newCanvas.drawBitmap(image.getBitmap(), image.getX(), image.getY(),p);
				}
				//ѭ����ӵл�
				if(num==12){
					gameImages.add(new SmallDiren(direnPlane,bang));
					num=0;
				}
				//ѭ����ӿ���ը��
				if(num1==50){
					gameImages.add(new bulletGoods(bullet_goods));
					num1=0;
				}
				//���(��󻭻���ʾ��������)
				newCanvas.drawText("����:"+fenshu, 0, 50, p1);
				num++;
				num1++;
				
				
				//��ͼ
				canvas.drawBitmap(erjihuancun, 0,0, new Paint());
				holder.unlockCanvasAndPost(canvas);
				Thread.sleep(10);
			}
		} catch (Exception e) {
			Log.e("run", "run�쳣");
			e.printStackTrace();
		}
	}
	//��ʼ������ͼƬ���Զ����ࣩ
	private void init() {
		//��������ͼƬ
		myPlane=BitmapFactory.decodeResource(getResources(), R.drawable.my);
		direnPlane=BitmapFactory.decodeResource(getResources(), R.drawable.diren);
		bullet=BitmapFactory.decodeResource(getResources(), R.drawable.zidan);
		bang=BitmapFactory.decodeResource(getResources(), R.drawable.baozha);
		bgIMG=BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		flyPlane=BitmapFactory.decodeResource(getResources(), R.drawable.fly);
		bullet_goods=BitmapFactory.decodeResource(getResources(), R.drawable.bullet_goods);
		//���뱳��ͼƬ
		gameImages.add(new BeijingImage(bgIMG));
		//�����ҵķɻ�ͼƬ
		gameImages.add(new MyPlane(myPlane));
		//����л�ͼƬ
		gameImages.add(new SmallDiren(direnPlane,bang));
		//�������ը��ͼƬ
		gameImages.add(new bulletGoods(bullet_goods));
		
		//��ɶ���������Ƭ
		erjihuancun=Bitmap.createBitmap(w, h, Config.ARGB_8888);
		//��������
		pool=new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//��󲥷�������ͣ���׼���ͣ�������Ʒ��Ʒ��
		sound_mallbang=pool.load(getContext(),raw.explosion,1 );
		sound_shoot=pool.load(getContext(),raw.shoot,1 );
		sound_bigbang=pool.load(getContext(),raw.bigexplosion,1 );
		sound_middlebang=pool.load(getContext(),raw.explosion2,1 );
		sound_getgoods=pool.load(getContext(),raw.get_goods,1 );
	}
	//�����߳�
	class play extends Thread{
		int soundId;
		int whoFirst;
		private play(int soundId,int whoFirst){
			this.soundId=soundId;
			this.whoFirst=whoFirst;
		}
		@Override
		public void run() {
			pool.play(soundId, 1.0f, 1.0f, whoFirst, 0, 1.0f);//�����,�����,���ȼ�,����ѭ������,����
		}
	}
	MyPlane selectPlane;
	//�ҵķɻ����¼�
	public boolean onTouch(View v,MotionEvent event) {
		if(event.getAction()==event.ACTION_DOWN){//������Ļ
			for (GameImage image : gameImages ) {
				if(image instanceof MyPlane){//����µ����ҵķɻ�
					MyPlane mp=(MyPlane) image;//�õ�������·ɻ�
					if(event.getX()>mp.getX()&&
					   event.getX()<mp.getX()+mp.getWidth()&&
					   event.getY()>mp.getY()&&
					   event.getY()<mp.getY()+mp.getHeight()){
						selectPlane=mp;
						//Log.i("onTouch", "�����MyPlane");
					}else{
						selectPlane=null;
						//Log.i("onTouch", "û��dian'zhong�ɻ�");
					}
					break;
				}
			}
		}
		else if(event.getAction()==event.ACTION_MOVE){//���²��ƶ�(������Ļ���¼�ִ������ִ��)
			if(selectPlane!=null){
				selectPlane.setX((int) event.getX()-selectPlane.getWidth()/2);
				selectPlane.setY((int)event.getY()-selectPlane.getHeight()/2);
			}
		}
		else if(event.getAction()==event.ACTION_UP){//�ɿ�
			selectPlane=null;
		}
		return true;
	}

	//��ͼ����
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}
	//��ͼ�ı䣬surfaceCreated����ִ�к󣬸ú�������ִ��һ�Ρ���ô�ڵ�һ��ִ�б㴫������Ļ�Ŀ��
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		//�õ���Ļ���
		w=width;
		h=height;
		init();
		this.holder=holder;
		runing=true;
		new Thread(this).start();
	}
	//��ͼ���
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		pool.release();
		pool=null;
		runing=false;
	}
}
