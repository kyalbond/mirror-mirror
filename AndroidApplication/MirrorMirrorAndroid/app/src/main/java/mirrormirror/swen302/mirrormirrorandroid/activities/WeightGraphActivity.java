package mirrormirror.swen302.mirrormirrorandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.joda.time.Days;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import mirrormirror.swen302.mirrormirrorandroid.R;
import mirrormirror.swen302.mirrormirrorandroid.utilities.ServerController;
import mirrormirror.swen302.mirrormirrorandroid.utilities.Weight;

/**
 * Created by glewsimo on 7/09/17.
 */

public class WeightGraphActivity extends AppCompatActivity {

    private int numDays = 0;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    private double maxWeight = Double.MIN_VALUE;
    private double minWeight = Double.MAX_VALUE;
    private Button graphButton;

    private DataPoint[] datapoints;
    private List<Weight> weights;

    private GRAPHTYPE graphtype = GRAPHTYPE.BAR;

    enum GRAPHTYPE{
        BAR, LINE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_graph);

        Intent intent = getIntent();
        numDays = intent.getIntExtra("numDays", 0);
        TextView title = (TextView)findViewById(R.id.weight_graph_title);
        graphButton = (Button) findViewById(R.id.graph_switch_button);
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(graphtype == GRAPHTYPE.BAR){
                    graphButton.setText("Bar Chart");
                    graphtype = GRAPHTYPE.LINE;
                }
                else if (graphtype == GRAPHTYPE.LINE){
                    graphButton.setText("Line Chart");

                    graphtype = GRAPHTYPE.BAR;
                }
                makeGraph();
            }
        });
        title.setText(intent.getStringExtra("title"));

        ServerController.setSocketWeightListener(this);
        ServerController.sendWeightsRequest(this, numDays);
    }

    public void parseWeights(List<Weight> weights){
        this.weights = weights;
        this.datapoints = generateData(weights);
        makeGraph();
    }

    public void makeGraph(){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMinX(datapoints[0].getX());
        graph.getViewport().setMaxX(7);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxWeight + (maxWeight/5));

        graph.getViewport().setScrollable(true);

        graph.getViewport().setScalableY(false);
        graph.getViewport().setScrollableY(false);
        graph.getViewport().setScalable(false);


        Series series = null;
        if(graphtype == GRAPHTYPE.LINE){
            LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<DataPoint>(datapoints);
            lineSeries.setColor(getResources().getColor(R.color.colorAccent));
            lineSeries.setThickness(7);
            lineSeries.setDrawDataPoints(true);
            lineSeries.setDataPointsRadius(13);

            series = lineSeries;
        }
        else{
            BarGraphSeries <DataPoint> barSeries = new BarGraphSeries<>(datapoints);
            barSeries.setSpacing(10);
            barSeries.setColor(getResources().getColor(R.color.colorAccent));
            series = barSeries;
        }

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, (int)(-(dataPoint.getX())));
                Toast.makeText(WeightGraphActivity.this, "On " + format.format(cal.getTime()) + ", you weighed: " +
                        dataPoint.getY() + "kgs", Toast.LENGTH_SHORT).show();
            }
        });
        graph.removeAllSeries();
        graph.addSeries(series);
    }

    public DataPoint[] generateData(List<Weight> weights) {
        DecimalFormat df = new DecimalFormat("#.#");

        DataPoint[] dataPoints = new DataPoint[weights.size()];
        for(int i = 0; i < weights.size(); i ++) {
            double weight = weights.get(i).getWeight();
            long timeDiff = new Date().getTime() - weights.get(i).getDate().getTime();
            double dayDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

            if(weight > maxWeight) maxWeight = weight;
            if(weight < minWeight) minWeight = weight;

            System.out.println(dayDiff);
            if(dayDiff < 0){
                int z = 1;
//                System.out.println("Simon");
            }


            DataPoint d = new DataPoint(dayDiff, Double.parseDouble(df.format(weight)));
            dataPoints[i] = d;
        }
        return dataPoints;
    }

}
