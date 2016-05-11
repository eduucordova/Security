/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 *
 * @author eduardo
 */
public class FilesTable extends HashMap{
    private final HashMap<String, String> table;
    
    public FilesTable() {
        table = new HashMap<>();
    }
    
    public void print(){
        System.out.println("------------------------ Nome do Arquivo -------------------------------|----------------------------- Chave Simétrica --------------------------");
        table.forEach((k,v)->System.out.println("--- " + k + " ---|--- " + v + " ---"));
    }
    
    public void addElement(String hmac, String secretKey){
        table.put(hmac, secretKey);
    }
    
    public void removeElement(String hmac) {
        table.remove(hmac);
    }
    
    public boolean checkElement(String HMac) {
        return table.containsKey(HMac);
    }
    
    public String getValue(String HMac) {
        return table.get(HMac);
    }
    
    public void saveStatus(){
        try {
            FileOutputStream saveFile = new FileOutputStream("current.dat");
            ObjectOutputStream out = new ObjectOutputStream(saveFile);
            out.writeObject(this);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static HashMap<String, String> loadStatus(){
        HashMap<String, String> result = null;
        try {
           FileInputStream saveFile = new FileInputStream("current.dat");
           ObjectInputStream in = new ObjectInputStream(saveFile);
           result = (HashMap<String, String>) in.readObject();
           in.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return result;
    }
}
