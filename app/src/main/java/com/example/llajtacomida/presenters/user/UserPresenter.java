package com.example.llajtacomida.presenters.user;

import android.util.Log;
import android.widget.Toast;

import com.example.llajtacomida.interfaces.UserInterface;
import com.example.llajtacomida.models.user.User;
import com.example.llajtacomida.models.user.UserModel;

public class UserPresenter implements UserInterface.PresenterInterface {

    private UserInterface.ViewInterface viewInterface;
    private UserInterface.ModelInterface modelInterface;

    public UserPresenter(UserInterface.ViewInterface viewInterface){
        this.viewInterface =viewInterface;
        this.modelInterface = new UserModel(this);
    }

    /**
     * Este contructor se usará para el guardado de usuaio al iniciar sesion desde otro presentador (SesionPresenter)
     * Solo se accederá al método storeUser
     */
    public UserPresenter(){
        viewInterface = null;
        this.modelInterface = new UserModel(this);
    }

    @Override
    public void showUser(User user) {
        if(viewInterface != null){
            viewInterface.showUser(user);
        }else{
            Log.e("Error", "View is null");
        }
    }

    @Override
    public void findUser(String id) {
        modelInterface.findUser(id);
    }

    @Override
    public void storeUser(User user) {
        modelInterface.storeUser(user);
    }
}
