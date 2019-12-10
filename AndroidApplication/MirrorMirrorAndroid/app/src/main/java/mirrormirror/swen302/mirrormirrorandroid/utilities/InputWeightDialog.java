package mirrormirror.swen302.mirrormirrorandroid.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.shawnlin.numberpicker.NumberPicker;

import java.text.DecimalFormat;


import mirrormirror.swen302.mirrormirrorandroid.R;


/**
 * Created by hayandr1 on 29/08/17.
 */

public class InputWeightDialog extends AlertDialog implements View.OnClickListener{
    private static final int maxWeight = 200;
    private static final double subKGStepping = 0.1;
    private static final DecimalFormat dF = new DecimalFormat(".##");

    private static NumberPicker leftPicker;
    private static NumberPicker rightPicker;

    public InputWeightDialog(Context context){
        super(context);
    }

    void populateSpinnerAdapters(){
        leftPicker = (NumberPicker) findViewById(R.id.weight_picker_left);
        rightPicker = (NumberPicker) findViewById(R.id.weight_picker_right);
        leftPicker.setMaxValue(maxWeight);
        leftPicker.setMinValue(1);
        rightPicker.setMinValue(0);
        rightPicker.setMaxValue(9);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_weight_popup);

        populateSpinnerAdapters();
        Button cancel = (Button) findViewById(R.id.cancel_button);
        Button submit = (Button) findViewById(R.id.submit_button);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cancel_button){
            cancel();
        } else if(v.getId() == R.id.submit_button){
            float weightVal = leftPicker.getValue();
            weightVal += (rightPicker.getValue())/((int)(1/subKGStepping));
            ServerController.sendWeight(weightVal, DateTimeManager.getDatetimeAsString(), getContext());
            cancel();
        }
    }
}
