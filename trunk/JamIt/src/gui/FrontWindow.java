package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FrontWindow extends JFrame {

	/**
	 * 
	 */
	public BorderLayout topLayout;
	public JMenuBar menuBar;
	public JMenu menu, submenu;
	public JMenuItem menuitem;
	public TextArea alexGui;
	public JPanel topPanel = new JPanel();
	private static final long serialVersionUID = 1L;
	
	public FrontWindow(){
		super("Jam It In");
		topLayout = new BorderLayout();
		topPanel.setLayout(topLayout);
		
		initializeComponents();
		initializePanels();
		addItems();
		
		
		
	}
	
	private void addItems() {
		topPanel.add(alexGui, BorderLayout.CENTER);
		topPanel.add(menuBar, BorderLayout.NORTH);
		
		getContentPane().add(topPanel, BorderLayout.CENTER);
		
		
	}

	public static void main(String args[]){
		FrontWindow front = new FrontWindow();
		
		front.setLocation(10, 10);
		front.setSize(300, 300);
		
		front.setVisible(true);
	}
	
	private void initializePanels() {
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		submenu = new JMenu();
		alexGui = new TextArea();
		
		menuBar.add(menu);
		menuitem = new JMenuItem("New Room");
		menuitem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		menu.add(menuitem);
		
	}

	private void initializeComponents() {
		
		
	}
}