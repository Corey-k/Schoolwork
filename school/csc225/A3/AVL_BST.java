public class AVL_BST{
	public static boolean checkAVL(BST b){
		
		if(balanced(b.root) > 0){ //if the height has not been swapped to negative then its false otherwise true.
			return true;
		}else{
			return false;
		}
	}
	
	public static int balanced(BST_Node root){
		if(root==null) return 0;
		int leftTree = balanced(root.left);
		if(leftTree < 0){ //treat height as negative if subtree is unbalanced
			return -1;
		}
		int rightTree = balanced(root.right);
		if(rightTree < 0){ //treat height as negative if subtree is unbalanced
			return -1;
		}
		if(Math.abs(leftTree - rightTree) > 1){ // if the heights are different by more than one, its unbalanced
			return -1; 							// again return a negative if unbalanced
		}
		return 1 + Math.max(leftTree, rightTree); // return the greatest height and increase by 1
	}

	public static BST createBST(int[] a){
		if(a.length == 0) return null;
		BST myBST = new BST(a[0]);
		for(int i = 1; i < a.length; i++){
			BST.insert(a[i]);
//			System.out.println(a[i] + " was inserted"); // testing
		}
//		myBST.printInorder(myBST.root); //for testing
		return null;
	}
	public static void main(String[] args){
		int[] A = {82,85,153,195,124,66,200,193,185,243,73,153,76};
		int[] B = {5, 3, 7 ,1};
		int[] C = {5,1,98,100,-3,-5,55,3,56,50};
		int[] D = {297, 619,279,458,324,122,505,549,83,186,131,71};
		int[] E = {78};
		BST b = createBST(A);
		System.out.println(checkAVL(b));
		b = createBST(B);
		System.out.println(checkAVL(b));
		b = createBST(C);
		System.out.println(checkAVL(b));
		b = createBST(D);
		System.out.println(checkAVL(b));
		b = createBST(E);
		System.out.println(checkAVL(b));
	}
}

class BST_Node{
	public int data;
	public BST_Node left, right;

	public BST_Node(int d){
		this.data = d;
	}

}

class BST{
	private static int size = 0;
	public static BST_Node root;

	public BST(int data){
		root = new BST_Node(data);
	}
	//Inset is O(Logn)
	public static void insert(int data){ 
		if(root == null){
			root = new BST_Node(data);
			return;
		}else{
			BST_Node currNode = root;
			while(currNode != null){
				if(data < currNode.data){ // Left Subtree it goes
					if(currNode.left == null){
						currNode.left = new BST_Node(data);
						size++;
						return;
					}
					currNode = currNode.left;
					continue;

				}else if(data > currNode.data){ // Right subtree it goes
					if(currNode.right == null){
						currNode.right = new BST_Node(data);
						size++;
						return;
					}
					currNode = currNode.right;
					continue;
				}else if(data == currNode.data){ // No duplicates allowed!
					return;
				}


			}
		}
	}

	public static void printInorder(BST_Node node){
		if(node == null) return;
		printInorder(node.left);
		System.out.print(node.data + " ");
		printInorder(node.right);
	}

}