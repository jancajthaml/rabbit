package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
	
/**
 * Utility class for working with Text
 * 
 * @author Jan Cajthaml
 *
 */
public class TextUtils
{
	
	// >-------[attrs]---------------------------------------------------------------------------------------< //
	
	private static final int[] ASCII_CHAR_TABLE = { 0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,128,0,0,0,128,128,0,64,64,128,128,0,128,0,128,49,49,49,49,49,49,49,49,49,49,128,0,128,128,128,128,0,58,58,58,58,58,58,42,42,42,42,42,42,42,42,42,42,42,42,42,42,42,42,42,42,42,42,64,0,64,128,0,0,50,50,50,50,50,50,34,34,34,34,34,34,34,34,34,34,34,34,34,34,34,34,34,34,34,34,64,128,64,128,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 };

	// >-------[ctor]---------------------------------------------------------------------------------------< //
	
	private TextUtils()
	{}

	// >-------[methods]---------------------------------------------------------------------------------------< //
	
	/**
	 * Checks if Character is Bracket in ASCII
	 * 
	 * @param ch target
	 * @return Character is Bracket
	 */
	public static boolean isBracket(char ch)
	{ return ch<='}' && (ASCII_CHAR_TABLE[ch]&64)>0; }
	
	/**
	 * Checks if Character is Digit in ASCII
	 * 
	 * @param ch target
	 * @return Character is Digit
	 */
	public static boolean isDigit(char ch)
	{ return ch>='0' && ch<='9'; }
	
	/**
	 * Checks if Character is Hexadecimal Number in ASCII
	 * 
	 * @param ch target
	 * @return Character is Hexadecimal Number
	 */
	public static boolean isHexCharacter(char ch)
	{ return (ch<='f') && (ASCII_CHAR_TABLE[ch]&64)>0; }
	
	/**
	 * Checks if Character is Java Operator
	 * 
	 * @param ch target
	 * @return true if Character is Java Operator
	 */
	public static boolean isJavaOperator(char ch)
	{ return (ch<='~') && (ASCII_CHAR_TABLE[ch]&128)>0; }
	
	/**
	 * Checks if Character is Letter in ASCII
	 * 
	 * @param ch target
	 * @return Character is Letter
	 */
	public static boolean isLetter(char ch)
	{ return (ch<='z') && (ASCII_CHAR_TABLE[ch]&2)>0; }
	
	/**
	 * Checks if Character is Letter or Digit in ASCII
	 * 
	 * @param ch target
	 * @return Character is Letter or Digit
	 */
	public static boolean isLetterOrDigit(char ch)
	{ return (ch<='z') && (ASCII_CHAR_TABLE[ch]&32)>0; }
	
	/**
	 * Converts Character to lower case
	 * 
	 * @param ch target
	 * @return Character to LowerCase
	 */
	public static char toLowerCase(char ch)
	{ return (ch>='A' && ch<='Z')?(char)(ch | 0x20):ch; }
	
	/**
	 * Checks if Character is Whitespace
	 * 
	 * @param ch target
	 * @return Character is Whitespace
	 */
	public static boolean isWhitespace(char ch)
	{ return ch==' ' || ch=='\t'; }
	
	/**
	 * Fixes input indent 
	 * 
	 * @param text input
	 * @return Leading Whitespace
	 */
	public static String leadingWhitespace(String text)
	{
		int count = 0;
		while (count<text.length() && isWhitespace(text.charAt(count))) count++;
		return text.substring(0, count);
	}

	/**
	 * Converts ASCII text to HTML
	 * 
	 * @param text
	 * @param replacement
	 * @param pre
	 * @return HTML String
	 */
	public static String AsciiToHtml(String text, String replacement, boolean pre)
	{
		if (text==null)			return null;
		if (replacement==null)	replacement = "";
		
		final String tabString	= "   ";
		boolean space			= false;
		StringBuffer buffer		= new StringBuffer();

		for (int i=0; i<text.length(); i++)
		{
			char ch = text.charAt(i);
			switch (ch)
			{
				case ' ':	buffer.append((pre || !space)?' ':"&nbsp;");	space = true;	break;
				case '\n':	buffer.append(replacement);						space = false;	break;
				case '&':	buffer.append("&amp;");							space = false;	break;
				case '\t':	buffer.append(tabString);						space = false;	break;
				case '<':	buffer.append("&lt;");							space = false;	break;
				case '>':	buffer.append("&gt;");							space = false;	break;
				default:	buffer.append(ch);								space = false;	break;
			}
		}
		return buffer.toString();
	}

