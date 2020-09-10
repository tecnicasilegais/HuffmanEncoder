import java.util.Hashtable;

public class App {
	
	public static void main(String[] args) {
		
		//FreqTable table = new FreqTable(3);
		
		Hashtable<Character,Double> table = new Hashtable<Character,Double>();
		
		table.put('a', 45.0);
		table.put('b', 13.0);
		table.put('c', 12.0);
		table.put('d', 16.0);
		table.put('e', 9.0);
		table.put('f', 5.0);
		
		HuffmanEncoding hf = new HuffmanEncoding(table);
		
		hf.printEncoding();
		
	}
	
}
