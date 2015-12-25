/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contadfciacop1;

import mx.com.jammexico.jamlogon.JAM010RunTime;

/**
 *
 * @author VPCZ1290L
 */
public class ContaDFCIAcop1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        if (args.length == 0) {
            new JAM010RunTime();
        } else {
            new JAM010RunTime(args, false);
        }
    }
    
}
