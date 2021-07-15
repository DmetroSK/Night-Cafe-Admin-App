package com.nightcafeadmin.app.databases;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    //variables
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_Login = "IsLoggedIn";

    public static final String KEY_NAME = "name";
    public static final String KEY_email = "email";
    public static final String KEY_phone = "phone";

    public SessionManager(Context _context){
        context = _context;
        userSession = context.getSharedPreferences("adminLoginSession",Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String name,String email,String phone)
    {
        editor.putBoolean(IS_Login,true);

        editor.putString(KEY_NAME,name);
        editor.putString(KEY_email,email);
        editor.putString(KEY_phone,phone);

        editor.commit();
    }

    public HashMap<String, String> getUserDetailFromSession(){
        HashMap<String,String> userData = new HashMap<String,String>();

        userData.put(KEY_NAME,userSession.getString(KEY_NAME,null));
        userData.put(KEY_email,userSession.getString(KEY_email,null));
        userData.put(KEY_phone,userSession.getString(KEY_phone,null));

        return userData;
    }

    public boolean checkLogin(){
        if (userSession.getBoolean(IS_Login,true)){
            return true;
        }
        else {
            return false;
        }
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }
}
