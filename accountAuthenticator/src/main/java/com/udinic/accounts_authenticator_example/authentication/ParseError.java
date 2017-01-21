package com.udinic.accounts_authenticator_example.authentication;

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
