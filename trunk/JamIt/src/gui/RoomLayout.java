package jam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class RoomLayout{
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});

	}
	
	private static void createGUI() {
		JFrame frame = new JFrame("RoomLayout");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new LayoutPanel());
		frame.pack();
		frame.setVisible(true);
	}
}

class LayoutPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int xSize = 1280;
	private int ySize = 800;
	
	
	public LayoutPanel() {
		addMouseListener(new MouseAdapter() {});
		addMouseMotionListener(new MouseAdapter() {});
		
		new Thread() {	
			public void run() {
				gameUpdate();
			}
		}.start();
	}
	

	
	public void gameUpdate() {
		while (true) {
			try {
				repaint();
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(600,600);
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Set up image for double buffering, get a graphics context to draw to
		Image image = createImage(xSize, ySize);
		Graphics buffer = image.getGraphics();
		
		// Make white background
		buffer.setColor(Color.WHITE);
		buffer.fillRect(0, 0, xSize, ySize);
		buffer.setColor(Color.BLACK);
		
		drawMenu(buffer);
		drawAreas(buffer);
		drawGrid(buffer);
		
		
		g.drawImage(image, 0, 0, Color.BLUE, null);
	}
	
	public void drawMenu(Graphics b) {
	
	}
	
	public void drawAreas(Graphics b) {
		
	}
	
	public void drawGrid(Graphics b) {
		for (int x = 0; x < 32; x++) {
			for (int y = 0; y < 32; y++) {
				b.fillOval(50+x*15, 50+y*15, 3, 3);
			}
		}
	}
}
