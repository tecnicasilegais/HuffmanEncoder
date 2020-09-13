package com.tecnicasilegais;

import com.tecnicasilegais.huffman.HuffmanEncoding;

import java.util.Scanner;

public class App {

    private static final String path = "files\\";
    private static HuffmanEncoding.Operation op;
    private static String input;
    private static String output;

    /**
     * Menu class
     *
     * @author Eduardo Andrade
     * @author Marcelo Heredia
     * @author Michael Rosa
     * @author Pedro Castro
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int control;
        do {
            try {
                System.out.println("Digite a operação desejada");
                System.out.println("1 - Codificar texto");
                System.out.println("2 - Decodificar texto");
                System.out.println("0 - Sair");

                control = in.nextInt();

                switch (control) {
                    case 1:
                        op = HuffmanEncoding.Operation.Encode;
                        System.out.println("Digite o nome do arquivo a ser codificado");
                        System.out.println("(Lembrando que o arquivo deve estar na pasta files)");
                        input = in.next();
                        System.out.println("Digite o nome do arquivo de saida");
                        output = in.next();
                        executeAux();
                        break;
                    case 2:
                        op = HuffmanEncoding.Operation.Decode;
                        System.out.println("Digite o nome do arquivo a ser decodificado");
                        System.out.println("(Lembrando que o arquivo deve estar na pasta files)");
                        input = in.next();
                        System.out.println("Digite o nome do arquivo de saida");
                        output = in.next();
                        executeAux();
                        break;
                    default:
                }
            } catch (Exception ex) {
                System.out.println("Erro inesperado, recomeçando.");
                control = 3;
            }
        } while (control != 0);
        in.close();
    }

    private static void executeAux() {
        HuffmanEncoding he = new HuffmanEncoding(path + input, path + output, op);
        boolean use = he.Start();
        if (!use) {
            System.out.println("Arquivo não encontrado.");
            return;
        }
        System.out.println("Concluido.");
    }
}