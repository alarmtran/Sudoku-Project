package group11.SystemSolveSudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class GraphService {
	protected int kLength = 9;
	protected int kPopulationLimit = 50;
	protected int kMin = 1;
	protected int kMax = 1000;
	protected float kMutationFrequency = 0.33f;

	protected ArrayList<GraphSV> graphSVS = new ArrayList<GraphSV>();
	protected ArrayList<GraphSV> graphSVReproducers = new ArrayList<GraphSV>();
	protected ArrayList<GraphSV> graphSVResults = new ArrayList<GraphSV>();
	protected ArrayList<GraphSV> graphSVFamily = new ArrayList<GraphSV>();
	protected int CurrentPopulation;
	protected int generation = 1;
	protected boolean best2 = true;
	protected GraphSV lastGraphSV;
	public int[][] board = new int[9][9];
	static final int SIZE = 9;
	static Random r = new Random();
	boolean refreshGame = true;

	public ArrayList<ArrayList<Integer>> lastChoice = new ArrayList<>();
	Graph graph;
	public GraphService() {

	}

	public void refresh() {
		for (int i = 0; i < 1000; i++) {
			GraphSV aGraphSV = new GraphSV(kLength, kMin, kMax);
			aGraphSV.setCrossoverPoint(9/2);
			aGraphSV.calFitness();
			graphSVS.add(aGraphSV);
		}
	}

	public void mutate(GraphSV aGene) {
		if (GraphSV.random.nextInt(100) < (int) (kMutationFrequency * 100.0)) {//33%
			aGene.mutate();
		}
	}

	public void calculateFitnessForAll(ArrayList<GraphSV> genes) {
		for (GraphSV g : genes) {
			g.calculateFitness();
		}
	}

	public void doCrossover(ArrayList<GraphSV> genes) {
		ArrayList<GraphSV> geneMoms = new ArrayList<GraphSV>();
		ArrayList<GraphSV> geneDads = new ArrayList<GraphSV>();
		for (int i = 0; i < genes.size(); i++) {
			if (GraphSV.random.nextInt(100) % 2 > 0) {
				geneMoms.add(genes.get(i));
			} else {
				geneDads.add(genes.get(i));
			}
		}
		// Can bang
		if (geneMoms.size() > geneDads.size()) {
			while (geneMoms.size() > geneDads.size()) {
				geneDads.add(geneMoms.get(geneMoms.size() - 1));
				geneMoms.remove(geneMoms.size() - 1);
			}
			if (geneDads.size() > geneMoms.size()) {
				geneDads.remove(geneDads.size() - 1);
			}
		} else {
			while (geneDads.size() > geneMoms.size()) {
				geneMoms.add(geneDads.get(geneDads.size() - 1));
				geneDads.remove(geneDads.size() - 1);
			}
			if (geneMoms.size() > geneDads.size()) {
				geneMoms.remove(geneMoms.size() - 1);
			}
		}
		// Lai ghep
		for (int i = 0; i < geneDads.size(); i++) {
			GraphSV childGene1 = geneDads.get(i).crossOver(geneMoms.get(i));
			GraphSV childGene2 = geneMoms.get(i).crossOver(geneDads.get(i));
			graphSVFamily.clear();
			graphSVFamily.add(geneDads.get(i));
			graphSVFamily.add(geneMoms.get(i));
			graphSVFamily.add(childGene1);
			graphSVFamily.add(childGene2);
			calculateFitnessForAll(graphSVFamily);
			Collections.sort(graphSVFamily);
			graphSVResults.add(graphSVFamily.get(0));
			graphSVResults.add(graphSVFamily.get(1));
		}
	}

	public void nextGeneration() {
		generation++;
		graphSVResults.clear();
		doCrossover(graphSVS);
		graphSVS = (ArrayList<GraphSV>) graphSVResults.clone();
		for (int i = 0; i < graphSVS.size(); i++) {
			mutate(graphSVS.get(i));
		}
		for (int i = 0; i < graphSVS.size(); i++) {
			graphSVS.get(i).calFitness();
		}
		Collections.sort(graphSVS);
		for (int i = graphSVS.size() - 1; i > kPopulationLimit; i--) {
			graphSVS.remove(i);

		}
		CurrentPopulation = graphSVS.size();
	}

	public void CalculateFitnessForAll(ArrayList<GraphSV> genes) {
		for (GraphSV lg : genes) {
			lg.calFitness();
		}
	}

	public GraphSV getLastGraph() {
		return lastGraphSV;
	}

	public void makeNewGame() {
		refresh();
		for (int i = 0; i < 1000; i++) {
			nextGeneration();
			print();
		}
		int[][] lastState = lastGraphSV.getMatrix();
		makeProblem(lastState);
		display2(lastState);
		solveGame();


	}

	public int[][] copyState(int[][] state) {
		int[][] result = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				result[i][j] = state[i][j];
			}
		}
		return result;
	}

	public void display2(int board[][]) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(" " + board[i][j]);
			}

			System.out.println();
		}

		System.out.println();
	}

	
	
	
	
	public void solveGame() {

		if (solve()) {
			System.out.println("Tạo Thành công");
			display2(lastGraphSV.getMatrix());
			refreshGame =true;

		} else {
			System.out.println("Thất bại");
			refreshGame =false;
		}
		graphSVFamily.clear();
		graphSVResults.clear();
		graphSVReproducers.clear();
		graphSVS.clear();
	}
	public boolean isRefreshGame() {
		return refreshGame;
	}
	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public void print() {
		Collections.sort(graphSVS);
		lastGraphSV = graphSVS.get(0);
	}

	//Tạo độ khó
	public void makeProblem(int[][] state) {
		for (int k = 0; k < 9; k++) {
			for (int i = 0; i < 9; i++) {
				if (i < 8) {
					for (int j = i + 1; j < 9; j++) {
						if (state[k][i] == state[k][j]) {
							state[k][i] = 0;
						}
					}
				}
			}
		}
		for (int k = 0; k < 9; k++) {
			for (int i = 0; i < 9; i++) {
				if (i < 8) {
					for (int j = i + 1; j < 9; j++) {
						if (state[i][k] == state[j][k]) {
							state[i][k] = 0;
						}
					}
				}
			}
		}

		for (int l = 0; l < 5; l++) {
			for (int k = 0; k < 9; k++) {
				int i = 1 + r.nextInt(8);
				state[k][i] = 0;
			}
			for (int k = 0; k < 9; k++) {
				int i = 1 + r.nextInt(8);
				state[i][k] = 0;
			}
		}
	}

	// Check xem nó có thể nằm ở hàng đó không
	private boolean isInRow(int row, int number) {
		for (int i = 0; i < SIZE; i++)
			if (lastGraphSV.getMatrix()[row][i] == number)
				return true;
		return false;
	}

	// Check xem nó có thể nằm cột đó không
	private boolean isInCol(int col, int number) {
		for (int i = 0; i < SIZE; i++)
			if (lastGraphSV.getMatrix()[i][col] == number)
				return true;

		return false;
	}

	//Kiểm tra xem số đó có thể nằm trong ô ma trận 3x3 này ko
	private boolean isInBox(int row, int col, int number) {
		int r = row - row % 3;
		int c = col - col % 3;

		for (int i = r; i < r + 3; i++)
			for (int j = c; j < c + 3; j++)
				if (lastGraphSV.getMatrix()[i][j] == number)
					return true;
		return false;
	}

	// Check cả 3 điều kiện ta được 1 ô đặt đúng số
	private boolean isOk(int row, int col, int number) {
		return !isInRow(row, number) && !isInCol(col, number) && !isInBox(row, col, number);
	}

	public boolean solve2(){
		int[][] matrix = lastGraphSV.getMatrix();
		graph = new Graph(lastGraphSV.getMatrix());
		graph.init();
		Vertex[] cells = graph.getCells();
		for(int i=0;i< cells.length;i++){
			if(cells[i].getValue() == 0){
				ArrayList<Integer> listNumber = new ArrayList<>();
				int[] check = new int[10];
				for(int j=1;j<=9;j++){
					listNumber.add(j);
				}
				ArrayList<Vertex> listNeighbor = graph.listVertex().get(i).getNeighbor();
				//Loại dần màu sắc
				for(Vertex v : listNeighbor){
					if(listNumber.contains(v.getValue())){
						check[v.getValue()] = 1;
					}
				}
				//Tiến hành check xem số màu còn lại màu nào khả thi
				int cot = i % 9;
				int hang = i / 9;
				for(int j=1;j<=9;j++){
					if(check[j] == 0){
						if(isInRow(hang,j) == false && isInCol(cot,j) == false && isInBox(hang,cot,j) == false){
							lastGraphSV.getMatrix()[hang][cot] = j;
							graph.listVertex().get(i).setValue(j);
							//display2(lastGraphSV.getMatrix());
							if(solve2()){
								return true;
							}else{
								lastGraphSV.getMatrix()[hang][cot] = 0;
							}
						}
					}
				}
				return false;
			}
		}
		return true;
	}


	//Hiện tại bọn em mới chỉ kịp làm backtracking , còn làm bằng đồ thị bọn em sẽ cố gắng hoàn thiện nốt và thuyết trình vào ngày 18/12

	public boolean solve() {
		/*for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				//Bước đi tìm những ô trống do các phần tử bằng 0 là biểu thị cho các ô trống
				if (lastGraphSV.getMatrix()[row][col] == 0) {
					// Thử các số
					for (int number = 1; number <= SIZE; number++) {
						if (isOk(row, col, number)) {
							//Nếu số đó ok thì đặt vào
							lastGraphSV.getMatrix()[row][col] = number;
							display2(lastGraphSV.getMatrix());
							if (solve()) { //Tiến hành quay lui
								return true;
							} else { //Nếu ko giải được thì để = 0 ô đó và tieps tục thực hiện
								lastGraphSV.getMatrix()[row][col] = 0;
							}
						}
					}
					return false;
				}
			}
		}
		return true;*/
		return solve2();
	}
}
