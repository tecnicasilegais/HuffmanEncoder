import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class HuffmanEncoding
{
	Hashtable<Character, Double> ft;
	BinTree bt;

	public HuffmanEncoding(Hashtable<Character, Double> frequencyTable)
	{
		ft = frequencyTable;
		bt = buildBinTree();
	}

	private BinTree buildBinTree()
	{
		int n = ft.size();

		List<BinTree> Q = new ArrayList<BinTree>();

		for (Character c : ft.keySet())
		{
			Node tempNode = new Node(c, ft.get(c));
			BinTree temp = new BinTree(tempNode);
			Q.add(temp);
		}

		for (int i = 1; i < n; i++)
		{
			Node z = new Node();
			z.setLeftChild(extractMin(Q).getRoot());
			z.setRightChild(extractMin(Q).getRoot());
			z.setFreq(z.getLeftChild().getFreq() + z.getRightChild().getFreq());
			Q.add(new BinTree(z));
		}

		return extractMin(Q);
	}

	private BinTree extractMin(List<BinTree> Q)
	{
		BinTree result = Q.get(0);
		int iRemove = 0;
		for (int i = 1; i < Q.size(); i++)
		{
			if (Q.get(i).getFreq() < result.getFreq())
			{
				result = Q.get(i);
				iRemove = i;
			}
		}

		Q.remove(iRemove);
		return result;
	}

	public void printEncoding()
	{
		printEncodingAux(bt.getRoot(), "");
	}

	private void printEncodingAux(Node n, String code)
	{
		if (n.getLeftChild() == null && n.getRightChild() == null)
		{
			System.out.println(n.character + " - " + code);
			return;
		}

		printEncodingAux(n.getLeftChild(), code + "0");
		printEncodingAux(n.getRightChild(), code + "1");
	}

	private class Node
	{
		private char character;
		private double freqVal;
		private Node leftChild;
		private Node rightChild;

		public Node()
		{
		}

		public Node(char character, double freqVal)
		{
			this.character = character;
			this.freqVal = freqVal;
		}

		public Node(double freqVal, Node leftChild, Node rightChild)
		{
			this.character = character;
			this.freqVal = freqVal;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
		}

		public Node getLeftChild()
		{
			return leftChild;
		}

		public void setLeftChild(Node leftChild)
		{
			this.leftChild = leftChild;
		}

		public Node getRightChild()
		{
			return rightChild;
		}

		public void setRightChild(Node rightChild)
		{
			this.rightChild = rightChild;
		}

		public Double getFreq()
		{
			return freqVal;
		}

		public void setFreq(Double freq)
		{
			freqVal = freq;
		}
	}

	private class BinTree
	{
		private Node root;

		public BinTree(Node root)
		{
			this.root = root;
		}

		public Node getRoot()
		{
			return root;
		}

		public Double getFreq()
		{
			return root.freqVal;
		}

		private String print()
		{
			return printAux(root);
		}

		private String printAux(Node n)
		{
			if (n == null)
			{
				return "";
			}

			return "([" + n.character + "," + n.freqVal + "]," + printAux(n.leftChild) + "," + printAux(n.rightChild) + ")";
		}

		@Override
		public String toString()
		{
			return this.print();
		}
	}
}
