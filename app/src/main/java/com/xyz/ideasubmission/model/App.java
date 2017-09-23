package com.xyz.ideasubmission.model;



public class App {
    public  static String NAME="USER_NAME";
    public  static String EMAIL="USER_EMAIL";
    public  static String ID="USER_ID";
    public  static String TYPE="USER_TYPE";


    public  static String id;
    public  static String body;
    public  static String title;
    public  static String likes;


    public  static User USER;
    public static String replay_title="";
    public static int replay_id=1;

    public static User modifiedUser;

    public static void  initUser()
    {
        modifiedUser=new User();
        USER=new User();
    }

    public final static String Management="Management";
    public final static String Admin="Admin";
    public final static String Student="Student";

}
