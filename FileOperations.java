import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Class responsible for general File operations.
 */
public class FileOperations {
	/**
	 * Method that reads the given file to count the frequency for huffman encoding
	 * @param filepath - path to file
	 * @return Hash map containing the frequencies
	 * @throws IOException
	 * @throws NoSuchFileException
	 */
	public static HashMap<Character, Integer> CountFrequency(Path filepath) throws IOException, NoSuchFileException {
		String fileContent = Files.lines(filepath, StandardCharsets.UTF_8).collect(Collectors.joining("\n"));
		HashMap<Character, Integer> frequencyMap = new HashMap<>();
		for (int i = 0; i < fileContent.length(); i++) {
			char c = fileContent.charAt(i);
			int frequency = frequencyMap.containsKey(c) ? frequencyMap.get(c) : 0;
			frequencyMap.put(c, frequency + 1);
		}
		return frequencyMap;
	}

	/**
	 * Read all lines from a file as a String.
	 *
	 * @param filePath the path to the file
	 * @return the whole file content as a single String
	 * @throws IOException if an I/O error occurs while trying to open the file
	 */
	public static String ReadFileToString(Path filePath) throws IOException, NoSuchFileException {
		return Files.lines(filePath, StandardCharsets.US_ASCII).collect(Collectors.joining("\n"));
	}

	/**
	 * Write file
	 *
	 * @param filePath path to the file
	 * @param text     text to write
	 * @return success or failure
	 * @throws IOException
	 */
	public static boolean WriteStringToFile(Path filePath, String text) throws IOException {
		try {
			Files.write(filePath, text.getBytes());
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
}
