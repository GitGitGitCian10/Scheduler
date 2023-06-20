package com.mycompany.so;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler{
    ArrayDeque<Process> colaListo = new ArrayDeque<Process>();
    ArrayDeque<Process> colaEspera = new ArrayDeque<Process>();
    Process enEjecucion;
    int procesosCompletados = 0;
    int quantumActual = 4;
    Timer timer = new Timer();
    
    public void AddProcess(String nombre, int tiempoEjecucion, int tiempoLlegada) {
        Process p = new Process(nombre, tiempoEjecucion, tiempoLlegada);
        colaEspera.add(p);
    }
    
    public void AddProcess(Process p) {
        colaEspera.add(p);
    }
    
    public void ChequearListos() {
        Iterator<Process> iter = colaEspera.iterator();
        if(iter.hasNext()) {
            Process p = iter.next();
            if(p.EstaListo()) {
                colaListo.add(p);
                iter.remove();
            }
        }
        enEjecucion = colaListo.poll();
    }
    
    public void Simulacion() {
        Scanner scan = new Scanner(System.in);
        System.out.print('>');
        String op = scan.next();
        String pop = null;       
        while(!op.equalsIgnoreCase("a") && !op.equalsIgnoreCase("e")) {
            System.out.print('>');
            op = scan.next();
            while(op.equalsIgnoreCase("p") && !pop.equalsIgnoreCase("pe")) {
                System.out.print('>');
                pop = scan.next();
                String[] p = pop.split(",");
                AddProcess(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]));
            }
            CicloScheduler();
        }
        if(op.equalsIgnoreCase("e")) return;
        if(colaEspera.isEmpty()) {
            System.out.println("¡El scheduler no tiene tareas!");
            return;
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                CicloScheduler();
            }
        }, 1000, 1000);
        op = "e";
    }
    
    public void CicloScheduler() {
        if(colaListo.isEmpty()) {
            if(colaEspera.isEmpty()) {
                timer.cancel();
                return;
            }
            
        }
    }
}
