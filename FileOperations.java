import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FileOperations {
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
}
