package com.qiyuesuo.proxy.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class StringUtils {

	public static final String SPACE = " ";
	public static final String EMPTY = "";

	public static String STRINGS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private static Pattern mobilePattern =  Pattern.compile("[\\s-]"); //手机号过滤正则,空格或者连字符-
	
	/**
	 * <p>
	 * Checks if a CharSequence is empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 * <p>
	 * NOTE: This method changed in Lang version 2.0.
	 * It no longer trims the CharSequence.
	 * That functionality is available in isBlank().
	 * </p>
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is empty or null
	 */
	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is not empty ("") and not null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isNotEmpty(null)      = false
	 * StringUtils.isNotEmpty("")        = false
	 * StringUtils.isNotEmpty(" ")       = true
	 * StringUtils.isNotEmpty("bob")     = true
	 * StringUtils.isNotEmpty("  bob  ") = true
	 * </pre>
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is not empty and not null
	 * @since 3.0 Changed signature from isNotEmpty(String) to isNotEmpty(CharSequence)
	 */
	public static boolean isNotEmpty(final CharSequence cs) {
		return !StringUtils.isEmpty(cs);
	}

	/**
	 * <p>
	 * Checks if any one of the CharSequences are empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isAnyEmpty(null)             = true
	 * StringUtils.isAnyEmpty(null, "foo")      = true
	 * StringUtils.isAnyEmpty("", "bar")        = true
	 * StringUtils.isAnyEmpty("bob", "")        = true
	 * StringUtils.isAnyEmpty("  bob  ", null)  = true
	 * StringUtils.isAnyEmpty(" ", "bar")       = false
	 * StringUtils.isAnyEmpty("foo", "bar")     = false
	 * </pre>
	 *
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if any of the CharSequences are empty or null
	 * @since 3.2
	 */
	public static boolean isAnyEmpty(CharSequence... css) {
		if (ArrayUtils.isEmpty(css)) {
			return true;
		}
		for (CharSequence cs : css) {
			if (isEmpty(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if none of the CharSequences are empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isNoneEmpty(null)             = false
	 * StringUtils.isNoneEmpty(null, "foo")      = false
	 * StringUtils.isNoneEmpty("", "bar")        = false
	 * StringUtils.isNoneEmpty("bob", "")        = false
	 * StringUtils.isNoneEmpty("  bob  ", null)  = false
	 * StringUtils.isNoneEmpty(" ", "bar")       = true
	 * StringUtils.isNoneEmpty("foo", "bar")     = true
	 * </pre>
	 *
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if none of the CharSequences are empty or null
	 * @since 3.2
	 */
	public static boolean isNoneEmpty(CharSequence... css) {
		return !isAnyEmpty(css);
	}

	/**
	 * <p>
	 * Checks if a CharSequence is whitespace, empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace
	 * @since 2.0
	 * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is not empty (""), not null and not whitespace only.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is
	 *         not empty and not null and not whitespace
	 * @since 2.0
	 * @since 3.0 Changed signature from isNotBlank(String) to isNotBlank(CharSequence)
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !StringUtils.isBlank(cs);
	}

	/**
	 * 判断是否有空字符，只要有一个为空就返回true。</br>
	 * Checks if any one of the CharSequences are blank ("") or null and not whitespace only..</br>
	 * <pre>
	 * StringUtils.isAnyBlank(null)             = true
	 * StringUtils.isAnyBlank(null, "foo")      = true
	 * StringUtils.isAnyBlank(null, null)       = true
	 * StringUtils.isAnyBlank("", "bar")        = true
	 * StringUtils.isAnyBlank("bob", "")        = true
	 * StringUtils.isAnyBlank("  bob  ", null)  = true
	 * StringUtils.isAnyBlank(" ", "bar")       = true
	 * StringUtils.isAnyBlank("foo", "bar")     = false
	 * </pre>
	 *
	 * @param css the CharSequences to check, may be null or empty
	 * @return 只要有一个为空就返回true，全部不为空时返回false
	 * @since 3.2
	 */
	public static boolean isAnyBlank(CharSequence... css) {
		if (ArrayUtils.isEmpty(css)) {
			return true;
		}
		for (CharSequence cs : css) {
			if (isBlank(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否有空字符，全部都不为空时返回true。</br>
	 * Checks if none of the CharSequences are blank ("") or null and whitespace only..</br>
	 *
	 * <pre>
	 * StringUtils.isNoneBlank(null)             = false
	 * StringUtils.isNoneBlank(null, "foo")      = false
	 * StringUtils.isNoneBlank(null, null)       = false
	 * StringUtils.isNoneBlank("", "bar")        = false
	 * StringUtils.isNoneBlank("bob", "")        = false
	 * StringUtils.isNoneBlank("  bob  ", null)  = false
	 * StringUtils.isNoneBlank(" ", "bar")       = false
	 * StringUtils.isNoneBlank("foo", "bar")     = true
	 * </pre>
	 *
	 * @param css the CharSequences to check, may be null or empty
	 * @return 只要有一个为空就返回false，全部不为空时返回true
	 * @since 3.2
	 */
	public static boolean isNoneBlank(CharSequence... css) {
		return !isAnyBlank(css);
	}
	
	/**
	 * 判断是否全部为空
	 * @param css
	 * @return 全部为空返回true
	 */
	public static boolean isAllBlank(CharSequence... css) {
		if (ArrayUtils.isEmpty(css)) {
			return true;
		}
		for (CharSequence cs : css) {
			if (isNotBlank(cs)) {
				return false;
			}
		}
		return true;
	}


	public static String trim(final String str) {
		return str == null ? null : str.trim();
	}


	public static String trimToNull(final String str) {
		final String ts = trim(str);
		return isEmpty(ts) ? null : ts;
	}


	public static String trimToEmpty(final String str) {
		return str == null ? EMPTY : str.trim();
	}
	
	public static String trimToEmpty(Object o) {
		return o == null ? EMPTY : o.toString();
	}

	/**
	 * 获取随机字符串
	 * @param length 字符串长度
	 * @return 
	 */
	public static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(STRINGS.length());
			sb.append(STRINGS.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 废弃的方法，不要使用，用 {@link StringUtils#randomString(int)}
	 */
	@Deprecated
	public static String random(int length) {
		return randomString(length);
	}
	
	/**
     * Tokenize the given String into a String array via a StringTokenizer.
     * Trims tokens and omits empty tokens.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     * <p/>
     * <p>Copied from the Spring Framework while retaining all license, copyright and author information.
     *
     * @param str        the String to tokenize
     * @param delimiters the delimiter characters, assembled as String
     *                   (each of those characters is individually considered as delimiter).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see java.lang.String#trim()
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     * <p/>
     * <p>Copied from the Spring Framework while retaining all license, copyright and author information.
     *
     * @param str               the String to tokenize
     * @param delimiters        the delimiter characters, assembled as String
     *                          (each of those characters is individually considered as delimiter)
     * @param trimTokens        trim the tokens via String's <code>trim</code>
     * @param ignoreEmptyTokens omit empty tokens from the result array
     *                          (only applies to tokens that are empty after trimming; StringTokenizer
     *                          will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens (<code>null</code> if the input String
     *         was <code>null</code>)
     * @see java.util.StringTokenizer
     * @see java.lang.String#trim()
     */
    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return tokens.toArray(new String[tokens.size()]);
    }

    
	/**
	 * 格式化手机号码，目前只是替换空格跟连字符
	 * @param mobile
	 * @return
	 */
	public static String patternMobile(String mobile) {
		if(mobile == null) {
			return EMPTY;
		}
		return mobilePattern.matcher(mobile).replaceAll(EMPTY);
	}
	
	/**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array  the array of values to join together, may be null
     * @param separator  the separator character to use
     * @return the joined String, <code>null</code> if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }

        return join(array, separator, 0, array.length);
    }

    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array  the array of values to join together, may be null
     * @param separator  the separator character to use
     * @param startIndex the first index to start joining from.  It is
     * an error to pass in an end index past the end of the array
     * @param endIndex the index to stop joining from (exclusive). It is
     * an error to pass in an end index past the end of the array
     * @return the joined String, <code>null</code> if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return EMPTY;
        }

        StringBuilder buf = new StringBuilder();

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
	
    /**
     * 对字符串中间位数（除前两位后两位）用星号遮挡
     * 
     * @param str
     * @return
     */
    public static String coverMessage(String str) {
    	if(str != null && str.length() > 4) {
			String beginStr = str.substring(0,2);
			String endStr = str.substring(str.length() - 2, str.length());
			str = beginStr + stars(str.length()-4) + endStr;
		}
    	return str;
    }
    
    //获取遮挡的星
  	private static String stars(int num) {
  		StringBuffer stars = new StringBuffer();
  		for(int i=0; i<num; i++){
  			stars.append("*");
  		}
  		return stars.toString();
  	}
}
