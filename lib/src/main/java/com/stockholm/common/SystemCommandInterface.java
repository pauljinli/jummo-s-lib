package com.stockholm.common;

import android.view.KeyEvent;

public interface SystemCommandInterface {


    boolean onAllKeyEvent(KeyEvent event);

    /**
     * 拉绳长拉事件
     */
    void onLineLongDrag();

    /**
     * 拉绳短拉事件
     */
    void onLineShortDrag();

    /**
     * 线控按钮上 单击事件
     */
    void onControlUpClick();

    /**
     * 线控按钮上 长按事件
     */
    void onControlUpLongPress();

    /**
     * 线控按钮下 单击事件
     */
    void onControlDownClick();

    /**
     * 线控按钮下 长按事件
     */
    void onControlDownLongPress();

    /**
     * 线控按钮OK单击事件
     */
    void onControlOKClick();

    /**
     * 线控按钮OK双击事件
     */
    void onControlOkDoubleClick();

    /**
     * 线控按钮OK长按事件
     */
    void onControlOkLongClick();

    /**
     * 音量+ 点击事件
     */
    void onKeyVolumeUpClick();

    /**
     * 音量- 点击事件
     */
    void onKeyVolumeDownClick();

    /**
     * 测试按键
     */
    void onTestButtonClick();

    /**
     * 测试弹出通知
     */
    void onNotificationButtonClick();

    /**
     * 重新连接wifi按钮点击事件
     */
    void onConnectWifiButtonClick();

    /**
     * 显示debug页面
     */
    void onDebugButtonClick();

    /**
     * 显示版本信息
     */
    void onVersionButtonClick();

    /**
     * 启动硬件测试软件
     */
    void onDeviceTestButtonClock();

    /**
     * 工厂模式
     */
    void onFactoryButtonClick();

}