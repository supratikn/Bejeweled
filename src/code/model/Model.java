package code.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;

import code.model.HighScore;
import code.ui.UI;

public class Model {
	private int _counter;
	private UI _observer;
	private Random _rand;
	private int _score;
	private ArrayList<ArrayList<Integer>> _grid;
	private ArrayList<myButton> _clickedButtons;

	public Model() {

		_counter = 0;
		_rand = new Random();
		_clickedButtons = new ArrayList<myButton>();

		_grid = new ArrayList<ArrayList<Integer>>();

		for (int i = 0; i < 5; i++) {
			_grid.add(new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0)));
		}

	}

	public void clearClickedButton() {
		_clickedButtons.clear();
	}

	public void addClickedButton(myButton b) {
		_clickedButtons.add(b);
	}

	public ArrayList<ArrayList<Integer>> fillRandom() {

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {

				_grid.get(i).set(j, _rand.nextInt(6));

			}

		}

		forceLegalMove();
		initialHorizontalMatch();
		initialVerticalMatch();


		return _grid;
	}

	public void initialHorizontalMatch(){
		for (int i = 0; i < 5; i++) {

			for (int j = 0; j < 3; j++) {

				while (_grid.get(i).get(j) == _grid.get(i).get(j + 1)
						&& _grid.get(i).get(j + 1) == _grid.get(i).get(j + 2)) {
					_grid.get(i).set(j+1, _rand.nextInt(6));
				}

			}
		}
	}


	public void initialVerticalMatch(){
		for (int j = 0; j < 5; j++) {

			for (int i = 0; i < 3; i++) {

				while (_grid.get(i).get(j) == _grid.get(i + 1).get(j)
						&& _grid.get(i + 1).get(j) == _grid.get(i + 2).get(j)) {
					_grid.get(i+1).set(j, _rand.nextInt(6));
				}
			}

		}

	}

	public void forceLegalMove(){
		int m = _rand.nextInt(3 - 1) + 1;
		int n = _rand.nextInt(3 - 1) + 1;
		int l = _grid.get(m).get(n);
		while (_grid.get(m - 1).get(n) == l) {
			_grid.get(m - 1).set(n, _rand.nextInt(6));
		}
		_grid.get(m + 1).set(n, l);
		while (_grid.get(m + 2).get(n) == l) {
			_grid.get(m + 2).set(n, _rand.nextInt(6));
		}
		_grid.get(m + 2).set(n + 1, l);

	}
	public ArrayList<ArrayList<Integer>> getGrid() {
		return _grid;
	}

	public MatchValue match() {

		for (int i = 0; i < 5; i++) {

			for (int j = 0; j < 3; j++) {

				if (_grid.get(i).get(j) == _grid.get(i).get(j + 1)
						&& _grid.get(i).get(j + 1) == _grid.get(i).get(j + 2)) {
					Point p1 = new Point(i, j);
					Point p2 = new Point(i, j + 1);
					Point p3 = new Point(i, j + 2);
					ArrayList<Point> ap1 = new ArrayList<Point>();
					ap1.add(p1);
					ap1.add(p2);
					ap1.add(p3);
					_score = _score + 3;
					return new MatchValue(ap1, true);

				}
			}
		}

		for (int j = 0; j < 5; j++) {

			for (int i = 0; i < 3; i++) {

				if (_grid.get(i).get(j) == _grid.get(i + 1).get(j)
						&& _grid.get(i + 1).get(j) == _grid.get(i + 2).get(j)) {
					Point p1 = new Point(i, j);
					Point p2 = new Point(i + 1, j);
					Point p3 = new Point(i + 2, j);
					ArrayList<Point> ap2 = new ArrayList<Point>();
					ap2.add(p1);
					ap2.add(p2);
					ap2.add(p3);
					_score = _score + 3;
					return new MatchValue(ap2, true);

				}

			}
		}

		return new MatchValue(null, false);

	}

	public void checkLegalMove() throws InterruptedException {



		myButton b1 = _clickedButtons.get(0);
		myButton b2 = _clickedButtons.get(1);
		MatchValue mv = new MatchValue();

		float d = (float) Math
				.sqrt(Math.pow(b1.getRow() - b2.getRow(), 2) + Math.pow(b1.getColumn() - b2.getColumn(), 2));

		if (d <= 1) {

			this.swap(b1.getRow(), b1.getColumn(), b2.getRow(), b2.getColumn());

			mv = this.match();
			if (mv.isMatch()) {

				for (myButton b : _clickedButtons) {

					b.getButton().setBorder(BorderFactory.createLineBorder(null));
				}

				while (mv.isMatch()) {
					// update the grid to remove the match row/col
					matchResponse(mv);

					// update the UI with new grid values
					stateChanged(true);
					
					// check for additional match
					mv = this.match();

				}

				return;

			} else {
				this.swap(b1.getRow(), b1.getColumn(), b2.getRow(), b2.getColumn());
			}
		}

		for (myButton b : _clickedButtons) {

			b.getButton().setBorder(BorderFactory.createLineBorder(null));
		}

		stateChanged(false);
		return;
	}

	public void swap(int row1, int col1, int row2, int col2) {//

		int temp = _grid.get(row1).get(col1);
	int temp2 = _grid.get(row2).get(col2);
	_grid.get(row1).set(col1, temp2);
	_grid.get(row2).set(col2, temp);
	}

	public void addObserver(UI ui) {
		_observer = ui;
	}

	public void stateChanged(boolean changed) {

		// reset
		this.setCounter(0);
		this.clearClickedButton();
		
		if (_observer != null && changed) {

			_observer.update();
			LegalMoveValues lmv = this.checkEndGame();

			if (lmv.hasLegalMoves()) {

				_observer.highlightLegalMove(lmv.getPoints());
				lmv.getPoints().clear();

			}else{
				
				//System.out.println(lmv.getPoints());
				System.out.println("Game Over! Final score: " + getScore());

				System.exit(0);
			}

		}
	}

	public void setCounter(int c) {
		_counter = c;
	}

	public int getCounter() {
		return _counter;
	}

	public void matchResponse(MatchValue mv) {
		if (mv.getPoints() != null && !mv.getPoints().isEmpty()) {
			ArrayList<Point> pts = mv.getPoints();


			for (Point p : pts) {
				_grid.get(p.x).set(p.y, -1);
				for (int i = p.x; i > 0; i--) {
					this.swap(i, p.y, i - 1, p.y);

				}
			}

		}

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (_grid.get(i).get(j) == -1) {
					_grid.get(i).set(j, _rand.nextInt(6));
				}
			}
		}
	}

	public LegalMoveValues checkEndGame() {
		ArrayList<Point> moves = new ArrayList<Point>();
		


		for (int i = 0; i < _grid.size(); i++) {
			for (int j = 0; j < _grid.get(0).size(); j++) {
				Point p = new Point(i, j);
				Point check;
				// for up
				check = new Point(p.x - 1, p.y);
				if (inBounds(check, _grid) && matches(p, check, _grid)) {

					check = new Point(p.x - 2, p.y + 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x-2,p.y));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x - 2, p.y - 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x-2,p.y));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x - 3, p.y );
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(new Point(p.x-2,p.y));
						moves.add(check);
						return new LegalMoveValues(moves, true);
					}

				}
				/// for down
				check = new Point(p.x + 1, p.y);
				if (inBounds(check, _grid) && matches(p, check, _grid)) {

					check = new Point(p.x + 2, p.y + 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x+2,p.y));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x + 2, p.y - 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x+2,p.y));
						return new LegalMoveValues(moves, true);

					}

					check = new Point(p.x + 3, p.y );
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x+2,p.y));
						return new LegalMoveValues(moves, true);
					}
				}

				// for right
				check = new Point(p.x, p.y + 1);
				if (inBounds(check, _grid) && matches(p, check, _grid)) {

					check = new Point(p.x - 1, p.y + 2);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y+2));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x + 1, p.y + 2);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y+2));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x , p.y + 3);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y+2));
						return new LegalMoveValues(moves, true);
					}
				}
				// for left
				check = new Point(p.x, p.y - 1);
				if (inBounds(check, _grid) && matches(p, check, _grid)) {

					check = new Point(p.x - 1, p.y - 2);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y-2));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x + 1, p.y - 2);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y-2));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x , p.y - 3);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y-2));
						return new LegalMoveValues(moves, true);
					}
				}
				check = new Point(p.x, p.y - 2);
				if (inBounds(check, _grid) && matches(p, check, _grid)) {

					check = new Point(p.x - 1, p.y - 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y-1));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x + 1, p.y - 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y-1));
						return new LegalMoveValues(moves, true);
					}
				}
				check = new Point(p.x, p.y + 2);
				if (inBounds(check, _grid) && matches(p, check, _grid)) {

					check = new Point(p.x - 1, p.y + 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y+1));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x + 1, p.y+ 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x,p.y+1));
						return new LegalMoveValues(moves, true);
					}
				}
				////
				check = new Point(p.x-2, p.y );
				if (inBounds(check, _grid) && matches(p, check, _grid)) {

					check = new Point(p.x -1, p.y + 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x-1,p.y));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x - 1, p.y - 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x-1,p.y));
						return new LegalMoveValues(moves, true);
					}
				}
				check = new Point(p.x+2, p.y );
				if (inBounds(check, _grid) && matches(p, check, _grid)) {

					check = new Point(p.x +1, p.y + 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x+1,p.y));
						return new LegalMoveValues(moves, true);
					}
					check = new Point(p.x + 1, p.y - 1);
					if (inBounds(check, _grid) && matches(p, check, _grid)) {
						moves.add(check);
						moves.add(new Point(p.x+1,p.y));
						return new LegalMoveValues(moves, true);
					}

				}
			}


		}

		return new LegalMoveValues(null, false);
	}

	private boolean matches(Point p, Point q, ArrayList<ArrayList<Integer>> board) {
		return board.get(p.x).get(p.y) == (board.get(q.x).get(q.y));
	}

	private boolean inBounds(Point p, ArrayList<ArrayList<Integer>> board) {
		return p.x >= 0 && p.x < board.size() && p.y >= 0 && p.y < board.get(0).size();
	}
	public int getScore(){
		return _score;
	}
	public String getHighScore() throws IOException{
		changeHighScore();

		Iterator<String> it1 = new HighScore("HighScore/HighScore.txt");
		String s1 =it1.next();
		return s1+"";

	}
	public void changeHighScore() throws IOException{

		Iterator<String> it1 = new HighScore("HighScore/HighScore.txt");
		String s1 =it1.next();
		int result = Integer.parseInt(s1);


		BufferedReader file = new BufferedReader(new FileReader("HighScore/HighScore.txt"));
		String line;
		String input = "";

		while ((line = file.readLine()) != null)
			input += line + System.lineSeparator();
		if(getScore()>result){
			input = input.replace(s1, getScore()+"");
		}
		FileOutputStream os = new FileOutputStream("HighScore/HighScore.txt");
		os.write(input.getBytes());

		file.close();
		os.close();
	}
}

