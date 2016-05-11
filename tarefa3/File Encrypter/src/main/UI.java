/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import file.encrypter.EncryptDecryptFile;
import file.encrypter.FileEncrypter;
import file.encrypter.GCMCipher;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import table.FilesTable;

/**
 *
 * @author eduardo
 */
public class UI {
    FileEncrypter Util = new FileEncrypter();
    String masterKey = "";
    
    public void Start() {
        FilesTable Table = (FilesTable) FilesTable.loadStatus();
        if (Table == null) { Table = new FilesTable(); }
        
        int choice = 0;
        String filePath, hmac, key = "";
        Scanner input = new Scanner(System.in);
        
        while(choice != 4) {
            System.out.println("O que você deseja fazer?");
            System.out.println("1 - Criptografar");
            System.out.println("2 - Descriptografar");
            System.out.println("3 - Mostrar Tabela");
            System.out.println("4 - Salvar e Sair");
            String line = input.nextLine();
            
            switch (line) {
                case "1" :
                    System.out.println("Digite o caminho do arquivo para criptografar: ");
                    filePath = input.nextLine();
//                    filePath = "/home/eduardo/tempData/Teste";
                    File inputFile = new File("/home/eduardo/tempData/" + filePath);
                    hmac = Util.GenerateHmacWithFileName(inputFile.getName());
                    if(Table.checkElement(hmac)){
                        System.out.println("Arquivo já criptografado!");
                        break;
                    }
                    key = Util.GenerateSecureKey(masterKey, 10000);
//                    System.out.println("Chave gerada: " + key);
                    File encryptedFile = new File("/home/eduardo/tempData/"+hmac);
                    try {
                        EncryptDecryptFile.encrypt(key, inputFile, encryptedFile);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }
                    String gcm = GCMCipher.GCMBlockCipherBC(masterKey, key, true);
                    Table.addElement(hmac, gcm);
//                    System.out.println("Isso fo criado: " + gcm);
                    System.out.println("Arquivo criptografado com sucesso.");
                    break;
                case "2" :
                    System.out.println("Digite o nome do arquivo que deseja descriptografar: ");
                    filePath = input.nextLine();
//                    filePath = "Teste";
                    hmac = Util.GenerateHmacWithFileName(filePath);
                    if(!Table.checkElement(hmac)){
                        System.out.println("Arquivo não encontrado!");
                        break;
                    }else{
                        gcm = Table.getValue(hmac);
//                        System.out.println("Gcm da tabela: " + gcm);
                        key = GCMCipher.GCMBlockCipherBC(masterKey, gcm, false);
//                        System.out.println("Chave encontrada: " + key);
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
                    Table.saveStatus();
                    break;
                default:
                    System.out.println("Entrada inválida");
            }
        }
    }

    public boolean Authenticate() throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        String savedMac = new Scanner(new FileReader("password.txt")).useDelimiter("\\Z").next();
        int attempts = 0;
        String passwordMac = "";
        boolean correct = false;
        
        while(attempts < 3) {
            System.out.println("Insira a senha");
            passwordMac = input.nextLine();
            masterKey = Util.GenerateSecureKey(passwordMac, "361a731be2f8f98d929ca2a5a8bbe764", 34564);
            passwordMac = Util.GenerateHmacWithFileName(masterKey);
            if(savedMac.equals(passwordMac))
                return true;
            else {
                attempts++;
                System.out.println("Senha incorreta, "+ (3-attempts) + " restantes.");
            }
        }
        return correct;
    }
}

/* TODO
usar criptografia autenticada nas chaves da tabela (gcm)
Falta a chave do HMAC - DONE
Guardar a tabela no disco - em memória o gcm na chave - DONE
Tirar a senha do código - DONE
*/