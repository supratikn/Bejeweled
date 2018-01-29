package code.ui;


import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import code.model.myButton;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import code.ui.EventHandler;
import code.model.Model;

public class UI implements Runnable {

	private JFrame _frame;

	private Model _model;

	private ArrayList<ImageIcon> _imageList;

	private ArrayList<ArrayList<JButton>> _jbs;
	private ArrayList<JButton> _bs;

	@Override
	public void run() {
		_jbs = new ArrayList<ArrayList<JButton>>();
		_model = new Model();
		_bs = new ArrayList<JButton>();

		_frame = new JFrame("Supratik Neupane's Lab 11");
		_imageList = new ArrayList<ImageIcon>();
		_imageList.add(new ImageIcon("Images/Tile-0.png"));
		_imageList.add(new ImageIcon("Images/Tile-1.png"));
		_imageList.add(new ImageIcon("Images/Tile-2.png"));
		_imageList.add(new ImageIcon("Images/Tile-3.png"));
		_imageList.add(new ImageIcon("Images/Tile-4.png"));
		_imageList.add(new ImageIcon("Images/Tile-5.png"));
		

		this.refreshUIGrid(true);

		_model.addObserver(this);

		_frame.getContentPane().setLayout(new GridLayout(6, 5));

		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.pack();
		_frame.setVisible(true);

	}

	public void refreshUIGrid(boolean isCreate) {
		ArrayList<ArrayList<Integer>> grid;

		if (isCreate) {
			grid = _model.fillRandom();
		} else {
			grid = _model.getGrid();
		}

		_frame.getContentPane().removeAll();
		_jbs.clear();
		
		for (int i = 0; i < 5; i++) {

			_bs=new ArrayList<JButton>();
			for (int j = 0; j < 5; j = j + 1) {

				myButton temp = new myButton(i, j);

				JButton b = temp.getButton();

				b.setEnabled(true);

				b.setIcon(_imageList.get(grid.get(i).get(j)));
				
				

				_bs.add(b);
				_frame.add(b);

				b.addActionListener(new EventHandler(_model, temp));

			}
			_jbs.add(_bs);
		}
	
		
		_frame.add(new JLabel());
      JLabel jScore= new JLabel();
	  _frame.add(jScore);
	  jScore.setForeground(java.awt.Color.RED);
      jScore.setOpaque(true);
	  jScore.setFont(new Font("Ariel",Font.BOLD,14));
	  jScore.setBorder(BorderFactory.createEmptyBorder(0,0,8,8));
	  jScore.setText("Score: "+_model.getScore());
	  JLabel jHigh = new JLabel();
	  jHigh.setOpaque(true);
	  jHigh.setForeground(java.awt.Color.BLUE);
	  jHigh.setFont(new Font("Ariel",Font.BOLD,14));
	  jHigh.setBorder(BorderFactory.createEmptyBorder(0,0,8,8));
	  try {
		jHigh.setText("High Score: "+_model.getHighScore());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	_frame.add(new JLabel());
	  _frame.add(jHigh);
	  _frame.pack();
	}
	

	public void highlightLegalMove(ArrayList<Point> ps)
	{
		JButton tBs;
		for(Point p:ps){
			tBs=_jbs.get(p.x).get(p.y);
			tBs.setBorder(BorderFactory.createLineBorder(java.awt.Color.ORANGE, 4));
		}
	}
	

	
public void update() {
	
			this.refreshUIGrid(false);
		
		
	}



}
