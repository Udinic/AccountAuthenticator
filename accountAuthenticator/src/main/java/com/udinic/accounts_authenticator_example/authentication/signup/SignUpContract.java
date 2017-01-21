/*
 * Copyright (c) 2017 Udi Cohen, Joao Paulo Fernandes Ventura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.udinic.accounts_authenticator_example.authentication.signup;

import com.udinic.accounts_authenticator_example.authentication.BasePresenter;
import com.udinic.accounts_authenticator_example.authentication.BaseView;

public interface SignUpContract {

    interface View extends BaseView<Presenter> {

        String getAccountName();

        String getAccountType();

        String getPassword();

        String getName();

        void onClickSignIn();

        void onClickSignUp();

    }

    interface Presenter extends BasePresenter {

        void signUp();

    }

}