	/**
	 * Converts Windcard to Pattern
	 * 
	 * @param wildcard source string
	 * @param caseSensitive is case sensitive
	 * @param start is start
	 * 
	 * @return Pattern
	 */
	public static Pattern wildcardToPattern(String wildcard, boolean caseSensitive, boolean start)
	{
		int flags			= (!caseSensitive)?(Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE):0;
		StringBuffer buffer	= new StringBuffer();
		
		for (int i=0; i<wildcard.length(); i++)
		{
			char ch = wildcard.charAt(i);
			switch (ch)
			{
				case '*'	: buffer.append(".*");						break;
				case '?'	: buffer.append('.');						break;
				case '^'	: buffer.append((i>0 || start)?'\\':'^');	break;
				case '\\'	:
				case '.'	:
				case '|'	:
				case '+'	:
				case '-'	:
				case '$'	:
				case '['	:
				case ']'	:
				case '{'	:
				case '}'	:
				case '('	:
				case ')'	: buffer.append('\\').append(ch);			break;
				default		: buffer.append(ch);						break;
			}
		}

		try									{ return Pattern.compile(buffer.toString(), flags);	}
		catch (PatternSyntaxException e)	{ return Pattern.compile(".+");						}
	}

	/**
	 * Replacement text for CharSequence
	 * 
	 * @param m provider mathcer
	 * @param temp source characters sequence
	 * 
	 * @return Replacement text
	 */
	public static String getReplacement(Matcher m, CharSequence temp)
	{
		int pointer			= 0;
		StringBuffer buffer	= new StringBuffer();
		
		while (pointer < temp.length())
		{
			char next = temp.charAt(pointer);
			if (next == '\\')
			{
				next = temp.charAt(++pointer);
				switch (next)
				{
					case 'n'	: next = '\n';	break;
					case 't'	: next = '\t';	break;
				}
				buffer.append(next);
				pointer++;
			}
			else if (next == '$')
			{
				pointer++;
				
				int num = temp.charAt(pointer) - '0';
				
				if ((num < 0)||(num > 9)) throw new IndexOutOfBoundsException("Unknown group " + temp.charAt(pointer));
				
				pointer++;
				
				boolean done = false;
				
				while (!done)
				{
					if (pointer >= temp.length())		break;
					
					int digit = temp.charAt(pointer) - '0';
					
					if ((digit < 0)||(digit > 9))		break;
					
					int newnum = (num * 10) + digit;
					
					if (m.groupCount() < newnum) done = true;
					else
					{
						num = newnum;
						pointer++;
					}
				}
				if (m.group(num) != null) buffer.append(m.group(num));
			}
			else
			{
				buffer.append(next);
				pointer++;
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Gets default encoding
	 * 
	 * @return Default Encoding
	 */
	public static String getDefaultEncoding()
	{
		String encoding = System.getProperty("file.encoding");
		if (encoding==null)
		{
			try
			{
				File file			= File.createTempFile("temp", null);
				FileWriter writer	= new FileWriter(file);
				encoding			= writer.getEncoding();
				writer.close();
				file.deleteOnExit();
			}
			catch (IOException e) { encoding = "US-ASCII"; }
		}
		return encoding;
	}
	
	/**
	 * Checks whenever string starts with ignore case
	 * 
	 * @param str source string
	 * @param start start string 
	 * 
	 * @return true if String starts with Ignore Case
	 */
	public static boolean startsWithIgnoreCase(String str, String start)
	{
		int startLen = start.length();
		if (str.length()>=startLen)
		{
			for (int i=0; i<startLen; i++)
			{
				char c1 = str.charAt(i);
				char c2 = start.charAt(i);
				if (Character.toLowerCase(c1)!=Character.toLowerCase(c2)) return false;
			}
			return true;
		}
		return false;
	}
	
}