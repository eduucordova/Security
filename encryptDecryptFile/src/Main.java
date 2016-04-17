import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserInteface();
    }

    private static void UserInteface(){
        int choice = 0;
        String filePath = "";
        String key = "";
        Scanner input = new Scanner(System.in);

        while(choice != 3) {
            System.out.println("O que você quer fazer?");
            System.out.println("1 - Criptografar");
            System.out.println("2 - Descriptografar");
            System.out.println("3 - Sair");
            String line = input.nextLine();
            switch (line) {
                case "1" :
                    System.out.println("Digite o caminho do arquivo para criptografar: ");
                    filePath = input.nextLine();
                    System.out.println("Digite uma chave: ");
                    key = input.nextLine();
                    File inputFile = new File(filePath);
                    File encryptedFile = new File("../AppData/document.encrypted");
                    try {
                        EncryptDecryptFile.encrypt(key, inputFile, encryptedFile);
                    } catch (CryptoException ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                    System.out.println("Arquivo criptografado com sucesso.\nGuardado em ~/AppData/document.encrypted.");
                    break;
                case "2" :
                    System.out.println("Digite o caminho do arquivo para descriptografar: ");
                    filePath = input.nextLine();
                    System.out.println("Digite uma chave: ");
                    key = input.nextLine();
                    inputFile = new File(filePath);
                    File decryptedFile = new File("../AppData/document.decrypted");
                    try {
                        EncryptDecryptFile.decrypt(key, inputFile, decryptedFile);
                    } catch (CryptoException ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                    System.out.println("Arquivo criptografado com sucesso.\nGuardado em ~/AppData/document.decrypted.");
                    break;
                case "3" :
                    choice = 3;
                    break;
                default:
                    System.out.println("Entrada inválida");
            }

        }
    }
}
