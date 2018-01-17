package com.stockholm.common.view;


import android.content.Context;
import android.view.KeyEvent;

import com.stockholm.common.ReleaseSystemCommandInterface;
import com.stockholm.common.notification.NotificationManager;
import com.stockholm.common.utils.StockholmLogger;
import com.stockholm.common.utils.WeakHandler;

import java.util.Timer;
import java.util.TimerTask;

class ReleaseSystemKeyEventHandler {
    private static final String TAG = "ReleaseSystemKeyEventHandler";
    private static final long LONG_PRESS_DISTANCE = 1000L;
    private static final long DOUBLE_PRESS_DISTANCE = 500L;
    private static final long VOLUME_LONG_PRESS_PERIOD = 500L;

    private Context context;
    private ReleaseSystemCommandInterface commandInterface;
    private WeakHandler handler = new WeakHandler();

    /**
     * 按键所持有的打断事件
     */
    private boolean interceptUpAction;
    private boolean interceptDownAction;
    private boolean interceptEnterAction;
    private boolean interceptSpaceAction;

    /**
     * 按键长按事件标记
     */
    private boolean upLongPress;
    private boolean downLongPress;
    private boolean enterLongPress;
    private boolean spaceLongPress;

    /**
     * 每个按键对应的事件
     */
    private KeyAction enterAction;
    private KeyAction spaceAction;
    private KeyAction upAction;
    private KeyAction downAction;

    /**
     * 成对出现，在ACTION_UP时重置对应按键开头的标记
     */
    private boolean multiUpAndDown;
    private boolean multiDownAndUp;

    private boolean multiSpaceAndUp;
    private boolean multiUpAndSpace;

    private boolean multiSpaceAndEnter;
    private boolean multiEnterAndSpace;

    //volume adjust
    private Timer upLongPressTimer;
    private Timer downLongPressTimer;

    //enter double press
    private long lastPressEnterTime;
    private boolean enterDoublePress;

    // runnable start
    private final Runnable upPressRunnable = new Runnable() {

        @Override
        public void run() {
            upLongPress = true;
            if (!multiUpAndDown && !multiUpAndSpace && !multiDownAndUp && !multiSpaceAndUp) {
                commandInterface.onControlUpLongPress();
                if (upLongPressTimer == null) {
                    upLongPressTimer = new Timer();
                }
                upLongPressTimer.schedule(new UpLongPressTimer(), 0, VOLUME_LONG_PRESS_PERIOD);
            }
        }
    };

    private final Runnable downPressRunnable = new Runnable() {

        @Override
        public void run() {
            downLongPress = true;
            if (!multiDownAndUp && !multiUpAndDown) {
                commandInterface.onControlDownLongPress();
                if (downLongPressTimer == null) {
                    downLongPressTimer = new Timer();
                }
                downLongPressTimer.schedule(new DownLongPressTimer(), 0, VOLUME_LONG_PRESS_PERIOD);
            }
        }
    };

    private final Runnable enterPressRunnable = new Runnable() {
        @Override
        public void run() {
            enterLongPress = true;
            commandInterface.onControlOkLongClick();
        }
    };

    private final Runnable enterDoublePressRunnable = new Runnable() {
        @Override
        public void run() {
            long currentClickTime = System.currentTimeMillis();
            long elapsedTime = currentClickTime - lastPressEnterTime;
            lastPressEnterTime = currentClickTime;

            if (elapsedTime < DOUBLE_PRESS_DISTANCE) {
                enterDoublePress = true;
                lastPressEnterTime = 0;
                commandInterface.onControlOkDoubleClick();
            } else {
                enterDoublePress = false;
            }
        }
    };

    private final Runnable spacePressRunnable = new Runnable() {
        @Override
        public void run() {
            spaceLongPress = true;
            if (!multiSpaceAndUp && !multiUpAndSpace) {
                commandInterface.onLineLongDrag();
            }
        }
    };

    // multi key event
    private final Runnable upAndDownRunnable = new Runnable() {
        @Override
        public void run() {
            multiUpAndDown = false;
            multiDownAndUp = false;
            upLongPress = true;
            downLongPress = true;
            commandInterface.onConnectWifiButtonClick();
        }
    };

