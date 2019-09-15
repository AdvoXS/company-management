package com.company;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        DataBaseControl dataBaseControl = new DataBaseControl();
        UserInterface ui = new UserInterface(dataBaseControl);
        ui.setVisible(true);
    }
}
