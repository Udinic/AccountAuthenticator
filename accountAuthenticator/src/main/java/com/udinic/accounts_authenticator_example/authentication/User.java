/*
 * Copyright (c) 2013 Udi Cohen
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


package com.udinic.accounts_authenticator_example.authentication;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

class User implements Parcelable {

    final String avatarUrl;

    final String firstName;

    final String gravatarId;

    final String lastName;

    final String objectId;

    final String phone;

    final String sessionToken;

    final String username;

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel source) {
        avatarUrl = source.readString();
        firstName = source.readString();
        gravatarId = source.readString();
        lastName = source.readString();
        objectId = source.readString();
        phone = source.readString();
        sessionToken = source.readString();
        username = source.readString();
    }

    @Override
    public int describeContents() {
        return User.class.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatarUrl);
        dest.writeString(firstName);
        dest.writeString(gravatarId);
        dest.writeString(lastName);
        dest.writeString(objectId);
        dest.writeString(phone);
        dest.writeString(sessionToken);
        dest.writeString(username);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
