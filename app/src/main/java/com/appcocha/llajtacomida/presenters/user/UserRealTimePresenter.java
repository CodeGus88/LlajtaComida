package com.appcocha.llajtacomida.presenters.user;

import com.appcocha.llajtacomida.interfaces.UserInterface;
import com.appcocha.llajtacomida.models.user.User;
import com.appcocha.llajtacomida.models.user.UserRealTimeModel;

public class UserRealTimePresenter implements UserInterface.PresenterUserRealTime{

    private UserInterface.ViewUserRealTime viewUserRealTime;
    UserInterface.ModelUserRealTime modelUserRealTime;

    public UserRealTimePresenter(UserInterface.ViewUserRealTime viewUserRealTime){
        this.viewUserRealTime = viewUserRealTime;
        modelUserRealTime = new UserRealTimeModel(this);
    }


    @Override
    public void showUserRT(User user) {
        if(user != null) viewUserRealTime.showUserRT(user);
        else viewUserRealTime.showReport("User not found");
    }

    @Override
    public void findUser(String userId) {
        if(!userId.isEmpty())modelUserRealTime.findUser(userId);
        else viewUserRealTime.showReport("Invalid user");
    }

    @Override
    public void stopRealtimeDatabase() {
        modelUserRealTime.stopRealtimeDatabase();
    }
}
