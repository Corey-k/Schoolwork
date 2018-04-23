/*
 * MorseTree.java
 *
 * Prepared for CSC 115, Summer 2017, Assignment #4.
 *
 * This is used as a binary tree representing the decoding of morse
 * "dot / dash" sequences (e.g., "." or "-"). (Morse *encoding* is
 * not done in this class.)
 *
 * The idea is that a sequence of dots and dashes can be use to
 * determine a path from the tree root to some node -- internal
 * or leaf -- at which the symbol corresponding to that dot/dash
 * sequence is stored. Rather than naming the subtrees "left"
 * and "right", there are instead named within a MorseTreeNode as
 * "dot" and "dash".
 *
 * Note: Not every internal node will correspond to the end of some
 * dot/dash path of a legitimate symbol; therefore if an internal
 * node stores *null* as the symbol, then this means there is no
 * morse code sequence from the root to this node that corresponds
 * to a legitimate symbol.
 * 
 * For a further treatment of how a MorseTree can be visualized,
 * please consult the PDF description distributed for assignment #4.
 */

package morse;


public class MorseTree {
    MorseTreeNode root;
	int n = 0; // tracker for which substring of code we follow.
    public MorseTree() {
		//use a null node as root, because we do not store data in root
		root = new MorseTreeNode(null);
    }

    /*
     * PURPOSE:
     *
     * Given a one-character symbol and its corresponding Morse code
     * equivalent, update a binary tree to enable decoding of that
     * equivalent.
     *
     * Unlike operations on a binary search tree where an insert()
     * operation normally results in at most one new node, this
     * insert might result in more than one. In essence, after
     * the insertion is complete, there will be a path from the
     * root to some node (not necessarily a leaf node!) where
     * the combination of dot and dash edges matches the code
     * given as the parameter. The node at the end of the path will
     * be set to contain the symbol.
     *
     * Refer to the A#4 description (PDF) for more details.
     *
     */
    public void insertSymbol(char symbol, String code) {
		MorseTreeNode tmp = root;
		// changes our string of code into a character array, so that we can
		// step through it nicely.
		for(char ch :  code.toCharArray()){
			if(ch == '.'){
				if(tmp.dot == null){
					tmp.dot = new MorseTreeNode(null);
				}
				tmp = tmp.dot;
			}else if(ch == '-'){
				if(tmp.dash == null){
					tmp.dash = new MorseTreeNode(null)	;
				}
				tmp = tmp.dash;
			}
		}
		tmp.symbol = symbol;
        return;
    }


    /*
     * PURPOSE:
     *
     * Given some sequence of dots (".") and dashes ("-"), 
     * traverse the binary tree at root from the start to the end
     * of the sequence. "Traverse" here means follow the "dot"
     * link if the char in the sequence is the "." character, and
     * follow the "dash" link if the char in the sequence is the
     * "-" character. Examine the node at the end of that sequence.
     * There are three possibilities:
     * 
     * (1) The node's symbol is not null. In this case, the
     *     morse-code sequence is meaningful, and we return
     *     the symbol at that node.
     *
     * (2) The node's symbol *is* null. In this case, the 
     *     morse-code sequence is *not* meaningful. We
     *     return null (and the caller of lookupSymbol() must
     *     explicitly check for this possibility.
     * 
     * (3) The sequence is longer than any path in the
     *     tree. As with (2), this means the morse-code
     *     sequence is not meaningful (i.e., garbage?) and
     *     so we return null. Again as with (2) the caller
     *     of lookupSymbol must 
     */
    public Character lookupSymbol(String code) {
		MorseTreeNode tmp = root;
		
	// again use char arrays for ease of stepping though with a for loop
		for( char ch : code.toCharArray()){
			if(ch == '.'){
				if(tmp.dot == null){
					return null;
				}
				tmp = tmp.dot;
			}else if(ch == '-'){
				if(tmp.dash == null){
					return null;
				}
				tmp = tmp.dash;
			}
		}
			
        return tmp.symbol;
    }
}