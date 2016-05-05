/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import file.encrypter.EncryptDecryptFile;
import file.encrypter.FileEncrypter;
import java.io.File;
import java.util.Scanner;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Hex;
import table.FilesTable;

/**
 *
 * @author eduardo
 */
public class UI {
    FileEncrypter Util = new FileEncrypter();
    FilesTable Table = new FilesTable();
    
    public void Start() {
        int choice = 0;
        String filePath, hmac, key = "";
        Scanner input = new Scanner(System.in);
        
        while(choice != 4) {
            System.out.println("O que você deseja fazer?");
            System.out.println("1 - Criptografar");
            System.out.println("2 - Descriptografar");
            System.out.println("3 - Mostrar Tabela");
            System.out.println("4 - Sair");
            String line = input.nextLine();
            
            switch (line) {
                case "1" :
                    System.out.println("Digite o caminho do arquivo para criptografar: ");
//                    filePath = input.nextLine();
                    filePath = "/home/eduardo/tempData/Teste";
                    File inputFile = new File(filePath);
                    hmac = Util.GenerateHmacWithFileName(inputFile.getName());
                    if(Table.checkElement(hmac)){
                        System.out.println("Arquivo já criptografado!");
                        break;
                    }
                    key = Util.GenerateSecureKey("663878", 1000);
                    File encryptedFile = new File("/home/eduardo/tempData/"+hmac);
                    try {
                        EncryptDecryptFile.encrypt(key, inputFile, encryptedFile);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }
                    Table.addElement(hmac, key);
                    System.out.println("Arquivo criptografado com sucesso.");
                    break;
                case "2" :
                    System.out.println("Digite o nome do arquivo que deseja descriptografar: ");
                    //filePath = input.nextLine();
                    filePath = "Teste";
                    hmac = Util.GenerateHmacWithFileName(filePath);
                    if(!Table.checkElement(hmac)){
                        System.out.println("Arquivo não encontrado!");
                        break;
                    }else{
                        key = Table.getValue(hmac);
                    }
                    inputFile = new File("/home/eduardo/tempData/"+hmac);
                    File decryptedFile = new File("/home/eduardo/tempData/decrypted/"+filePath);
                    try {
                        EncryptDecryptFile.decrypt(key, inputFile, decryptedFile);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    Table.removeElement(hmac);
                    System.out.println("Arquivo descriptografado com sucesso.");
                    break;
                case "3" :
                    Table.print();
                    break;
                case "4" :
                    choice = 4;
                    break;
                default:
                    System.out.println("Entrada inválida");
            }
        }
    }
}
