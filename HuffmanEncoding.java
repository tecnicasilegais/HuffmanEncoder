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
	private final String path = "files\\";
	private String fullPath;
	private String filename;
	private String output;
	private String keyFullPath;
	private Operation op;
	private int codeSize;

	/**
	 * Constructor method
	 *
	 * @param filename name of file to encode
	 * @param op indicates if its going to Decode or Encode a text
	 */
	public HuffmanEncoding(String filename, String output, Operation op) {
		this.filename = filename;
		this.fullPath = path + filename;
		this.op = op;
		this.output = output;
		this.keyFullPath = path + output.replaceAll("\\..*$","") + ".dic";
	}

	public enum Operation{
		Encode, Decode
	}
	/**
	 * Start codification/decodification
	 *
	 * @return true for success or false for failure
	 */
	public boolean Start() {
		try {
			switch(op){
				case Encode:{
					// file reading
					String fileText = PrintFrequencyMap(fullPath);
					// frequency count
					HashMap<Character, Integer> map = StringToFrequencyMap(fileText);
					// creating tree
					HuffmanTree tree = new HuffmanTree(map);
					// export code to hashmap
					HashMap<Character, String> code = tree.NodeTreeToCodeMap();
					// convert to string
					String encodedText = Codify(fileText, code);
					// replace text
					boolean isOk = FileOperations.WriteStringToBin(path+output, encodedText);
					boolean isOk2 = SaveKeys(code, keyFullPath, encodedText.length());

					return isOk && isOk2;
				}
				case Decode:{
					// read keys file
					HashMap<String, Character> valueKeyMap = ReadKeys(keyFullPath);
					// read encoded file
					String encodedText = FileOperations.ReadFileToString(fullPath,codeSize);
					// start comparations
					FileOperations.WriteStringToFile(Paths.get(path+output), Decode(valueKeyMap, encodedText));

					return true;
				}
				default: return false;
			}
		}
		catch (Exception ex) {
			return false;
		}
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
	 *         respective frequencies as values
	 */
	private static HashMap<Character, Integer> StringToFrequencyMap(String input) {
		HashMap<Character, Integer> frequencyMap = new HashMap<Character, Integer>();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			// Se existe estiver no mapa pega a frequencia atual, se nao comeca em 0
			int frequency = frequencyMap.containsKey(c) ? frequencyMap.get(c) : 0;
			// Adiciona no mapa somando a frequencia + 1
			frequencyMap.put(c, frequency + 1);
		}
		return frequencyMap;
	}

	/**
	 * put all the file's text into a string
	 *
	 * @param path file
	 * @return returns the string with the text
	 * @throws Exception
	 */
	private static String PrintFrequencyMap(String path) throws Exception {
		String fileContent = "";
		fileContent = FileOperations.ReadFileToString(Paths.get(path));
		return fileContent;
	}

	private static boolean SaveKeys(HashMap<Character, String> keys, String keypath, int codeLength) {
		try {
			StringBuilder sb = new StringBuilder();
			String key;
			sb.append(codeLength).append("\n");
			for (Entry<Character, String> entry : keys.entrySet()) {
				if (String.valueOf(entry.getKey()).matches("\r")) {
					key = "CR";
				}
				else if (String.valueOf(entry.getKey()).matches("\n")) {
					key = "LF";
				}
				else if (Character.isWhitespace(entry.getKey())) {
					key = "SP";
				}
				else {
					key = entry.getKey().toString();
				}
				sb.append(entry.getValue()).append(" ").append(key).append("\n");
			}
			return FileOperations.WriteStringToFile(Paths.get(keypath), sb.toString());
		}
		catch (Exception ex) {
			return false;
		}

	}

	//decoding methods

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

	/**
	 * Reads the generated keys file and turns it into a map
	 *
	 * @param path caminho
	 * @return a map with the code as key and the character it represents as value
	 */
	private HashMap<String, Character> ReadKeys(String path) {
		try {

			HashMap<String, Character> map = new HashMap<String, Character>();
			List<String> texts = Files.readAllLines(Paths.get(path), StandardCharsets.US_ASCII);

			codeSize = Integer.parseInt(texts.remove(0));

			for (String text : texts) {
				String[] line = text.split(" ");
				String code = line[0];
				// the only char
				Character val = line[1].toCharArray()[0];
				map.put(code, val);
			}
			return map;
		}
		catch (Exception ex) {
			return null;
		}
	}

}
