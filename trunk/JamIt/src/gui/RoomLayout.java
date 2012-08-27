package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	
	boolean room[][] = new boolean[32][32];
	int mouseX = 0;
	int mouseY = 0;
	int mouseX2 = 0;
	int mouseY2 = 0;
	
	// TODO: Right click expands the room, left click removes parts of the room
	public LayoutPanel() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed (MouseEvent e) { // Get the first corner for a selection
				mouseX = Math.round((e.getX() - 50)/15f);
				mouseY = Math.round((e.getY() - 50)/15f);
				
				if (mouseX < 0) mouseX = 0;
				if (mouseY < 0) mouseY = 0;
				if (mouseX > 32) mouseX = 32;
				if (mouseY > 32) mouseY = 32;
				mouseX2 = mouseX;
				mouseY2 = mouseY;
				repaint();
			}
			
			public void mouseReleased(MouseEvent e) {
				if (mouseX == mouseX2 || mouseY == mouseY2)
					return;
				int top, bottom, left, right;
				if (mouseX < mouseX2) {
					left = mouseX;
					right = mouseX2;
				} else {
					left = mouseX2;
					right = mouseX;
				}
				if (mouseY < mouseY2) {
					top = mouseY;
					bottom = mouseY2;
				} else {
					top = mouseY2;
					bottom = mouseY;
				}
				
				for (int x = left; x < right; x++) {
					for (int y = top; y < bottom; y++) {
						if (e.getButton() == MouseEvent.BUTTON1)
							room[x][y] = true;
						if (e.getButton() == MouseEvent.BUTTON3)
							room[x][y] = false;
					}
				}
				repaint();
			}
		});
		
		addMouseMotionListener(new MouseAdapter() { // Get the second corner of the selection
			public void mouseDragged (MouseEvent e) {
				mouseX2 = Math.round((e.getX() - 50)/15f);
				mouseY2 = Math.round((e.getY() - 50)/15f);
				
				if (mouseX2 < 0) mouseX2 = 0;
				if (mouseY2 < 0) mouseY2 = 0;
				if (mouseX2 > 32) mouseX2 = 32;
				if (mouseY2 > 32) mouseY2 = 32;
				repaint();
			}
		});
		
		repaint();
		
//		new Thread() {	
//			public void run() {
//				gameUpdate();
//			}
//		}.start();
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
		for (int x = 0; x < room.length; x++) {
			for (int y = 0; y < room[0].length; y++) {
				if (room[x][y]) {
					b.setColor(Color.BLUE);
					b.fillRect(52+x*15, 52+y*15, 15, 15);
					b.setColor(Color.BLACK);
				}
			}
		}
	}
	
	public void drawGrid(Graphics b) {
		for (int x = 0; x < 33; x++) {
			for (int y = 0; y < 33; y++) {
				b.fillOval(50+x*15, 50+y*15, 3, 3);
			}
		}
		//b.drawOval(50+mouseX*15-1, 50+mouseY*15-1, 5, 5);
		//b.drawLine(52+mouseX*15, 52+mouseY*15, 52+mouseX2*15, 52+mouseY2*15);
		
		int xcorner;
		int ycorner;
		if (mouseX < mouseX2)
			xcorner = mouseX;
		else
			xcorner = mouseX2;
		if (mouseY < mouseY2)
			ycorner = mouseY;
		else
			ycorner = mouseY2;
		
		b.drawRect(52+xcorner*15, 52+ycorner*15, Math.abs((mouseX-mouseX2)*15), Math.abs((mouseY-mouseY2)*15));
	}
}
