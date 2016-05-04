/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eduardo
 */
public class FilesTable {
    private final Map<String, String> table;
    
    public FilesTable() {
        table = new HashMap<>();
    }
    
    public void print(){
        System.out.println("---- Nome do Arquivo ---------|---------- Chave Simétrica ---------");
        table.forEach((k,v)->System.out.println("--- " + k + " ---|--- " + v + " ---"));
    }
    
    public void addElement(String hmac, String secretKey){
        table.put(hmac, secretKey);
    }
    
    public boolean checkElement(String HMac) {
        return table.containsKey(HMac);
    }
}
