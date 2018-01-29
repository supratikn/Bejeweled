package code.model;

import javax.swing.JButton;

public class myButton {
	private int _r;
	private int _c;
	private JButton _b;

	public myButton(int row, int column) {
		_b = new JButton();
		_r = row;
		_c = column;
	}

	public int getRow() {
		return _r;

	}

	public int getColumn() {
		return _c;
	}

	public JButton getButton() {
		return _b;
	}
}
