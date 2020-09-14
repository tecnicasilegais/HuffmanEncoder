package com.tecnicasilegais.huffman;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Implementation of a text encoder using Huffman Tree
 *
 * @author Eduardo Andrade
 * @author Marcelo Heredia
 * @author Michael Rosa
 * @author Pedro Castro
 */
public class HuffmanEncoding {
    private final String fileIn;
    private final String fileOut;
    private final Operation op;
    private int codeSize;

    /**
     * Constructor method
     *
     * @param fileIn  text to encode/decode
     * @param fileOut encoded/decoded text
     * @param op      indicates if its going to Encode or Decode a text
     */
    public HuffmanEncoding(String fileIn, String fileOut, Operation op) {
        this.fileIn = fileIn;
        this.fileOut = fileOut;
        this.op = op;
    }

    /**
     * Codifies the text into a encoded text
     *
     * @param text original text
     * @param code codes
     * @return encoded String
     */
    private static String Codify(String text, HashMap<Character, String> code) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            Character letter = text.charAt(i);
            sb.append(code.get(letter));
        }
        return sb.toString();
    }

    /**
     * Counts the frequency each character from the string appears.
     *
     * @param input String to count frequency of each character
     * @return a HashMap using the characters from input as keys and their
     * respective frequencies as values
     */
    public static HashMap<Character, Integer> StringToFrequencyMap(String input) {
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            // Se existe estiver no mapa pega a frequencia atual, se nao comeca em 0
            int frequency = frequencyMap.getOrDefault(c, 0);
            // Adiciona no mapa somando a frequencia + 1
            frequencyMap.put(c, frequency + 1);
        }
        return frequencyMap;
    }

    private static boolean SaveKeys(HashMap<Character, String> keys, String keypath, int codeLength) {
        try {
            StringBuilder sb = new StringBuilder();
            String key;
            sb.append(codeLength).append("\n");
            for (Entry<Character, String> entry : keys.entrySet()) {
                if (String.valueOf(entry.getKey()).matches("\r")) {
                    key = "CR";
                } else if (String.valueOf(entry.getKey()).matches("\n")) {
                    key = "LF";
                } else if (Character.isWhitespace(entry.getKey())) {
                    key = "SP";
                } else {
                    key = entry.getKey().toString();
                }
                sb.append(entry.getValue()).append(" ").append(key).append("\n");
            }
            return FileOperations.WriteStringToFile(Paths.get(keypath), sb.toString());
        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * Start codification/decodification
     *
     * @return true for success or false for failure
     */
    public boolean Start() {
        try {
            switch (op) {
                case Encode: {
                    // file reading
                    String fileText = FileOperations.ReadFileToString(Paths.get(fileIn));
                    // frequency count
                    HashMap<Character, Integer> map = StringToFrequencyMap(fileText);
                    // creating tree
                    HuffmanTree tree = new HuffmanTree(map);
                    // export code to hashmap
                    HashMap<Character, String> code = tree.NodeTreeToCodeMap();
                    // convert to string
                    String encodedText = Codify(fileText, code);
                    // replace text
                    boolean isOk = FileOperations.WriteStringToBin(fileOut, encodedText);
                    boolean isOk2 = SaveKeys(code, fileOut.replaceAll("\\..*$", "") + ".dic", encodedText.length());

                    return isOk && isOk2;
                }
                case Decode: {
                    // read keys file
                    HashMap<String, Character> valueKeyMap = ReadKeys(fileIn.replaceAll("\\..*$", "") + ".dic");
                    // read encoded file
                    String encodedText = FileOperations.ReadFileToString(fileIn, codeSize);
                    // start comparations
                    FileOperations.WriteStringToFile(Paths.get(fileOut), Decode(valueKeyMap, encodedText));

                    return true;
                }
                default:
                    return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Decodes the encoded text using the provided map
     *
     * @param codeMap     the map of codes and chars
     * @param encodedText the encoded text
     * @return the text decoded
     */
    private String Decode(HashMap<String, Character> codeMap, String encodedText) {
        StringBuilder resultText = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();

        for (int i = 0; i < encodedText.length(); i++) {
            currentCode.append(encodedText.charAt(i));
            if (codeMap.containsKey(currentCode.toString())) {
                resultText.append(codeMap.get(currentCode.toString()));
                currentCode.setLength(0);
            }
        }
        return resultText.toString();
    }

    //decoding methods

    /**
     * Reads the generated keys file and turns it into a map
     *
     * @param path caminho
     * @return a map with the code as key and the character it represents as value
     */
    private HashMap<String, Character> ReadKeys(String path) {
        try {

            HashMap<String, Character> map = new HashMap<>();
            List<String> texts = Files.readAllLines(Paths.get(path), StandardCharsets.US_ASCII);

            codeSize = Integer.parseInt(texts.remove(0));

            for (String text : texts) {
                String[] line = text.split(" ");
                String code = line[0];
                char val;
                if (String.valueOf(line[1]).matches("LF")) {
                    val = '\r';
                } else if (String.valueOf(line[1]).matches("CR")) {
                    val = '\n';
                } else if (String.valueOf(line[1]).matches("SP")) {
                    val = ' ';
                } else {
                    val = line[1].toCharArray()[0];
                }
                map.put(code, val);
            }
            return map;
        } catch (Exception ex) {
            return null;
        }
    }

    public enum Operation {
        Encode, Decode
    }

}
