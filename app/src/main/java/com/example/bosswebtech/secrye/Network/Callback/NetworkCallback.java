package com.example.bosswebtech.secrye.Network.Callback;

import android.os.Bundle;

public interface NetworkCallback {
    public void onSuccess(String msg);
    public void onFailure(String msg);
    public void onSuccess(Bundle msg);
    public void onFailure(Bundle msg);
}