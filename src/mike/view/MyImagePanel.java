package mike.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MyImagePanel extends JPanel{

		public MyImagePanel(String path) {
			BufferedImage myPicture;
			try {
				myPicture = ImageIO.read(new File(path));
				JLabel picLabel = new JLabel(new ImageIcon(myPicture));
				add(picLabel);
			} catch (IOException e) {
				e.printStackTrace();
			
			}	
	}

}
