package org.example.disksheduler;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
                "FCFS - First Come First Serve",
                "SSTF - Shortest Seek Time First",
                "SCAN - Elevator",
                "C-SCAN - Circular SCAN",
                "LOOK",
                "C-LOOK - Circular LOOK"
        ));
        algorithmComboBox.getSelectionModel().selectFirst();
        grid.add(algorithmLabel, 0, 2);
        grid.add(algorithmComboBox, 1, 2);

        // Calculate button
        Button calculateButton = new Button("Calculate");
        calculateButton.setOnAction(e -> calculateSeekOperations());
        grid.add(calculateButton, 1, 3);

        // Result label
        resultLabel = new Label();
        grid.add(resultLabel, 0, 4, 2, 1);

        // Chart setup
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Sequence");
        yAxis.setLabel("Position");

        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Disk Movement");
        chart.setPrefHeight(400);
        chart.setPrefWidth(600);
        grid.add(chart, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 650, 600);
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
                case "FCFS - First Come First Serve":
                    result = scheduler.FCFS(headPosition, requests);
                    break;
                case "SSTF - Shortest Seek Time First":
                    result = scheduler.SSTF(headPosition, requests);
                    break;
                case "SCAN - Elevator":
                    result = scheduler.SCAN(headPosition, requests);
                    break;
                case "C-SCAN - Circular SCAN":
                    result = scheduler.C_SCAN(headPosition, requests);
                    break;
                case "LOOK":
                    result = scheduler.LOOK(headPosition, requests);
                    break;
                case "C-LOOK - Circular LOOK":
                    result = scheduler.C_LOOK(headPosition, requests);
                    break;
                default:
                    result = scheduler.FCFS(headPosition, requests);
            }

            // Update result label
            resultLabel.setText("Total number of seek operations = " + result.getTotalSeekTime());

            // Update chart
            updateChart(result.getSeekSequence());

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
        series.setName("Head Movement");

        for (int i = 0; i < sequence.length; i++) {
            series.getData().add(new XYChart.Data<>(i, sequence[i]));
        }

        chart.getData().add(series);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
