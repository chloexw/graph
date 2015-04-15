package cs601.graph;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class TestGraph extends TestCase {
	String edges = "110 -> 112\n" +
			"112 -> 210\n" +
			"110 -> 220\n" +
			"112 -> 245\n" +
			"210 -> 315\n" +
			"245 -> 315\n" +
			"210 -> 326\n" +
			"220 -> 326\n" +
			"245 -> 326\n" +
			"112 -> 336\n" +
			"245 -> 336\n" +
			"245 -> 342\n" +
			"112 -> 345\n" +
			"245 -> 345\n" +
			"210 -> 414\n" +
			"245 -> 414\n" +
			"342 -> 490\n" +
			"498\n";

	public void testNodes() throws IOException {
		testOn(edges +
						"nodes 110 336\n",

				"nodes 110 -> 336 = [110, 112, 245, 336]\n");
	}

	public void testNodesWithCircle() throws IOException {
		testOn(edges +
						"210 -> 110\n" +
						"nodes 112 326\n",

				"nodes 112 -> 326 = [110, 112, 210, 220, 245, 326]\n");
	}

	public void testNodesWithCircle2() throws IOException {
		testOn(edges +
						"210 -> 110\n" +
						"220 -> 245\n" +
						"nodes 112 490\n",

				"nodes 112 -> 490 = [110, 112, 210, 220, 245, 342, 490]\n");
	}

	public void testNodesWithSameNode() throws IOException {
		testOn(edges +
						"210 -> 110\n" +
						"nodes 112 112\n",

				"nodes 112 -> 112 = [112]\n");
	}

	public void testNodesWithNullNode() throws IOException {
		testOn(edges +
						"210 -> 110\n" +
						"nodes 999 112\n",

				"nodes 999 -> 112 = []\n");
	}

	public void testNodesWithUnreachableNodes() throws IOException {
		testOn(edges +
						"210 -> 110\n" +
						"nodes 112 498\n",

				"nodes 112 -> 498 = []\n");
	}

	public void testLen() throws IOException {
		testOn("110 -> 112\n" +
						"112 -> 326\n" +
						"len 110 326\n",

				"len 110 -> 326 = 2\n");
	}

	public void testLenWithUnreachableNodes() throws IOException {
		testOn(edges +
						"len 112 498\n",

				"len 112 -> 498 = -1\n");
	}

	public void testLenWithNullNodes() throws IOException {
		testOn(edges +
						"len 112 999\n",

				"len 112 -> 999 = -1\n");
	}

	public void testLenWithSameNode() throws IOException {
		testOn(edges +
						"len 112 112\n",

				"len 112 -> 112 = 0\n");
	}

	public void testLenWithCircle() throws IOException {
		testOn(edges +
						"210 -> 110\n" +
						"len 210 490\n",

				"len 210 -> 490 = 5\n");
	}

	public void testReach() throws IOException {
		testOn(edges +
						"reach 210\n",

				"reach 210 = [210, 315, 326, 414]\n");
	}

	public void testReachWithNullNode() throws IOException {
		testOn(edges +
						"reach 999\n",

				"reach 999 = []\n");
	}

	public void testReachWithSingleNode() throws  IOException {
		testOn(edges +
						"reach 498\n",

				"reach 498 = [498]\n");
	}

	public void testReachWithCircle() throws  IOException {
		testOn(edges +
						"490 -> 110\n" +
						"reach 112\n",

				"reach 112 = [110, 112, 210, 220, 245, 315, 326, 336, 342, 345, 414, 490]\n");
	}

	public void testRoots() throws IOException {
		testOn(edges +
						"roots\n",

				"roots = [110, 498]\n");
	}

	public void testRootsWithCircle() throws IOException {
		testOn(edges +
						"210 -> 110\n" +
						"roots\n",

				"roots = [498]\n");
	}

	public void testRootsWithNoRoot() throws IOException {
		testOn(edges +
						"210 -> 110\n" +
						"210 -> 498\n" +
						"roots\n",

				"roots = []\n");
	}

	public void testPrint() throws IOException {
		testOn("110 -> 112\n" +
						"112 -> 326\n" +
						"110 -> 212\n" +
						"print\n",

				"Graph:\n" +
						"110 -> 112\n"+
						"110 -> 212\n"+
						"112 -> 326\n");
	}

	private static String exec(Reader in) throws IOException {
		Lexer lex = new Lexer(in);
		Parser parser = new Parser(lex);
		String output = parser.prog();
		return output;
	}

	private static void testOn(String g, String expecting) throws IOException {
		StringReader in = new StringReader(g);
		String found = exec(in);
		assertEquals(expecting, found);
	}
}
