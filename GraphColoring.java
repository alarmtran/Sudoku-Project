package com.example.vertex1;

import java.util.ArrayList;

class GraphColoring {
    private int[][] graphMatrix;
    private ArrayList<Vertex> listVertex;



    public GraphColoring(int[][] graphMatrix, ArrayList<Vertex> listVertex) {
        this.graphMatrix = graphMatrix;
        this.listVertex = listVertex;
    }

    public ArrayList<Vertex> getListVertex() {
        return listVertex;
    }

    public int[][] getGraphMatrix() {
        return graphMatrix;
    }

    public void calculateVertexDegree() {
        for (int i = 0; i < graphMatrix.length; i++) {
            listVertex.get(i).setDeg(getNeighbors(listVertex.get(i)).size());
        }
    }
    public void colorGraph() {
        ArrayList<Vertex> vertices = new ArrayList<>(listVertex);

        mergeSortVertices(vertices);

        for (Vertex v : vertices) {
            if (v.getColor() == null) {
                String color = v.getListColorAvailable().get(0);
                ArrayList<Vertex> neighbors = getNeighbors(v);

                for (Vertex neighbor : neighbors) {
                    if (neighbor.getColor() == null) {
                        neighbor.getListColorAvailable().remove(color);
                    }
                }
                v.setColor(color);
                System.out.println("Assigned color " + color + " to vertex " + v.getId());
            }
        }
    }

    private ArrayList<Vertex> getNeighbors(Vertex vertex) {
        ArrayList<Vertex> neighbors = new ArrayList<>();
        int vertexIndex = vertex.getId();

        for (int i = 0; i < graphMatrix.length; i++) {
            if (graphMatrix[i][vertexIndex] == 1) {
                neighbors.add(listVertex.get(i));
            }
        }
        return neighbors;
    }
    private void mergeSortVertices(ArrayList<Vertex> vertices) {
        if (vertices.size() > 1) {
            int mid = vertices.size() / 2;
            ArrayList<Vertex> leftHalf = new ArrayList<>(vertices.subList(0, mid));
            ArrayList<Vertex> rightHalf = new ArrayList<>(vertices.subList(mid, vertices.size()));

            mergeSortVertices(leftHalf);
            mergeSortVertices(rightHalf);

            merge(vertices, leftHalf, rightHalf);
        }
    }

    private void merge(ArrayList<Vertex> vertices, ArrayList<Vertex> left, ArrayList<Vertex> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getDeg() >= right.get(j).getDeg()) {
                vertices.set(k, left.get(i));
                i++;
            } else {
                vertices.set(k, right.get(j));
                j++;
            }
            k++;
        }

        while (i < left.size()) {
            vertices.set(k, left.get(i));
            i++;
            k++;
        }

        while (j < right.size()) {
            vertices.set(k, right.get(j));
            j++;
            k++;
        }
    }
}
