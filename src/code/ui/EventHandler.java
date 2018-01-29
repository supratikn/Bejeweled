package code.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import code.model.myButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;

import code.model.Model;

public class EventHandler implements ActionListener {

	private Model _model;

	private myButton _b;
	private int _c;

	public EventHandler(Model m, myButton b) {
		_model = m;

		_b = b;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		_c = _model.getCounter();
		_c = _c + 1;
		_model.setCounter(_c);

		JButton b2 = _b.getButton();
		_model.addClickedButton(_b);

		if (_c == 1) {

			b2.setBorder(BorderFactory.createLineBorder(java.awt.Color.RED, 4));

		}

		if (_c > 1) {
			_b.getButton().setBorder(BorderFactory.createLineBorder(null));

			try {
				_model.checkLegalMove();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

}
