package com.chobo.week2_2try;

public interface HttpCallback {
    void onSuccess(String result);
    void onFailure(Exception e);
}
