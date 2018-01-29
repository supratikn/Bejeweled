package code.model;

import java.awt.Point;
import java.util.ArrayList;

public class MatchValue {
	private ArrayList<Point> _p;

	private boolean _b;

	public MatchValue() {
		_b = false;
	}

	public MatchValue(ArrayList<Point> p, boolean b) {
		_p = p;
		_b = b;

	}

	public ArrayList<Point> getPoints() {
		return _p;
	}

	public boolean isMatch() {
		return _b;
	}
}