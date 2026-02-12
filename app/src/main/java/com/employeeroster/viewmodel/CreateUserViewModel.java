package com.employeeroster.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.employeeroster.data.repository.UserRepository;

public class CreateUserViewModel extends ViewModel {
    private final UserRepository userRepository = new UserRepository();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public void createUser(String firstName, String lastName, String idNumber, String email, String password, String jobRole, String contactNumber) {
        if (isBlank(firstName) || isBlank(lastName) || isBlank(idNumber) || isBlank(email) || isBlank(password) || isBlank(jobRole) || isBlank(contactNumber)) {
            errorMessage.setValue("Please fill all fields");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage.setValue("Please enter a valid email");
            return;
        }

        if (password.length() < 6) {
            errorMessage.setValue("Password must be at least 6 characters");
            return;
        }

        loading.setValue(true);
        userRepository.createNewUser(firstName.trim(), lastName.trim(), idNumber.trim(), email.trim(), password, jobRole.trim(), contactNumber.trim(), result -> {
            loading.postValue(false);
            if (result.isSuccess()) {
                successMessage.postValue("User created successfully");
            } else {
                errorMessage.postValue(result.getErrorMessage() != null ? result.getErrorMessage() : "Failed to create user");
            }
        });
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
