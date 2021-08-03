package com.appcocha.llajtacomida.presenter.weekdays;

import com.appcocha.llajtacomida.model.weekdays.Day;

import java.util.ArrayList;

public class WeekDays {
    /**
     * Retorna las comidas según el día
     * @return week
     */
    public static ArrayList<Day> getWeekDays(){

        ArrayList<Day> week = new ArrayList<Day>();

        Day day1 = new Day("monday");
        day1.addPlateName("Chorizo Criollo");
        day1.addPlateName("Escabeche de Patas");
        day1.addPlateName("Enrrollado de Cerdo");

        Day day2 = new Day("tuesday");
        day2.addPlateName("Ranga Colorada (Kasauchu)");
        day2.addPlateName("Relleno de Papa");
        day2.addPlateName("Chanq'a de Pollo");

        Day day3 = new Day("wednesday");
        day3.addPlateName("Patas Uchu");
        day3.addPlateName("Riñon al Caldo");
        day3.addPlateName("Locoto Relleno");
        day3.addPlateName("Sillpancho (Sillpanchu)");
        day3.addPlateName("Puchero");

        Day day4 = new Day("thursday");
        day4.addPlateName("Picante Mixto");
        day4.addPlateName("Fideos Uchu");
        day4.addPlateName("Lapping");
        day4.addPlateName("Trancapecho");

        Day day5 = new Day("friday");
        day5.addPlateName("Planchitas");
        day5.addPlateName("K'allu");
        day5.addPlateName("Lechón al Horno");
        day5.addPlateName("Pique Macho");

        Day day6 = new Day("saturday");
        day6.addPlateName("Lomo Borracho");
        day6.addPlateName("Jawri Uchu");
        day6.addPlateName("Conejo Lambreado");
        day6.addPlateName("Pejtu de Habas");

        Day day7 = new Day("sunday");
        day7.addPlateName("Sopa de Maní");
        day7.addPlateName("Chicharrón");
        day7.addPlateName("Pampaku");

        week.add(day1);
        week.add(day2);
        week.add(day3);
        week.add(day4);
        week.add(day5);
        week.add(day6);
        week.add(day7);

        return week;
    }
}
