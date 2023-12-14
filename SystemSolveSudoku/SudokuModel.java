package group11.SystemSolveSudoku;


public class SudokuModel {
	GraphService graphService;
	public SudokuModel() {
		graphService = new GraphService();
	}
	public void makeNewGame() {
		graphService.makeNewGame();
	}
	public GraphSV getGenome() {
		return graphService.getLastGraph();
	}
	public boolean isSuccess() {
		return graphService.isRefreshGame();
	}
	public static void main(String[] args) {
		SudokuModel s = new SudokuModel();
		s.makeNewGame();
	}
}
