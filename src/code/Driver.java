package code;

public class Driver {
	
	public static void main(String[] args) {
		//javax.swing.SwingUtilities.invokeLater(new code.ui.UI());
		Thread t = new Thread(new code.ui.UI());
		t.start();
	}

}
