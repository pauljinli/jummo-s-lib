package com.stockholm.common.view;


import android.content.Context;
import android.view.KeyEvent;

import com.stockholm.common.SystemCommandInterface;
import com.stockholm.common.notification.NotificationManager;

public class SystemKeyEventHandler {

    private boolean volumePlusLongPress = false;
    private boolean dpadUpLongPress = false;
    private boolean dpadDownLongPress = false;

    private Context context;

    private SystemCommandInterface commandInterface;

    public SystemKeyEventHandler(Context context) {
        this.context = context;
    }

    public void setCommandInterface(SystemCommandInterface commandInterface) {
        this.commandInterface = commandInterface;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                volumePlusLongPress = true;
                commandInterface.onLineLongDrag();
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                dpadUpLongPress = true;
                commandInterface.onControlUpLongPress();
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                dpadDownLongPress = true;
                commandInterface.onControlDownLongPress();
                return true;

            default:
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                event.startTracking();
                break;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                event.startTracking();
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                event.startTracking();
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                event.startTracking();
                break;

            default:
        }
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event, boolean notificationShow) {
        if (commandInterface.onAllKeyEvent(event)) return true;

        if (notificationShow) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_SPACE:
                    NotificationManager.clearNotification(context);
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    NotificationManager.pauseNotification(context);
                    break;

                default:
            }
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_SPACE:
                    commandInterface.onLineShortDrag();
                    break;
                case KeyEvent.KEYCODE_0:
                    commandInterface.onControlOkLongClick();
                    break;
                case KeyEvent.KEYCODE_9:
                    commandInterface.onLineLongDrag();
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (!dpadUpLongPress) {
                        commandInterface.onControlUpClick();
                    }
                    dpadUpLongPress = false;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (!dpadDownLongPress) {
                        commandInterface.onControlDownClick();
                    }
                    dpadDownLongPress = false;
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    commandInterface.onControlOKClick();
                    break;
                case KeyEvent.KEYCODE_EQUALS:
                    commandInterface.onKeyVolumeUpClick();
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    commandInterface.onKeyVolumeDownClick();
                    break;
                case KeyEvent.KEYCODE_T:
                    commandInterface.onTestButtonClick();
                    break;
                case KeyEvent.KEYCODE_N:
                    commandInterface.onNotificationButtonClick();
                    break;
                case KeyEvent.KEYCODE_2:
                    commandInterface.onControlOkDoubleClick();
                    break;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    if (!volumePlusLongPress) {
                        commandInterface.onControlOKClick();
                    }
                    volumePlusLongPress = false;
                    break;
                case KeyEvent.KEYCODE_C:
                    commandInterface.onConnectWifiButtonClick();
                    break;
                case KeyEvent.KEYCODE_D:
                    commandInterface.onDebugButtonClick();
                    break;
                case KeyEvent.KEYCODE_V:
                    commandInterface.onVersionButtonClick();
                    break;
                case KeyEvent.KEYCODE_H:
                    commandInterface.onDeviceTestButtonClock();
                    break;
                case KeyEvent.KEYCODE_F:
                    commandInterface.onFactoryButtonClick();
                    break;
                default:
            }
        }
        return true;
    }
}
