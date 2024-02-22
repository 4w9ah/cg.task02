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


        points.add(clickPoint);
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Point2D i : points) {
            graphicsContext.fillOval(
                    i.getX() - POINT_RADIUS, i.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

        if (points.size() > 2) {
            double t = 0;

            while (t <= 1) {
                double drawingX = 0;
                double drawingY = 0;
                for (int i = 0; i < points.size(); i++) {
                    double bern = CurveUtils.calcBernstein(points.size()-1, i , t);
                    drawingX += bern * points.get(i).getX();
                    drawingY += bern * points.get(i).getY();
                }
                graphicsContext.fillOval(
                        drawingX - SPLINE_POINT_RADIUS, drawingY - SPLINE_POINT_RADIUS,
                        2 * SPLINE_POINT_RADIUS, 2 * SPLINE_POINT_RADIUS);


                t += STEP;
            }

        }


//        if (points.size() > 0) {
//            final Point2D lastPoint = points.get(points.size() - 1);
//            graphicsContext.strokeLine(lastPoint.getX(), lastPoint.getY(), clickPoint.getX(), clickPoint.getY());
//        }

    }
}