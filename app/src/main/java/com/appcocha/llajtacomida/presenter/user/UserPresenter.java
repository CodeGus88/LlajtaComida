package com.appcocha.llajtacomida.presenter.user;

import android.util.Log;
import com.appcocha.llajtacomida.interfaces.UserInterface;
import com.appcocha.llajtacomida.model.user.User;
import com.appcocha.llajtacomida.model.user.UserModel;

import java.util.ArrayList;

/**
 * Presentador, usuario
 */
public class UserPresenter implements UserInterface.PresenterUser {

    private UserInterface.ViewUser viewUser;
    private UserInterface.ModelUser modelUser;

    /**
     * Constructor, inicializa viewUser
     * @param viewUser
     */
    public UserPresenter(UserInterface.ViewUser viewUser){
        this.viewUser = viewUser;
        this.modelUser = new UserModel(this);
    }

    /**
     * Este contructor se usará para el guardado de usuaio al iniciar sesion desde otro presentador (SesionPresenter)
     * Solo se accederá al método storeUser
     */
    public UserPresenter(){
        viewUser = null;
        this.modelUser = new UserModel(this);
    }

    @Override
    public void showUser(User user) {
        if(viewUser != null){
            viewUser.showUser(user);
        }else{
            Log.e("Error", "View is null");
        }
    }

    @Override
    public void findUser(String id) {
        modelUser.findUser(id);
    }

    @Override
    public void storeUser(User user) {
        modelUser.storeUser(user);
    }

    @Override
    public void stopRealtimeDatabase() {
        modelUser.stopRealtimeDatabase();
    }

    @Override
    public void showUserList(ArrayList<User> userList) {
        viewUser.showUserList(userList);
    }

    @Override
    public void loadUserList() {
        modelUser.loadUserList();
    }
}
