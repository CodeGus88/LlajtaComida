package com.appcocha.llajtacomida.view.weekdays;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.appcocha.llajtacomida.R;
import com.appcocha.llajtacomida.interfaces.SearchPlate;
import com.appcocha.llajtacomida.model.plate.Plate;
import com.appcocha.llajtacomida.model.weekdays.Day;
import com.appcocha.llajtacomida.presenter.plate.PlateNavegation;
import com.appcocha.llajtacomida.presenter.tools.Sound;
import com.appcocha.llajtacomida.presenter.weekdays.WeekDays;
import com.appcocha.llajtacomida.presenter.weekdays.WeekDaysPresenter;
import com.appcocha.llajtacomida.view.main.MainActivity;

import java.util.ArrayList;
import java.util.Date;

public class AlertShowWeekDays{

    private AlertDialog alertDialog;
    private MainActivity activity;
    private ListView lvRestaurants;
    private Button btnClose;

    public AlertShowWeekDays(MainActivity activity) {
        this.activity = activity;
        View viewAlert = activity.getLayoutInflater().inflate(R.layout.alert_show_week_days, null);
        lvRestaurants = (ListView) viewAlert.findViewById(R.id.lvRestaurants);
        btnClose = (Button) viewAlert.findViewById(R.id.btnClose);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(viewAlert);
        alertDialog = builder.create();
        alertDialog.setView(viewAlert);
        alertDialog.show();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        initAdapter();
    }

    private void initAdapter(){
        ArrayAdapterWeekDays arrayAdapterWeekDays = new ArrayAdapterWeekDays(activity, R.layout.adapter_week_days, WeekDays.getWeekDays());
        lvRestaurants.setAdapter(arrayAdapterWeekDays);
    }

    class ArrayAdapterWeekDays extends ArrayAdapter<Day> implements SearchPlate.ViewPlateFound {

        private final Context context;
        private final int resource;
        private ArrayList<Day> dayLit;
        private WeekDaysPresenter presenterPlateFound;

        public ArrayAdapterWeekDays(@NonNull Context context, int resource, @NonNull final ArrayList<Day> dayList) {
            super(context, resource, dayList);
            this.resource = resource;
            this.context = context;
            this.dayLit = new ArrayList<Day>();
            this.dayLit.addAll(dayList);
            this.presenterPlateFound = new WeekDaysPresenter(ArrayAdapterWeekDays.this);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(resource, null);
            }
            TextView tvDay = (TextView) convertView.findViewById(R.id.tvDay);
            LinearLayout llPlateList = (LinearLayout) convertView.findViewById(R.id.llPlateList);
            llPlateList.removeAllViews();
            llPlateList.setBackgroundResource(R.drawable.panel_white);

            if(dayLit.get(position).getDayName().equals("monday")) tvDay.setText(context.getString(R.string.day_monday));
            else if(dayLit.get(position).getDayName().equals("tuesday")) tvDay.setText(context.getString(R.string.day_tuesday));
            else if(dayLit.get(position).getDayName().equals("wednesday")) tvDay.setText(context.getString(R.string.day_wednesday));
            else if(dayLit.get(position).getDayName().equals("thursday")) tvDay.setText(context.getString(R.string.day_thursday));
            else if(dayLit.get(position).getDayName().equals("friday")) tvDay.setText(context.getString(R.string.day_friday));
            else if(dayLit.get(position).getDayName().equals("saturday")) tvDay.setText(context.getString(R.string.day_saturday));
            else if(dayLit.get(position).getDayName().equals("sunday")) tvDay.setText(context.getString(R.string.day_sunday));

            ArrayList<String> day = dayLit.get(position).getPlateListName();
            for(String plateName: day){
                TextView textView = new TextView(context);
                textView.setText(plateName);
                textView.setClickable(true);
                textView.setPadding(5, 5, 5, 6);
                llPlateList.addView(textView);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Sound.playMagic();
                        presenterPlateFound.searchPlateName(textView.getText().toString());
                    }
                });
            }
            Date date = new Date();
            int d = date.getDay();
            if(d==0) d = 7; // en java domingo es el número 0
            if(d==position+1) tvDay.setTextColor(context.getColor(R.color.colorAccent));
//            else tvDay.setTextColor(Color.DKGRAY);
            else tvDay.setTextColor(context.getColor(R.color.colorPromotion));;
            return convertView;
        }

        @Override
        public void showPlateList(ArrayList<Plate> plateList) {
            if(plateList!=null){
                if(plateList.size()>0){
                    PlateNavegation.showPlateView(context, plateList.get(0));
                    presenterPlateFound.stopRealtimeDatabase();
                    alertDialog.dismiss();
                }else Toast.makeText(context, context.getString(R.string.message_not_found), Toast.LENGTH_LONG).show();
            }else Toast.makeText(context, context.getString(R.string.message_not_found), Toast.LENGTH_LONG).show();
        }
    }
}
