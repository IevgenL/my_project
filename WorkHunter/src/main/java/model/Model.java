package model;

import view.View;
import vo.Vacancy;

import java.util.ArrayList;
import java.util.List;


public class Model {
    private View view;
    private Provider[] providers;

    public Model(View view, Provider[] providers) {

        if ( providers.length == 0 || providers == null || view == null ){

            throw new IllegalArgumentException();
        }
        else {
            this.providers = providers;
            this.view = view;
        }


    }

    public void selectCity(String city){
        List<Vacancy> list =new ArrayList<>();
        for (int i = 0; i < providers.length; i++) {
            list.addAll(providers[i].getJavaVacancies(city));



        }
        view.update(list);
    }
}
