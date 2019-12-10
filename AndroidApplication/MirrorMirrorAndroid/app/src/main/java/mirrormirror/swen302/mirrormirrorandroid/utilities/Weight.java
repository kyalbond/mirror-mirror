package mirrormirror.swen302.mirrormirrorandroid.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by glewsimo on 21/09/17.
 */

public class Weight {
    private double weight;
    private Date date;

    public Weight(double weight, Date date){
        this.weight = weight;
        this.date = date;
    }

    public double getWeight(){
        return this.weight;
    }

    public Date getDate(){
        return this.date;
    }

    public static List<Weight> parseWeights(JSONArray jsonArray){
        List<Weight> weights = new ArrayList<Weight>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                String date = object.getString("date");
                String weight = object.getString("weight");
                weights.add(new Weight(Double.parseDouble(weight), DateTimeManager.getDateFromString(date)));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return weights;
    }

}
