package group11.Client;

import group11.Graphic_AND_Controller.Controller;
import group11.Graphic_AND_Controller.SudokuController;
import group11.SystemSolveSudoku.SudokuModel;

public class Start {
	public static void main(String[] args) {
		SudokuModel model =new SudokuModel();
		Controller controller = new SudokuController(model);
	}
}
