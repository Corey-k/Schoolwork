/*
* Name: Corey Koelewyn
* ID: V00869081
* Date: Completed 07/08/2017
* Filename: PostFixMachine.java
* Details: CSC115 Assignment 3
* Description: Uses my StackLinked class and Java's built in LinkedList
*			   to execute a PostFix calculator as discussed in lecture
			   and as outlines by the course description.
*/


/*
 * IMPORTANT:
 *
 * Code in this method use the "for-each" loop. Some students
 * may prefer to old-school "for" loop as taught in CSC 110/111,
 * and some students may even want to play around with 
 * listIterator() method of the LinkedList class and use this
 * as part of code for a loop.
 *
 * Official documentation on the LinkedList class can be found
 * at: skin
 */

import java.util.List;
import java.util.LinkedList;


public class PostfixMachine {

	/**
	 * Taken from ExpressionToken.Java from CSC 115, summer 2017
     * List of kinds of ExpressionTokens. Everything below OPERAND
     * represents either a delimiter (i.e., parentheses) or an
     * operator.
     */ 
	 
    public final static int OPERAND = 0;
    public final static int LEFT_PAREN = 1;
    public final static int RIGHT_PAREN = 2;
    public final static int UNKNOWN = 3;
    public final static int ADD = 4;
    public final static int SUBTRACT = 5;
    public final static int MULTIPLY = 6;
    public final static int DIVIDE = 7;
    public final static int MODULO = 8;



    /**
     * PURPOSE:
     *
     *   Given a string corresponding to some arithmetic expression,
     *   return a list of ExpressionToken where each token in the
     *   original string corresponds to one token in the resulting
     *   list.
     *   
     *   This method simply tokenizes the list. It performs
     *   absolutely no conversions from infix to postfix or back. In
     *   fact, syntactically incorrect expressions can also be
     *   tokenized (i.e., some other code needs to determine if
     *   the resulting list of tokens is a valid expression).
     *
     *   There is one severe limitation with this code in that
     *   negative numbers (i.e., -100) cannot be expressed in that
     *   way. To represent -100 we must instead write 0 - 100 (i.e.,
     *   the tokenizer cannot distinguish between unary and binary
     *   uses of the "-" symbol).
     *
     * EXAMPLES:
     *
     *   If expression = "2" then the list returned is:
     *       { (OPERAND, 2) }
     *
     *   If expression = "32 + 413 * 5" then the list returned is:
     *       { (OPERAND, 32), (PLUS, 0), (OPERAND, 413),
     *         (MULTIPLY, 0), (OPERAND, 5) }
     *
     *   If expression = "( ( 3 ( %" then the list returned is:
     *       { (LEFT_PAREN, 0), (LEFT_PAREN, 0), (OPERAND, 3),
     *         (LEFT_PAREN, 0), (MODULO, 0) }
     */
    public static List<ExpressionToken> tokenize(String expression) {
        LinkedList<ExpressionToken> list = new LinkedList<>();

        String symbols[] = {"\\(", "\\)", "\\+", "\\-", "\\*", "\\/"};

        for (String regex : symbols) {
            String replace = " " + regex + " ";
            expression = expression.replaceAll(regex, replace);
        }

        expression = expression.trim();

        String[] tokens = expression.split("\\s+");
        for (String t : tokens) {
            ExpressionToken et = new ExpressionToken(t);
            list.add(et);
        }

        return list;
    }


