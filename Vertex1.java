package com.example.vertex1;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Vertex1 {
    @FXML
    private Canvas graphCanvas;
    @FXML
    private Button colorButton;
    @FXML
    private TextArea matrixInput;
    @FXML
    private Label infoLabel;

    private GraphColoring graphColoring;

    @FXML
    private void initialize() {
        // Ví dụ: Thiết lập giá trị mặc định cho TextField hoặc các thành phần khác
        matrixInput.setText("0,1,1,0,1\n" +
                "1,0,0,0,0,\n" +
                "1,0,0,0,1,\n" +
                "0,0,0,0,1,\n" +
                "1,0,1,1,0");
    }

    @FXML
    private void onColorButtonClick() {
        int[][] graphMatrix = parseMatrixInput(matrixInput.getText());
        if (graphMatrix != null) {
            ArrayList<Vertex> vertices = createVertices(graphMatrix.length);
            graphColoring = new GraphColoring(graphMatrix, vertices);
            graphColoring.calculateVertexDegree();
            graphColoring.colorGraph();
            updateInfoLabel();
            drawGraph();
        } else {
            // Xử lý lỗi khi ma trận không hợp lệ
            infoLabel.setText("Lỗi: Ma trận không hợp lệ.");
        }
    }

    private int[][] parseMatrixInput(String input) {
        String[] rows = input.split("\n");
        int[][] matrix = new int[rows.length][];

        try {
            for (int i = 0; i < rows.length; i++) {
                String sanitizedRow = rows[i].replaceAll("\\n", "");
                String[] values = sanitizedRow.split(",");
                matrix[i] = new int[values.length];
                for (int j = 0; j < values.length; j++) {
                    matrix[i][j] = Integer.parseInt(values[j].trim());
                }
            }
            return matrix;
        } catch (NumberFormatException e) {
            // Xử lý ngoại lệ NumberFormatException khi giá trị không phải là số
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Vertex> createVertices(int size) {
        ArrayList<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            vertices.add(new Vertex(i));
        }
        return vertices;
    }

    private void updateInfoLabel() {
        StringBuilder info = new StringBuilder();
        for (Vertex v : graphColoring.getListVertex()) {
            info.append("Đỉnh ").append(v.getId()).append(": Bậc = ")
                    .append(v.getDeg()).append(", Màu = ").append(v.getColor()).append("\n");
        }
        infoLabel.setText(info.toString());
    }

    private void drawGraph() {
        GraphicsContext gc = graphCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, graphCanvas.getWidth(), graphCanvas.getHeight());

        calculateVertexPositions(graphCanvas.getWidth(), graphCanvas.getHeight());

        // Truy cập graphMatrix thông qua graphColoring
        int[][] matrix = graphColoring.getGraphMatrix();

        // Vẽ cạnh
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix[i].length; j++) { // Thay đổi vòng lặp để chỉ vẽ một lần cho mỗi cạnh
                if (matrix[i][j] == 1) {
                    if (i < graphColoring.getListVertex().size() && j < graphColoring.getListVertex().size()) {
                        Vertex v1 = graphColoring.getListVertex().get(i);
                        Vertex v2 = graphColoring.getListVertex().get(j);
                        gc.strokeLine(v1.getX(), v1.getY(), v2.getX(), v2.getY());
                    }
                }
            }
        }

        // Vẽ đỉnh
        for (Vertex v : graphColoring.getListVertex()) {
            gc.setFill(Color.web(v.getColor()));
            gc.fillOval(v.getX() - 10, v.getY() - 10, 20, 20); // Vẽ đỉnh dạng hình tròn
            gc.setStroke(Color.BLACK);
            gc.strokeOval(v.getX() - 10, v.getY() - 10, 20, 20);
            gc.fillText("Đỉnh " + v.getId(), v.getX() - 10, v.getY() - 20); // Thêm nhãn cho đỉnh
        }
    }

    private void calculateVertexPositions(double canvasWidth, double canvasHeight) {
        ArrayList<Vertex> vertices = graphColoring.getListVertex();
        int size = vertices.size();
        double centerX = canvasWidth / 2;
        double centerY = canvasHeight / 2;
        double radius = Math.min(canvasWidth, canvasHeight) / 3;

        for (int i = 0; i < size; i++) {
            double angle = 2 * Math.PI * i / size;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            vertices.get(i).setPosition(x, y);
        }
    }
}
