/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.harkaitz.hrkono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 *
 * @author harkaitz
 */
public class GenericShell implements Runnable {
    PrintStream    [] o = {null,null};
    BufferedReader    i = null;
    public boolean isInteractive = true;
    public String ps1    = "> ";
    public String banner = "Type `help` for help.";
    public String help   = 
            "help        : List commands.\n"+
            "echo STR... : Print arguments.\n"+
            "err         : Get last error info.\n"+
            "exit        : Quit shell.\n";
    public boolean loop  = true;
    Exception lastError = null;
    public GenericShell(
            InputStream stdin,
            PrintStream stdout,
            PrintStream stderr) {
        this.i = new BufferedReader(new InputStreamReader(stdin));
        this.o[0] = stdout;
        this.o[1] = stderr;
    }
    public GenericShell() {
        this(System.in,System.out,System.err);
    }
    protected String overrideAddHelp() { return ""; }
    public static void main(String [] args) {
        GenericShell s = new GenericShell();
        s.run();
    }
    @Override
    public void run() {
        print(3,banner+"\n");
        while(loop) {
            try {
                print(3,ps1);
                String str = readLine();
                if(str != null) {
                    execute(str);
                } else {
                    loop = false;
                }
            } catch(IOException err) {
                loop = false;
            } catch(Exception err) {
                this.lastError = err;
                print(2,"Error: "+err.getMessage()+"\n");
            }
        }
    }
    public final void execute(String [] str) throws Exception {
        if(str.length==0) {
            
        } else if(str[0].equals("")) {
            
        } else if(str[0].equalsIgnoreCase("echo")) {
            for(int j=1;j<str.length;j++) {
                print(1,str[j]+" ");
            }
            print(1,"\n");
        } else if(str[0].equalsIgnoreCase("help")) {
            print(1,help+"\n"+overrideAddHelp());
        } else if(str[0].equalsIgnoreCase("err")) {
            if(lastError==null) {
                print(1,"No errors.\n");
            } else {
                lastError.printStackTrace(o[0]);
            }
        } else if(str[0].equalsIgnoreCase("exit")) {
            loop = false;
        } else if(!overrideExecute(str)) {
            throw new Exception("Command not found.");
        }
    }
    public final void execute(String str) throws Exception {
        execute(str.replaceAll("^\\s+|\\s+$", "").split("  *"));
    }
    public final void print(int p,Object obj) {
        if(p==1 || p==2) {
            o[p-1].print(obj);
        }
        if(p==3 && isInteractive) {
            o[1].print(obj);
        }
    }
    public final String readLine() throws IOException {
        return i.readLine();
    }
    
    /**
     * Override this function to implement new commands,
     * run `super.overrideExecute` before and if it returns
     * true skip interpreting.
     * @param str
     * @return Returns true if the command is recognized.
     * @throws Exception 
     */
    protected boolean overrideExecute(String [] str) throws Exception {
        return false;
    }

    
    
}
