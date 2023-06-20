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
    final int quantumBase = 4;
    int quantumActual;
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
                iter.remove();
                colaListo.add(p);
            }
        }
        enEjecucion = colaListo.poll();
    }
    
    public boolean NoMasProcesos() {
        return colaListo.isEmpty() && colaEspera.isEmpty();
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
        ChequearListos();
        quantumActual = quantumBase;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                CicloScheduler();
            }
        }, 1000, 1000);
        op = "e";
    }
    
    public void CicloScheduler() {
        if(NoMasProcesos() && (enEjecucion == null || enEjecucion.EstaFinalizado())) {
            timer.cancel();
            return; 
        }
        
        if(enEjecucion == null) {
            enEjecucion = colaListo.poll();
        } else if(enEjecucion.EstaFinalizado()) {
            procesosCompletados++;
            if(!NoMasProcesos()) {     
                enEjecucion = colaListo.poll();
                quantumActual = quantumBase;
            }
        }
        
        if(quantumActual == 0) {
            colaListo.add(enEjecucion);
            enEjecucion = colaListo.poll();
            quantumActual = quantumBase;
        }
        
        Iterator<Process> iter = colaEspera.iterator();
        while(iter.hasNext()) {
            Process p = iter.next();
            p.CicloComun();
            if(p.EstaListo()) {
                iter.remove();
                colaListo.add(p);
            }
        }
        
        if(enEjecucion != null) {
            enEjecucion.CicloEnEjecucion();
            quantumActual--;
        }
    }
}
