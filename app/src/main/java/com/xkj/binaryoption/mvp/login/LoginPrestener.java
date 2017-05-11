package com.xkj.binaryoption.mvp.login;

/**
 * Created by huangsc on 2017-04-17.
 * TODO:控制登入界面的控制类
 */

public class LoginPrestener {
    public interface ViewListener{
      void   onLoginHttpfaild(String failedString);
    }
    public interface PreListener{
        void doLogin(String name, String passwd);
        void onDestroy();
    }
}
