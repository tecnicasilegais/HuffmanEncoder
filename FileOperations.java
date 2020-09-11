import java.io.FileNotFoundException;
import java.nio.ReadOnlyBufferException;
import java.util.NoSuchElementException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;


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

	public static String ReadFileToString(String filepath, int textlen) throws IOException{
		BinaryInBuffer bib = new BinaryInBuffer(filepath);
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<textlen; i++){
			try{
				char bit = bib.readBoolean() == true ? '1' : '0';
				sb.append(bit);
			}
			catch(ReadOverException e){
				break;
			}
		}
		return sb.toString();
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

	public static boolean WriteStringToBin(String path, String text) throws FileNotFoundException{
		BinaryOutBuffer bb = new BinaryOutBuffer(path);
		for(int i=0; i<text.length(); i++){
			if (text.charAt(i) == '0') {
				bb.writeBit(false);
			} else {
				bb.writeBit(true);
			}
		}
		bb.flush();
		return true;
	}

	private static class BinaryOutBuffer{
		private FileOutputStream fos;
		private BufferedOutputStream bos;
		private int buffer;
		private int n;
		private String fpath;

		public BinaryOutBuffer(String fpath) throws FileNotFoundException{
			fos = new FileOutputStream(fpath);
			bos = new BufferedOutputStream(fos);
			buffer = 0;
			n = 0;
		}

		public void writeBit(boolean bit){
			buffer <<= 1;
			if (bit) buffer |= 1;

			n++;
			if(n==8) clearBuffer();
		}

		// write out any remaining bits in buffer to standard output, padding with 0s
		private void clearBuffer() {

			if (n == 0) return;
			if (n > 0) buffer <<= (8 - n);
			try {
				bos.write(buffer);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			n = 0;
			buffer = 0;
		}
		/**
		 * Flushes standard output, padding 0s if number of bits written so far
		 * is not a multiple of 8.
		 */
		public void flush() {
			clearBuffer();
			try {
				bos.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static class BinaryInBuffer{
		private BufferedInputStream bis;
		private int buffer;
		private int n;

		public BinaryInBuffer(String filepath) throws ReadOverException, IOException{
			bis = new BufferedInputStream(new FileInputStream(filepath));
			buffer = bis.read();
			n = 8;
		}

		public boolean isEmpty() {
			return buffer == -1;
		}

		private void refill(){
			try {
				buffer = bis.read();
				n = 8;
			}
			catch (IOException e) {
				buffer = -1;
				n = -1;
			}
		}

		public boolean readBoolean() {
			if (isEmpty()) throw new ReadOverException("Its really over!");
			n--;
			boolean bit = ((buffer >> n) & 1) == 1;
			if (n == 0) refill();
			return bit;
		}

		public void close() {
			try {
				bis.close();
			}
			catch (IOException ioe) {
				throw new IllegalStateException("Could not close BinaryStdIn", ioe);
			}
		}

	}

	public static class ReadOverException extends RuntimeException{
		public ReadOverException(String error){
			super(error);
		}
	}
}
