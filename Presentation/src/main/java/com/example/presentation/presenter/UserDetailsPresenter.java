/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.example.presentation.presenter;

import com.example.domain.interactor.GetUserDetailsUseCase;
import com.example.presentation.exception.ErrorMessageFactory;
import com.example.presentation.mapper.UserModelDataMapper;
import com.example.presentation.model.UserModel;
import com.example.presentation.view.UserDetailsView;
import com.example.shared.dagger.DaggerSupport;
import com.example.shared.exception.ErrorBundle;
import com.example.shared.model.User;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@EBean
public class UserDetailsPresenter implements Presenter {

    /**
     * id used to retrieve user details
     */
    private int userId;

    private UserDetailsView viewDetailsView;

    @Inject
    protected GetUserDetailsUseCase getUserDetailsUseCase;

    @Bean
    protected GetUserDetailsUseCaseCallback userDetailsCallback;

    @Bean
    protected UserModelDataMapper userModelDataMapper;

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    /**
     * Initializes the presenter by start retrieving user details.
     */
    public void initialize(UserDetailsView viewDetailsView, int userId) {
        DaggerSupport.inject(this);
        userDetailsCallback.register(this);
        this.viewDetailsView = viewDetailsView;
        this.userId = userId;
        this.loadUserDetails();
    }

    /**
     * Loads user details.
     */
    private void loadUserDetails() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getUserDetails();
    }

    private void showViewLoading() {
        this.viewDetailsView.showLoading();
    }

    void hideViewLoading() {
        this.viewDetailsView.hideLoading();
    }

    void showViewRetry() {
        this.viewDetailsView.showRetry();
    }

    private void hideViewRetry() {
        this.viewDetailsView.hideRetry();
    }

    void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.viewDetailsView.getContext(),
                errorBundle.getError());
        this.viewDetailsView.showError(errorMessage);
    }

    void showUserDetailsInView(User user) {
        final UserModel userModel = this.userModelDataMapper.transform(user);
        this.viewDetailsView.renderUser(userModel);
    }

    @Background
    protected void getUserDetails() {
        this.getUserDetailsUseCase.execute(this.userId, this.userDetailsCallback);
    }
}
