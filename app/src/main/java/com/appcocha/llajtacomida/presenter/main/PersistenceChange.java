package com.appcocha.llajtacomida.presenter.main;

import com.appcocha.llajtacomida.model.main.PersistenceSettings;

public class PersistenceChange {

    private PersistenceSettings persistenceSettings;

    public PersistenceChange(boolean value){
        persistenceSettings = new PersistenceSettings(value);
    }
}
