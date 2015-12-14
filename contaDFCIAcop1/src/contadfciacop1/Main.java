/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contadfciacop1;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.com.jammexico.jamcomponents.JAMLibKernel;
import mx.com.jammexico.jamdb.JAMSendSql;
import mx.com.jammexico.jamsrv.JAMRowSet;

/**
 *
 * @author sfx
 */
public class Main {
    
    
    public static void main(String args[]){
     
        JAMSendSql obj = new JAMSendSql("DESPACHOFI", 
        "JAM$_CPGUSTAVO", 
        "CUDG6208");
        
        String[] arr = new String[2];
        
        
        
        
        //arr[0] = "select * from JAM$_LOGON_MENU('CPGUSTAVO')";
        
        arr[0] = "select rdb$relation_name"+
        " from rdb$relations"+
        " where rdb$view_blr is null"+ 
        " and (rdb$system_flag is null or rdb$system_flag = 0)";
        
        
        arr[1] = "select * from LOGON_SOCSYST31_CALLTRACE('CPGUSTAVO')";
        
        obj.setCommands(arr);
        
        
      
        try {
            Object string = obj;
            System.out.println(string.getClass().getName());
            URL miurl = new URL("http://octsaifx2728.koding.io/JAMMexico/servlet/JAMServeletDB");
            URLConnection conexion = miurl.openConnection();
            conexion.setDoInput(true);
            conexion.setDoOutput(true);
            conexion.setUseCaches(false);
            conexion.setDefaultUseCaches(false);
            conexion.setRequestProperty("Content-Type", "java-internal/" + string.getClass().getName());
            conexion.connect();

            ObjectOutputStream output = new ObjectOutputStream(conexion.getOutputStream());
            output.writeObject(string);
            output.flush();
            JAMSendSql recibe  = null;
            
            try
            {
              ObjectInputStream input = new ObjectInputStream(conexion.getInputStream());
              Object response = input.readObject();
              recibe = (JAMSendSql)response;
              
             
              
              JAMRowSet[] rows = recibe.getRowSets();
              
             
              
              ResultSetMetaData rm  = null;
              
              
          
              
              for(JAMRowSet row : rows){
                  System.out.println(row.getTableName());
                  
                  
                  
                  
                  
                  
                  
                  rm = row.getMetaData();
                  for(int  i = 1;  i <= rm.getColumnCount(); i++){
                      //System.out.print(rm.getColumnName(i) +"\t" +  rm.getColumnTypeName(i)+"\t" +  rm.getColumnType(i)+"\n");
                      
                      System.out.print(rm.getColumnName(i) );
                      
                      
                      
                      
                       
                  }
                  System.out.println();
              
                  
                  
              }
              
      
            output.close();
            input.close();
             
            }
            catch (ClassNotFoundException e)
            {
              e.printStackTrace();
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (recibe.getError() != null)
            {

              System.out.println(recibe.getError());

            }
      }  catch (MalformedURLException ex) {  
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
       
    }
    
    
    
    
}
