package mike.view;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;


public class RacingView extends JPanel{
	MyImagePanel[] perSecondPic =new MyImagePanel[6];
	BufferedImage[] img = new BufferedImage[6];
	int picNum =0;
	int count = 0;
	public RacingView() {
		try {
		for(int i = 0 ;i<6;i++) {						
			img[i] = ImageIO.read(new File("public/racinggame/race"+i+".jpg"));		
		}
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		Timer timer = new Timer();
		MyTime mt = new MyTime();
		StopTimer stop = new StopTimer(timer);
		timer.schedule(mt, 0,450);
		timer.schedule(stop,10000);	
//		timer.schedule(mt, 0,1*70);
//		timer.schedule(stop,10000);	
		
	}
	
	protected void paintComponent(Graphics g) {	
			g.drawImage(img[picNum],0,0,null);
			repaint();
	}

	class MyTime extends TimerTask{
		@Override
		public void run() {
			int[] picSpeed = {0,1,2,1,3,1,2,3,1,4,4,5};
			if(picNum<5) {
				picNum = picSpeed[count];
				count++;
			}
						
		}	
	}
	class StopTimer extends TimerTask{
		private Timer timer;
		public StopTimer(Timer timer) {
			this.timer = timer; 
			
		}
		public void run() {		
			timer.cancel();
			timer.purge();
			timer = null;
		}
			
	}

}
