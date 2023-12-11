package group11.Graphic_AND_Controller;

import group11.SystemSolveSudoku.SudokuModel;
import group11.Views.SudokuGame;

public class SudokuController implements Controller {
	SudokuModel model;
	SudokuGame view;
	public SudokuController (SudokuModel model) {
		this.model = model;
		view = new SudokuGame(this, model);
	}
	@Override
	public void makeNewGame() {
		model.makeNewGame();
	}


	
}