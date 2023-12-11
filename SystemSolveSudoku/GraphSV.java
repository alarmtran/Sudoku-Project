package group11.SystemSolveSudoku;

import java.util.HashSet;
import java.util.Random;


//ĐỒ THỊ
public class GraphSV implements Comparable<GraphSV> {
	int matrix[][] = new int[9][9];
	static Random random = new Random();
	public long length;
	public int crossoverPoint;
	//public int mutationIndex;
	public float currentFitness = 0.0f;
	int theMin = 0;
	int theMax = 1000;
	HashSet<Integer> rowMap = new HashSet<Integer>(); //Các phần tử theo hàng
	HashSet<Integer> columnMap = new HashSet<Integer>(); //Các phn tử theo cột
	HashSet<Integer> squareMap = new HashSet<Integer>(); //Các phần tử theo ô

	public void setMatrix(int[][] matrix) {
		this.matrix = this.matrix;
	}


	@Override
	public int compareTo(GraphSV o) {
		if (this.currentFitness < o.currentFitness)
			return 1;
		else if (this.currentFitness > o.currentFitness)
			return -1;
		else
			return 0;
	}

	public void setCrossoverPoint(int crossoverPoint) {
		this.crossoverPoint = crossoverPoint;
	}

	public GraphSV() {

	}

	public GraphSV(long length, int min, int max) {
		this.length = length;
		this.theMax = max;
		this.theMin = min;
		//Sinh ma trận 9x9 các số từ 1-9
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				this.matrix[i][j] = 1+random.nextInt(8);
			}
		}
	}

	public boolean canDie(float fitness) {
		if (currentFitness <= (int) (fitness * 100.0f)) {
			return true;
		}
		return false;
	} 

	public boolean canReproduce(float fitness) {
		if (random.nextInt(100) >= (int) (fitness * 100.0f)) {
			return true;
		}
		return false;
	}

	public void mutate() {
		int mutationIndex1 = random.nextInt(9);
		int mutationIndex2 = random.nextInt(9);
		int mutationIndex3 = random.nextInt(9);
		if (random.nextInt(2) == 1) {
			this.matrix[mutationIndex1][mutationIndex2] = mutationIndex3 + 1;
		} else {
			int temp = 0;
			if (random.nextInt(2) == 1) {
				temp = this.matrix[mutationIndex1][mutationIndex2];
				this.matrix[mutationIndex1][mutationIndex2] = this.matrix[mutationIndex3][mutationIndex2];
				this.matrix[mutationIndex3][mutationIndex2] = temp;
			} else {
				temp = this.matrix[mutationIndex2][mutationIndex1];
				this.matrix[mutationIndex2][mutationIndex1] = this.matrix[mutationIndex2][mutationIndex3];
				this.matrix[mutationIndex2][mutationIndex3] = temp;
			}
		}
	}

	public float calculateFitness() {
		float fitnesssRows = 0;
		float fitnessColumns = 0;
		float fitnessSquare = 0;
		// Rows and Columns
		for (int i = 0; i < 9; i++) {
			rowMap.clear();
			columnMap.clear();
			for (int j = 0; j < 9; j++) {
				rowMap.add(matrix[i][j]);
				columnMap.add(matrix[j][i]);
			}
			fitnesssRows += (float) (1.0f / (float) (9 + 1 - rowMap.size())) / 9.0f;
			fitnessColumns += (float) (1.0f / (float) (9 + 1 - columnMap.size())) / 9.0f;
		}
		// Square
		for (int l = 0; l < 3; l++) {
			for (int k = 0; k < 3; k++) {
				squareMap.clear();
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						squareMap.add(matrix[i + k * 3][j + l * 3]);
					}
				}
				fitnessSquare += (float) (1.0f / (float) (9 + 1 - squareMap.size())) / 9.0f;
			}
		}
		return currentFitness = fitnesssRows * fitnessColumns * fitnessSquare;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public float calFitness() {
		calculateFitness();
		return currentFitness;
	}

	public void copy(GraphSV g) {
		GraphSV graphSV = g;
		graphSV.length = length;
		graphSV.theMin = theMin;
		graphSV.theMax = theMax;
	}

	/*public void toStrings() {
		for (int j = 0; j < 9; j++) {
			for (int k = 0; k < 9; k++) {
				if (k == 0)
					System.out.print("{" + this.matrix[j][k] + ", ");
				else if (k == 8)
					System.out.print(this.matrix[j][k] + "}, ");
				else
					System.out.print(this.matrix[j][k] + ", ");
			}
			System.out.println();
		}

	}*/

	public GraphSV crossOver(GraphSV g) {
		GraphSV g1 = new GraphSV();
		GraphSV g2 = new GraphSV();
		g.copy(g1);
		g.copy(g2);

		if (random.nextInt(2) == 1) {
			for (int j = 0; j < 9; j++) {
				crossoverPoint = random.nextInt(8) + 1;// 1 2 3 4 5 6 7 5 9
				for (int k = 0; k < crossoverPoint; k++) {
					g1.matrix[k][j] = g.matrix[k][j];
					g2.matrix[k][j] = this.matrix[k][j];
				}
				for (int k = crossoverPoint; k < 9; k++) {
					g2.matrix[k][j] = g.matrix[k][j];
					g1.matrix[k][j] = this.matrix[k][j];
				}
			}
		} else {
			for (int j = 0; j < 9; j++) {
				crossoverPoint = random.nextInt(8) + 1;
				for (int k = 0; k < crossoverPoint; k++) {
					g1.matrix[j][k] = g.matrix[j][k];
					g2.matrix[j][k] = this.matrix[j][k];
				}
				for (int k = crossoverPoint; k < 9; k++) {
					g2.matrix[j][k] = g.matrix[j][k];
					g1.matrix[j][k] = this.matrix[j][k];
				}
			}
		}
		GraphSV res = null;
		if (random.nextInt(2) == 1) {
			res = g1;
		} else
			res = g2;

		return res;

	}
}
