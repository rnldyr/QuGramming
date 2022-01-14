package com.huawei.qugramming;

import android.content.Context;
import android.content.SharedPreferences;

import com.huawei.hms.support.account.result.AuthAccount;

public class SharedPref {

    private SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
    }

    public void save(AuthAccount user){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", user.getDisplayName());
        editor.apply();
    }

    public String load(){
        return sharedPreferences.getString("name", "");
    }

    public void delete(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