    /**
     * PURPOSE:
     *   Given a list of ExpressionTokens, evaluate them as
     *   a postfix expression, and return the result. All
     *   operands are integers. If an error occurs during
     *   evaluation (e.g., too few operands; too many operands;
     *   unrecognized operator; a division-by-zero is being 
     *   attempted) then throw a SyntaxErrorException.
     *
     *
     * EXAMPLES:
     *
     *   If list = { (OPERAND, 3), (OPERAND, 5), (PLUS, 0) }
     *   then the value returned is 8.
     *
     *   If list = { (OPERAND, 10), (OPERAND, 2), (DIVIDE, 0) }
     *   then the value returned is 5.
     *
     *   If list = { (OPERAND, 2), (PLUS, 0) } then a
     *   SyntaxErrorException is thrown.
     *
     *   If list = { (OPERAND, 10), (OPERAND, 2), (OPERAND, 0) }
     *   then a SyntaxErrorException is thrown.
     *
     *   If list = { (OPERAND, 10), (OPERAND, 0), (DIVIDE, 0) }
     *   then a SyntaxErrorException is thrown.
     *
     *   If list = { (OPERAND, 10), (OPERAND, 30), (UNKNOWN, 0) }
     *   then a SyntaxErrorException is thrown.
	 *
	 *
	 *
	 * PARAMETERS:
	 *
	 *	Input: List of ExpressionTokens in postfix layout
	 *	Output: integer value of Expression token's after calculated
     */
    public static int eval(List<ExpressionToken> postfixExpression) {
		StackLinked<Integer> tmp = new StackLinked<Integer>();
		int toReturn = 0;
		int pushBack = 0;
		for(ExpressionToken ExpT:postfixExpression){
			if(ExpT.kind == (OPERAND)){
				tmp.push(ExpT.value);
			}else{
				if(tmp.size() < 2){
					throw new SyntaxErrorException("Too many operators");
				}
				int y = tmp.pop();
					//System.out.println(y + "was popped");
				int x = tmp.pop();
					//System.out.println(x + "was popped");
				switch (ExpT.kind) {
							
					case ADD:
						pushBack = x+y;
						break;
					case SUBTRACT:
						pushBack = x-y;
						break;
					case MULTIPLY:
						pushBack = x*y;
						break;
					case DIVIDE:
						if(y == 0){
							throw new SyntaxErrorException(
							"a division-by-zero is being attempted");
						}
						pushBack = x/y;
						break;
					case MODULO:
						pushBack = x%y;
						break;	
					default:
						throw new SyntaxErrorException(
						"Unknown opperator being used");
						
				}
				
				tmp.push(pushBack);
			}
			
		}
		if(tmp.size() > 1){
			throw new SyntaxErrorException("More Opperands then opperators");
		}
		toReturn = tmp.pop();
		//System.out.println(toReturn);
        return toReturn;
    }


    /**
     * PURPOSE:
     *   Given a list of ExpressionTokens, and assume the list
     *   represents an infix expression, return a list that
     *   represents the postfix equivalent. If an error
     *   occurs during evaluation (e.g., missing parenthesis;
     *   unknown operator) then throw a SyntaxErrorException.
     *
     *
     * EXAMPLES:
     *
     *   If list = { (OPERAND, 3), (PLUS, 0), (OPERAND, 5) }
     *   then the list returned is { (OPERAND, 3), (OPERAND, 5),
     *   (PLUS, 0) }
     *
     *   If list = { (OPERAND, 10), (PLUS, 0), (OPERAND, 20), 
     *   (MULTIPLY, 0), (OPERAND, 30) }
     *   then the list returned is { (OPERAND, 10), (OPERAND, 20),
     *   (OPERAND, 30), (MULTIPLY, 0), (PLUS, 0) }
     *
     *   If list = { (LEFT_PAREN, 0), (OPERAND, 10), (PLUS, 0),
     *   (OPERAND, 20), (RIGHT_PAREN, 0), (MULTIPLY, 0), (OPERAND 30) }
     *   then the list returned is { (OPERAND, 10), (OPERAND, 20),
     *   (PLUS, 0), (OPERAND, 30), (MULTIPLY, 0) }
     *
     *   If list = { (OPERAND, 2), (UNKNOWN, 0), (OPERAND, 5) } then
     *   a SyntaxErrorException is thrown.
     *
     *   If list = { (OPERAND, 2), (RIGHT_PAREN, 0) } then a
     *   SyntaxErrorException is thrown.
	 *
	 *
	 *
	 * PARAMETERS:
	 *
	 *	Input: List of ExpressionTokens in infix layout
	 *	Output: List of ExpressionTokens in Postfix layout

     */
    public static List<ExpressionToken> infix2prefix(
        List<ExpressionToken> infixExpression)
    {
        LinkedList<ExpressionToken> expression = new LinkedList<>();
		StackLinked<ExpressionToken> buildStack = new StackLinked<>();
		int leftParenth = 0; //for tracking parenth balance
		for(ExpressionToken expT : infixExpression){
			//System.out.println(expT);
			
				if(expT.kind == OPERAND){
						expression.add(expT);
						//System.out.println(expT + " added to list");
				}else if(expT.kind == LEFT_PAREN){
						leftParenth++;
						buildStack.push(expT);
				}else if(expT.kind == RIGHT_PAREN){
					if(leftParenth < 1){
							throw new SyntaxErrorException(
							"Parenthesises are not balanced");
						}
						while((buildStack.peek().kind != LEFT_PAREN)
							&&(!buildStack.isEmpty())){
							expression.add(buildStack.pop());
						}
						buildStack.pop();
						leftParenth--;
				}else{
					if(buildStack.isEmpty()){
						buildStack.push(expT);
					}else{
						while(
							expT.precedence() <= buildStack.peek().precedence()
							||buildStack.peek().kind == RIGHT_PAREN){
							expression.add(buildStack.pop());
							if(buildStack.isEmpty()) break;
						}
						buildStack.push(expT);
					}
				}
		}
		while(!buildStack.isEmpty()){
				expression.add(buildStack.pop());
		}
		//displayes the expression returned, used for toubleshooting logic.
		//System.out.println(expression);	
		return expression;
	}
}