package org.example.disksheduler;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DiskSchedulingApp extends Application {

    private TextField headPositionField;
    private TextField requestsField;
    private ComboBox<String> algorithmComboBox;
    private Label resultLabel;
    private LineChart<Number, Number> chart;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Disk Scheduling Algorithms");

        // Create UI components
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Head position input
        Label headPositionLabel = new Label("Head Position:");
        headPositionField = new TextField();
        grid.add(headPositionLabel, 0, 0);
        grid.add(headPositionField, 1, 0);

        // Requests input
        Label requestsLabel = new Label("Requests (comma separated):");
        requestsField = new TextField();
        grid.add(requestsLabel, 0, 1);
        grid.add(requestsField, 1, 1);

        // Algorithm selection
        Label algorithmLabel = new Label("Select Algorithm:");
        algorithmComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "FCFS",
                "SSTF",
                "SCAN",
                "C-SCAN",
                "C-LOOK"
        ));
        algorithmComboBox.getSelectionModel().selectFirst();
        grid.add(algorithmLabel, 0, 2);
        grid.add(algorithmComboBox, 1, 2);

        // Calculate button
        Button calculateButton = new Button("Calculate");
        calculateButton.setOnAction(e -> calculateSeekOperations());
        grid.add(calculateButton, 1, 3);

        // Add Comparison button
        Button comparisonButton = new Button("Comparison");
        comparisonButton.setOnAction(e -> showComparisonGraph());
        grid.add(comparisonButton, 2, 4); // Changed column from 1 to 2

        // Result label
        resultLabel = new Label();
        grid.add(resultLabel, 0, 4, 2, 1);

        // Chart setup
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelRotation(-90);
        yAxis.setTickLabelRotation(-90);

        xAxis.setLabel("Sequence");  // X axis is now Sequence
        yAxis.setLabel("Disk Position"); // Y axis is now Disk Position

        chart = new LineChart<>(xAxis, yAxis);
        chart.setPrefHeight(1000);
        chart.setPrefWidth(600);
        chart.setRotate(90);
        chart.setLegendVisible(false); // Hide legend

        grid.add(chart, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 650, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculateSeekOperations() {
        try {
            int headPosition = Integer.parseInt(headPositionField.getText().trim());
            String[] requestsStr = requestsField.getText().split(",");
            int[] requests = new int[requestsStr.length];

            for (int i = 0; i < requestsStr.length; i++) {
                requests[i] = Integer.parseInt(requestsStr[i].trim());
            }

            DiskScheduler scheduler = new DiskScheduler();
            String algorithm = algorithmComboBox.getValue();

            DiskScheduleResult result;

            switch (algorithm) {

                case "FCFS":{
                    result = scheduler.FCFS(headPosition, requests);
                    break;
                }
                case "SSTF":
                    result = scheduler.SSTF(headPosition, requests);
                    break;
                case "SCAN":
                    result = scheduler.SCAN(headPosition, requests);
                    break;
                case "C-SCAN":
                    result = scheduler.C_SCAN(headPosition, requests);
                    break;
                case "C-LOOK":
                    result = scheduler.C_LOOK(headPosition, requests);
                    break;
                default:
                    result = scheduler.FCFS(headPosition, requests);
            }

            // Update result label
            resultLabel.setText("Total number of seek operations = " + result.getTotalSeekTime());

            // Update chart
            updateChart(result.getSeekSequence());

            for(int i:result.getSeekSequence()){
                System.out.print(i+"   ");
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid numbers!");
            alert.showAndWait();
        }
    }

    private void updateChart(int[] sequence) {
        chart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.setName("Head Movement");

        for (int i = 0; i < sequence.length; i++) {
            // Use index as X (Sequence) and sequence value as Y (Disk Position)
            series.getData().add(new XYChart.Data<>(i, sequence[i]));
        }

        chart.getData().add(series);
    }

    private void showComparisonGraph() {
        try {
            int headPosition = Integer.parseInt(headPositionField.getText().trim());
            String[] requestsStr = requestsField.getText().split(",");
            int[] requests = new int[requestsStr.length];

            for (int i = 0; i < requestsStr.length; i++) {
                requests[i] = Integer.parseInt(requestsStr[i].trim());
            }

            DiskScheduler scheduler = new DiskScheduler();

            // Calculate total seek operations for all algorithms
            int fcfsSeek = scheduler.FCFS(headPosition, requests).getTotalSeekTime();
            int sstfSeek = scheduler.SSTF(headPosition, requests).getTotalSeekTime();
            int scanSeek = scheduler.SCAN(headPosition, requests).getTotalSeekTime();
            int cscanSeek = scheduler.C_SCAN(headPosition, requests).getTotalSeekTime();
            int clookSeek = scheduler.C_LOOK(headPosition, requests).getTotalSeekTime();

            // Create a new chart for comparison
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Algorithm");
            yAxis.setLabel("Total Seek Operations");

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Comparison of Disk Scheduling Algorithms");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Seek Operations");
            series.getData().add(new XYChart.Data<>("FCFS", fcfsSeek));
            series.getData().add(new XYChart.Data<>("SSTF", sstfSeek));
            series.getData().add(new XYChart.Data<>("SCAN", scanSeek));
            series.getData().add(new XYChart.Data<>("C-SCAN", cscanSeek));
            series.getData().add(new XYChart.Data<>("C-LOOK", clookSeek));

            barChart.getData().add(series);

            // Create a new stage to display the bar chart
            Stage comparisonStage = new Stage();
            comparisonStage.setTitle("Comparison Bar Chart");
            Scene scene = new Scene(barChart, 800, 600);
            comparisonStage.setScene(scene);
            comparisonStage.show();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid numbers!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
