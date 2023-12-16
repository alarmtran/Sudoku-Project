package group11.ColoringGraph;

import group11.Sudoku.SystemSolveSudoku.Graph;

import java.util.Scanner;

public class TestGraph {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Graph Coloring Test\n");

        GraphColoring gc = new GraphColoring();

        System.out.println("Enter number of vertices \n");
        int V = sc.nextInt();

        System.out.println("Enter matrix:");
        int[][] graph = new int[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                graph[i][j] = sc.nextInt();
            }
        }
        System.out.println("Enter number of colors");
        int c = sc.nextInt();

        gc.graphColor(graph, c);
    }
}
