/*
 * TelegraphOperator
 *
 * Provided as part of UVic CSC 115, Summer 2015, Assignment #4
 *
 */


import morse.*;


public class TelegraphOperator {
    private MorseTree decoder;
    private String[] encoder;
	
    /*
     * Constructor accepts an already-constructed instance of
     * MorseTree (in "decoder") and two arrays -- one of
     * chars, one of strings, where the same index for each
     * array correspond to each other (i.e., the char
     * at symbol[i] matches the morse-code sequence in
     * morse[i]).
	 * 
	 * Used string array size 256 as recommended by 
	 *	CSC	115:	Fundamentals	of	Programming	II
	 *	Assignment	#4:	Binary	Trees
	 * assinment description PDF
     */
    public TelegraphOperator(MorseTree decoder, char symbols[],
        String morse[])
    {
		int i = 0;
		this.decoder = decoder;
		encoder = new String[256]; 
		for(char C :symbols){
			encoder[(int)C] = morse[i];
			++i;
		}
	}

   
    /*
     * PURPOSE:
     *
     * Using the MorseTree instance provided when to the constructor,
     * convert the sequence of dots, dashes and spaces in codedMessage
     * nto a human-readable version of the message. A single space
     * separates morse-code sequences; two spaces indicate
     * not only the end of a morse-code sequence but also
     * the end of a word. For example:
     *
     *    "... --- ..." is "sos"
     *
     * but:
     *
     *    "... ---  ..." is "so s" (notice space after the 'o')
     * 
     * The codedMessage string can be tokenized into an array
     * of strings using the instructions provided within the PDF
     * description for Assignment #4.
     *
     * EXAMPLES:
     *
     *   If codedMessage = "...-- .-.-.- .---- ....-" then
     *   the string returned is "3.14".
     *
     *   If codedMessage = "..- ...- .. -.-.  .-. ..- .-.. --.. ..--.."
     *   then the string returned is "uvic rulz?"
     *
     */
    public String receiveMessage(String codedMessage) {
		StringBuilder sb = new StringBuilder();
		String[] tokens = codedMessage.split("\\s");
		for(String st : tokens){
			if(st.equals("")){
				sb.append(" ");
			}else{
				if(decoder.lookupSymbol(st) == null){
					return "<message garbled>";
				}
					
				sb.append(decoder.lookupSymbol(st));
			}

		}
//System.out.println(sb.toString()); //Shows whats being returned
        return sb.toString();
    }
   

    /* 
     * PURPOSE:
     *
     * Using the HashMap instance created in the constructor,
     * convert the sequence of characters and spaces into a
     * a morse code message. A single space separate the morse code
     * for each character in message, and two spaces separate
     * words in the morse-code sequence. For example:
     *
     *    "sos" is "... --- ..." 
     *
     * but:
     *
     *    "so s" is "... ---  ..." (notice two spaces after ---)
     *
     * EXAMPLES:
     *   If message is "3.141", then the string returned is
     *   "...-- .-.-.- .---- ....-"
     *
     *   If message is "uvic rulz?" then the string returned is
     *   "..- ...- .. -.-.  .-. ..- .-.. --.. ..--.."
     */
    public String sendMessage(String message) {
		String toReturn = new String();
		StringBuilder sb = new StringBuilder();
		for( char ch : message.toCharArray()){
			if(ch == ' '){
				sb.append(" ");
			}else{
/* Next line has 1 flaw, it always places a blank space after the morse code
 * But further down you see i just remove the last blank space so that the code
 * string returned does not end with a blank space
 */
				sb.append(encoder[(int)ch] + " "); 
			}
		}
		sb.setLength(sb.length() - 1); //removes blank space at end of string
		return sb.toString();
    }

 
}