package com.example.driveeaplication.viewmodels;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.driveeaplication.models.User;

public class MainViewModel extends ViewModel {
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public void init() {
        User user = new User("Иван", 500);
        userLiveData.setValue(user);
    }
    public MutableLiveData<User> getUserData() {
        return userLiveData;
    }
    public boolean processTrip(int price, boolean usePoints, boolean isHighDemand, boolean isDriver) {
        User user = userLiveData.getValue();
        if (user == null) return false;
        if (usePoints) {
            if (user.getPoints() >= 50) {
                user.spendPoints(50);
                userLiveData.setValue(user);
                return true;
            } else {
                return false;
            }
        } else {
            if (price > 0) {
                int earned = user.calculateTripReward(price, isHighDemand, isDriver);
                user.addPoints(earned);
                userLiveData.setValue(user);
                return true;
            }
            return false;
        }
    }

}
