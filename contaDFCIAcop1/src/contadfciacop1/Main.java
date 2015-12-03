/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contadfciacop1;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/**
 *
 * @author sfx
 */
public class Main {
    
    
    public static void main(String args[]){
        
        WindowsLookAndFeel laf = new WindowsLookAndFeel();

        System.out.println(laf.isSupportedLookAndFeel());
        
    }
    
    
    
    
}
