package com.employeeroster.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.employeeroster.data.repository.AuthRepository;
import com.employeeroster.util.Result;

public class LoginViewModel extends ViewModel {
    private final AuthRepository authRepository = new AuthRepository();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> userRole = new MutableLiveData<>();

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getUserRole() {
        return userRole;
    }

    public void login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            errorMessage.setValue("Please fill out all fields");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.setValue("Please enter a valid email");
            return;
        }

        loading.setValue(true);
        authRepository.login(email.trim(), password, result -> {
            loading.postValue(false);
            if (result.isSuccess()) {
                userRole.postValue(result.getData());
            } else {
                errorMessage.postValue(result.getErrorMessage() != null ? result.getErrorMessage() : "Login failed");
            }
        });
    }
}
