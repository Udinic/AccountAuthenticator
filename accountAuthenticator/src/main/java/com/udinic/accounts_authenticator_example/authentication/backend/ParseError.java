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

package com.udinic.accounts_authenticator_example.authentication.backend;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

class ParseError implements Parcelable {

    public static final Creator<ParseError> CREATOR = new Creator<ParseError>() {
        @Override
        public ParseError createFromParcel(Parcel source) {
            return new ParseError(source);
        }

        @Override
        public ParseError[] newArray(int size) {
            return new ParseError[size];
        }
    };

    final int code;

    final String error;

    private ParseError(Parcel source) {
        code = source.readInt();
        error = source.readString();
    }

    @Override
    public int describeContents() {
        return ParseError.class.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(error);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
