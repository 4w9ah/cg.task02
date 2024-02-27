package com.cgvsu.protocurvefxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import com.cgvsu.protocurvefxapp.CurveUtils;

import java.util.ArrayList;

public class ProtoCurveController {
    final int POINT_RADIUS = 3;
    final int SPLINE_POINT_RADIUS = 1;
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
    private double STEP = 0.002;

    ArrayList<Point2D> points = new ArrayList<Point2D>();

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(canvas.getGraphicsContext2D(), event);
            }
        });
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

        graphicsContext.setLineWidth(2*SPLINE_POINT_RADIUS);

        points.add(clickPoint);
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Point2D i : points) {
            graphicsContext.fillOval(
                    i.getX() - POINT_RADIUS, i.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

        if (points.size() > 2) {
            double prevX, prevY;
            double t = STEP;
            double drawingX = points.get(0).getX();
            double drawingY = points.get(0).getY();

            while (t <= 1) {
                prevX = drawingX;
                prevY = drawingY;
                drawingX = 0;
                drawingY = 0;
                for (int i = 0; i < points.size(); i++) {
                    double bern = CurveUtils.calcBernstein(points.size()-1, i , t);
                    drawingX += bern * points.get(i).getX();
                    drawingY += bern * points.get(i).getY();
                }
                graphicsContext.strokeLine(prevX, prevY, drawingX, drawingY);
                graphicsContext.fillOval(
                        drawingX - SPLINE_POINT_RADIUS, drawingY - SPLINE_POINT_RADIUS,
                        2 * SPLINE_POINT_RADIUS, 2 * SPLINE_POINT_RADIUS);


                t += STEP;
            }
            graphicsContext.strokeLine(drawingX, drawingY, points.get(points.size()-1).getX(), points.get(points.size()-1).getY());

        }
    }
}