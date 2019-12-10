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

public class InputPeakFlowDialog extends AlertDialog implements View.OnClickListener{
    private static final int maxPF = 800;
    private static final int minPF = 50;

    private static NumberPicker peakFlowPicker;

    public InputPeakFlowDialog(Context context){
        super(context);
    }

    void populateSpinnerAdapters(){
        peakFlowPicker = (NumberPicker) findViewById(R.id.peak_flow_picker);
        peakFlowPicker.setMinValue(minPF);
        peakFlowPicker.setMaxValue(maxPF);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_peak_flow_popup);

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
            int peakFlowVal = peakFlowPicker.getValue();
            ServerController.sendPeakFlow(peakFlowVal, DateTimeManager.getDatetimeAsString(), getContext());
            cancel();
        }
    }
}