    private final Runnable spaceAndUpRunnable = new Runnable() {
        @Override
        public void run() {
            multiSpaceAndUp = false;
            multiUpAndSpace = false;
            upLongPress = true;
            spaceLongPress = true;
            commandInterface.onReverseSwitchAppClick();
        }
    };

    // runnable end

    public ReleaseSystemKeyEventHandler(Context context) {
        this.context = context;
    }

    public void setCommandInterface(ReleaseSystemCommandInterface commandInterface) {
        this.commandInterface = commandInterface;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event, boolean notificationShow) {
        StockholmLogger.d(TAG, "onKeyDown: " + keyCode + ", showNotification: " + notificationShow);
        if (event.getRepeatCount() == 0) {
            if (notificationShow) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (spaceAction != null) {
                            interceptEnterAction = true;
                            interceptSpaceAction = true;
                            return false;
                        }
                        enterAction = new KeyAction();
                        break;
                    case KeyEvent.KEYCODE_SPACE:
                        if (enterAction != null) {
                            interceptSpaceAction = true;
                            interceptEnterAction = true;
                            return false;
                        }
                        spaceAction = new KeyAction();
                        break;
                    default:
                        break;
                }
                return true;
            } else {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        upAction = new KeyAction();
                        if (upAction.isMultiAction(downAction)) {
                            multiUpAndDown = true;
                            multiDownAndUp = true;
                            handler.postDelayed(upAndDownRunnable, LONG_PRESS_DISTANCE);
                        } else if (upAction.isMultiAction(spaceAction)) {
                            multiUpAndSpace = true;
                            multiSpaceAndUp = true;
                            handler.postDelayed(spaceAndUpRunnable, LONG_PRESS_DISTANCE);
                        } else {
                            handler.postDelayed(upPressRunnable, LONG_PRESS_DISTANCE);
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (enterAction != null || spaceAction != null) {
                            handler.removeCallbacksAndMessages(null);
                            interceptEnterAction = true;
                            interceptSpaceAction = true;
                            interceptDownAction = true;
                            return false;
                        }
                        downAction = new KeyAction();
                        if (downAction.isMultiAction(upAction)) {
                            multiDownAndUp = true;
                            multiUpAndDown = true;
                            handler.postDelayed(upAndDownRunnable, LONG_PRESS_DISTANCE);
                        } else
                            handler.postDelayed(downPressRunnable, LONG_PRESS_DISTANCE);
                        break;
                    case KeyEvent.KEYCODE_ENTER:
                        if (spaceAction != null || downAction != null) {
                            handler.removeCallbacksAndMessages(null);
                            interceptSpaceAction = true;
                            interceptDownAction = true;
                            interceptEnterAction = true;
                            return false;
                        }
                        enterAction = new KeyAction();
                        handler.post(enterDoublePressRunnable);
                        handler.postDelayed(enterPressRunnable, LONG_PRESS_DISTANCE);
                        break;
                    case KeyEvent.KEYCODE_SPACE:
                        if (downAction != null || enterAction != null) {
                            handler.removeCallbacksAndMessages(null);
                            interceptSpaceAction = true;
                            interceptDownAction = true;
                            interceptEnterAction = true;
                            return false;
                        }
                        spaceAction = new KeyAction();
                        if (spaceAction.isMultiAction(upAction)) {
                            multiSpaceAndUp = true;
                            multiUpAndSpace = true;
                            handler.postDelayed(spaceAndUpRunnable, LONG_PRESS_DISTANCE);
                        } else {
                            handler.postDelayed(spacePressRunnable, LONG_PRESS_DISTANCE);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event, boolean notificationShow, boolean hasKeyDownEvent) {
        StockholmLogger.d(TAG, "onKeyUp: " + keyCode + ", showNotification: " + notificationShow);
        if (commandInterface.onAllKeyEvent(event)) {
            handleKeyUpEvent(keyCode, false, hasKeyDownEvent);
            return true;
        }

        if (notificationShow) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    enterAction = null;
                    if (!interceptEnterAction) {
                        NotificationManager.pauseNotification(context);
                    }
                    interceptEnterAction = false;
                    break;
                case KeyEvent.KEYCODE_SPACE:
                    spaceAction = null;
                    if (!interceptSpaceAction) {
                        NotificationManager.clearNotification(context);
                    }
                    interceptSpaceAction = false;
                    break;
                default:
                    break;
            }
        } else {
            handleKeyUpEvent(keyCode, true, hasKeyDownEvent);
        }
        return true;
    }

