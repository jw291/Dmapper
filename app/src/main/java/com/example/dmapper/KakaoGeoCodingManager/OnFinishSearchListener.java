package com.example.dmapper.KakaoGeoCodingManager;

import java.util.List;

public interface OnFinishSearchListener {
    public void onSuccess(List<KakaoContainer> itemList);
    public void onFail();
}
