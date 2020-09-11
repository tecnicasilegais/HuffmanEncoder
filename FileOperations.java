import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;


/**
 * Class responsible for general File operations.
 */
public class FileOperations {

    /**
     * Read all lines from a file as a String.
     *
     * @param filePath the path to the file
     * @return the whole file content as a single String
     * @throws IOException if an I/O error occurs while trying to open the file
     */
    public static String ReadFileToString(Path filePath) throws IOException {
        return Files.lines(filePath, StandardCharsets.US_ASCII).collect(Collectors.joining("\n"));
    }

    public static String ReadFileToString(String filepath, int textlen) throws IOException {
        BinaryInBuffer bib = new BinaryInBuffer(filepath);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textlen; i++) {
            try {
                char bit = bib.readBoolean() ? '1' : '0';
                sb.append(bit);
            } catch (ReadOverException e) {
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
     */
    public static boolean WriteStringToFile(Path filePath, String text) {
        try {
            Files.write(filePath, text.getBytes());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean WriteStringToBin(String path, String text) throws FileNotFoundException {
        BinaryOutBuffer bb = new BinaryOutBuffer(path);
        for (int i = 0; i < text.length(); i++) {
            bb.writeBit(text.charAt(i) != '0');
        }
        bb.flush();
        return true;
    }

    private static class BinaryOutBuffer {
        private final BufferedOutputStream bos;
        private int buffer;
        private int n;

        public BinaryOutBuffer(String fpath) throws FileNotFoundException {
            bos = new BufferedOutputStream(new FileOutputStream(fpath));
            buffer = 0;
            n = 0;
        }

        public void writeBit(boolean bit) {
            buffer <<= 1;
            if (bit) buffer |= 1;

            n++;
            if (n == 8) clearBuffer();
        }

        // write out any remaining bits in buffer to standard output, padding with 0s
        private void clearBuffer() {

            if (n == 0) return;
            if (n > 0) buffer <<= (8 - n);
            try {
                bos.write(buffer);
            } catch (IOException e) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class BinaryInBuffer {
        private final BufferedInputStream bis;
        private int buffer;
        private int n;

        public BinaryInBuffer(String filepath) throws ReadOverException, IOException {
            bis = new BufferedInputStream(new FileInputStream(filepath));
            buffer = bis.read();
            n = 8;
        }

        public boolean isEmpty() {
            return buffer == -1;
        }

        private void refill() {
            try {
                buffer = bis.read();
                n = 8;
            } catch (IOException e) {
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

    }

    public static class ReadOverException extends RuntimeException {
        public ReadOverException(String error) {
            super(error);
        }
    }
}
