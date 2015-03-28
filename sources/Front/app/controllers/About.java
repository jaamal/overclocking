package controllers;

import play.mvc.Controller;

public class About extends Controller {

    public static void run() {
        render("About/about.html");
    }
}