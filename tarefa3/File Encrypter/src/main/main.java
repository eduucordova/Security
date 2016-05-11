/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author eduardo
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        UI UserInterface = new UI();
        try {
            if(UserInterface.Authenticate())
                UserInterface.Start();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