    private void handleKeyUpEvent(int keyCode, boolean consumeEvent, boolean hasKeyDownEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                upAction = null;
                handler.removeCallbacks(upAndDownRunnable);
                handler.removeCallbacks(upPressRunnable);
                handler.removeCallbacks(spaceAndUpRunnable);
                if (upLongPressTimer != null) {
                    upLongPressTimer.cancel();
                    upLongPressTimer = null;
                }
                if (consumeEvent && hasKeyDownEvent && !upLongPress && !multiUpAndDown && !multiUpAndSpace && !multiDownAndUp
                        && !multiSpaceAndUp && !interceptUpAction) {
                    commandInterface.onControlUpClick();
                }
                upLongPress = false;
                multiUpAndDown = false;
                multiUpAndSpace = false;
                interceptUpAction = false;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                downAction = null;
                handler.removeCallbacks(upAndDownRunnable);
                handler.removeCallbacks(downPressRunnable);
                if (downLongPressTimer != null) {
                    downLongPressTimer.cancel();
                    downLongPressTimer = null;
                }
                if (consumeEvent && hasKeyDownEvent && !downLongPress && !multiDownAndUp && !multiUpAndDown && !interceptDownAction) {
                    commandInterface.onControlDownClick();
                }
                downLongPress = false;
                multiDownAndUp = false;
                interceptDownAction = false;
                break;
            case KeyEvent.KEYCODE_ENTER:
                enterAction = null;
                handler.removeCallbacks(enterPressRunnable);
                if (consumeEvent && hasKeyDownEvent && !enterLongPress && !interceptEnterAction) {
                    handler.postDelayed(() -> {
                        StockholmLogger.d(TAG, "handleKeyUpEvent: " + enterDoublePress);
                        if (!enterDoublePress) commandInterface.onControlOKClick();
                    }, DOUBLE_PRESS_DISTANCE);
                }
                enterLongPress = false;
                interceptEnterAction = false;
                break;
            case KeyEvent.KEYCODE_SPACE:
                spaceAction = null;
                handler.removeCallbacks(spacePressRunnable);
                handler.removeCallbacks(spaceAndUpRunnable);
                if (consumeEvent && hasKeyDownEvent && !spaceLongPress && !multiSpaceAndUp && !multiUpAndSpace && !interceptSpaceAction) {
                    commandInterface.onLineShortDrag();
                }
                spaceLongPress = false;
                multiSpaceAndUp = false;
                interceptSpaceAction = false;
                break;
            case KeyEvent.KEYCODE_N:
                if (consumeEvent) {
                    commandInterface.onNotificationButtonClick();
                }
                break;
            case KeyEvent.KEYCODE_D:
                if (consumeEvent) {
                    commandInterface.onDebugButtonClick();
                }
                break;
            case KeyEvent.KEYCODE_V:
                if (consumeEvent) {
                    commandInterface.onVersionButtonClick();
                }
                break;
            case KeyEvent.KEYCODE_T:
                if (consumeEvent) {
                    commandInterface.onTestButtonClick();
                }
                break;
            default:
                break;
        }
    }

    private class UpLongPressTimer extends TimerTask {

        @Override
        public void run() {
            if (upLongPress && !multiUpAndDown && !multiUpAndSpace && !multiDownAndUp
                    && !multiSpaceAndUp && !interceptUpAction) {
                commandInterface.onLongPressRaiseVolume();
            }
        }
    }

    private class DownLongPressTimer extends TimerTask {

        @Override
        public void run() {
            if (downLongPress && !multiDownAndUp && !multiUpAndDown && !interceptDownAction) {
                commandInterface.onLongPressLowerVolume();
            }
        }
    }

}