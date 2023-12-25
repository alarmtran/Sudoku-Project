package com.example.vertex1;

import java.util.ArrayList;

public class Vertex {
    private int id;
    private int deg;
    private String color;
    private ArrayList<String> listColorAvailable;

    private double x, y; // Tọa độ x và y của đỉnh

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public Vertex(int id) {
        this.id = id;
        this.deg = 0;
        this.color = null;
        this.listColorAvailable = new ArrayList<>();
        initializeColorList();
    }

    public int getId() {
        return id;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void initializeColorList() {
        listColorAvailable.add("Red");
        listColorAvailable.add("Green");
        listColorAvailable.add("Blue");
        listColorAvailable.add("Yellow");
    }

    public ArrayList<String> getListColorAvailable() {
        return listColorAvailable;
    }

    public static void main(String[] args) {
        int[][] adjacencyMatrix = {
                {0, 1, 0, 1, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 1, 0, 0, 0, 0},
                {1, 1, 1, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 1, 1, 0},
                {0, 0, 0, 1, 0, 1, 0, 1, 1},
                {0, 0, 0, 0, 0, 1, 1, 0, 0},
                {1, 0, 0, 1, 0, 0, 1, 0, 0}
        };

        ArrayList<Vertex> listVertex = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            listVertex.add(new Vertex(i));
        }

        GraphColoring graphColoring = new GraphColoring(adjacencyMatrix, listVertex);
        graphColoring.calculateVertexDegree();
        graphColoring.colorGraph();

        for (Vertex v : listVertex) {
            System.out.println("Vertex " + v.getId() + ": Degree = " + v.getDeg() + ", Color = " + v.getColor());
        }
    }
}
