package mirrormirror.swen302.mirrormirrorandroid.utilities;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import mirrormirror.swen302.mirrormirrorandroid.R;

/**
 * Created by bondkyal on 28/09/17.
 */

public class WeightPopupDialog extends AlertDialog {

    private TextView weightTextView;
    private TextView bmiTextView;
    private Double weight;
    private Double bmi;

    public WeightPopupDialog(@NonNull Context context, Double w, Double b) {
        super(context);
        this.weight = roundWeight(w);
        this.bmi = roundWeight(b);
    }

    public void updateValues(Double w, Double b){
        this.weight = roundWeight(w);
        this.weightTextView.setText(this.weight + " kg");
        this.bmi = roundWeight(b);
        this.bmiTextView.setText(this.bmi+"");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_popup);

        weightTextView = (TextView) findViewById(R.id.weight_label);
        weightTextView.setText(this.weight + " kg");
        bmiTextView = (TextView) findViewById(R.id.bmi_label);
        bmiTextView.setText(this.bmi+"");
    }

    public static Double roundWeight(Double w){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(w));
    }

}
