package code.model;

import java.awt.Point;
import java.util.ArrayList;


public class LegalMoveValues {

	private boolean _hasLegalMoves;
         private ArrayList<Point> _points;
         
         public LegalMoveValues(){
        	 _hasLegalMoves =false;
         }
	public LegalMoveValues(ArrayList<Point> p, boolean b ){
		_points =p;
		_hasLegalMoves=b;
		
	}
	public boolean hasLegalMoves(){
		return _hasLegalMoves; 
	}
	public ArrayList<Point> getPoints(){
		return _points;
	}
	
}
