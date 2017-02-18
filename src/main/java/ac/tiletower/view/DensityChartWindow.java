package ac.tiletower.view;

import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Created by asilkaratas on 12/5/16.
 */
public class DensityChartWindow extends VBox {

    public DensityChartWindow(List<Double> densitySeries) {
        setAlignment(Pos.TOP_CENTER);
        setSpacing(10);

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Step");
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Density Chart");
        lineChart.setCreateSymbols(false);
        XYChart.Series series = new XYChart.Series();
        series.setName("Density");
        int step = 1;
        for(Double density : densitySeries) {
            series.getData().add(new XYChart.Data(step, density));
            step++;
        }
        lineChart.getData().add(series);

        getChildren().add(lineChart);
    }
}
