package com.example.bruno.tabs;

/**
 * OnDataReadListener defines witch methods should be used when a data is read from a connection
 * @see ConnectedThread
 */
public abstract class OnDataReadListener {
    public abstract void onDataRead(String data);
}
