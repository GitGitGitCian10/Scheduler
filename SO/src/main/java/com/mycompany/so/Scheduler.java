package com.mycompany.so;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Scheduler{
    ArrayDeque<Process> colaListo = new ArrayDeque<Process>();
    ArrayDeque<Process> colaEspera = new ArrayDeque<Process>();
    Process enEjecucion = null;
    int procesosCompletados = 0;
    int cantCiclos = 0;
    final int quantumBase = 4;
    int quantumActual = quantumBase;
    ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    boolean finalizado = false;
    
    public void AddProcess(String nombre, int tiempoEjecucion, int tiempoLlegada) {
        Process p = new Process(nombre, tiempoEjecucion, tiempoLlegada);
        colaEspera.add(p);
    }
    
    public void AddProcess(Process p) {
        colaEspera.add(p);
    }
    
    public boolean NoMasProcesos() {
        return colaListo.isEmpty() && colaEspera.isEmpty();
    }
    
    public void Simulacion() {
        Simulacion(false);
    }
    
    public void Simulacion(boolean automatico){
        Scanner scan = new Scanner(System.in);
        while(!finalizado) {
            String op;
            if(automatico) {
                op = "a";
            } else {
                System.out.print('>');
                op = scan.next();
            }
            switch(op) {
                case "p":
                    System.out.print('>');
                    String pop = scan.next();
                    while(!pop.equalsIgnoreCase("pe")) {
                        String[] p = pop.split(",");
                        AddProcess(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]));
                        System.out.println("Añadido el proceso '" + p[0] + "'");
                        System.out.print('>');
                        pop = scan.next();
                    }
                    break;
                case "e":
                    finalizado = true;
                    break;
                case "a":
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            CicloScheduler();
                        }
                    }, 1, 1, TimeUnit.SECONDS);
                    try {
                    timer.awaitTermination(1, TimeUnit.DAYS);
                    } catch(InterruptedException ex) {
                    }
                    break;

                default:
                    CicloScheduler();
                    break;
            } 
        }
        System.out.println("Procesos completados: " + procesosCompletados);
        System.out.println("Ciclos empleados: " + cantCiclos);
        
    }
    
    public void CicloScheduler() {
        if(NoMasProcesos() && (enEjecucion == null || enEjecucion.EstaFinalizado())) {
            timer.shutdownNow();
            finalizado = true;
            return; 
        }
        
        System.out.println("-".repeat(14) + "Ciclo " + (cantCiclos + 1) + "-".repeat(14));
        
        Iterator<Process> iter = colaEspera.iterator();
        while(iter.hasNext()) {
            Process p = iter.next();
            p.CicloComun();
            if(p.EstaListo()) {
                iter.remove();
                colaListo.add(p);
            }
        }
        
        if(enEjecucion == null) {
            enEjecucion = colaListo.poll();
            if(enEjecucion != null) System.out.println("Comenzando el proceso '" + enEjecucion.getNombre() + "' (" + enEjecucion.getTiempoEjecucion() + ")");;
        } else if(enEjecucion.EstaFinalizado()) {
            //
            if(!NoMasProcesos()) {     
                enEjecucion = colaListo.poll();
                if(enEjecucion != null) System.out.println("Comenzando el proceso '" + enEjecucion.getNombre() + "' (" + enEjecucion.getTiempoEjecucion() + ")");;
                quantumActual = quantumBase;
            }
        }
        
        if(quantumActual == 0) {
            colaListo.add(enEjecucion);
            System.out.println("Timeout al proceso '" + enEjecucion.getNombre() + "' (" + enEjecucion.getTiempoEjecucion() + ")");
            enEjecucion = colaListo.poll();
            if(enEjecucion != null) System.out.println("Comenzando el proceso '" + enEjecucion.getNombre() + "' (" + enEjecucion.getTiempoEjecucion() + ")");;
            quantumActual = quantumBase;
        }
        
        if(enEjecucion != null) {
            enEjecucion.CicloEnEjecucion();
            if(enEjecucion.EstaFinalizado()) {
                procesosCompletados++;
                System.out.println("Finalizado el proceso '" + enEjecucion.getNombre() + "' (" + enEjecucion.getTiempoEjecucion() + ")");
            }
            quantumActual--;
        }
        cantCiclos++;
    }
}
