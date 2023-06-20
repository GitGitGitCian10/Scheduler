package com.mycompany.so;

public class SO {

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        scheduler.AddProcess("jamon", 2, 0);
        scheduler.AddProcess("queso", 3, 2);
        scheduler.AddProcess("pan", 5, 1);
        scheduler.AddProcess("mayonesa", 2, 4);
        scheduler.Simulacion(true);
    }
}
