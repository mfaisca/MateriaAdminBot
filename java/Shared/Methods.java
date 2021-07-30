package Shared;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Methods {
	public static final Random RNG = new Random();

	public static final String urlizeDB(String s) {
		return s.substring(0, s.contains("(") ? s.indexOf("(") : s.length()).replace("'", "").trim().replace(" ", "_");
	}
	public static final String urlizeTT(String s) {
		s = s.toLowerCase();
		if(s.equalsIgnoreCase("cecil")) s = "cecil-dark-knight";
		if(s.equalsIgnoreCase("lann&reynn")) s = "lann-reynn";
		return s.substring(0, s.contains("(") ? s.indexOf("(") : s.length()).replace("'", "").trim().replace(" ", "-");
	}

	public static final String enframe(String s) { return "「**" + s + "**」"; }
	
//	public static final LinkedList<String> splitString(String s, int size) {
//		LinkedList<String> split = new LinkedList<String>();
//		int spaceIndex = s.indexOf(" ");
//		if(spaceIndex != -1)
//			while(spaceIndex < s.length()) {
//				int nextLineIndex = s.indexOf("\n", 0);
//				if(nextLineIndex != -1 && nextLineIndex < size) {
//					split.add(s.substring(0, nextLineIndex));
//					split.addAll(splitString(s.substring(nextLineIndex + 1), size));
//					return split;
//				}
//				int nextSpaceIndex = s.indexOf(" ", spaceIndex + 1);
//				if(nextSpaceIndex == -1){
//					if(s.length() < size)
//						split.addFirst(s);
//					else {
//						split.add(s.substring(0, spaceIndex));
//						split.add(s.substring(spaceIndex+1));
//					}
//					return split;
//				}
//				if(nextSpaceIndex < size)
//					spaceIndex = nextSpaceIndex;
//				else {
//					split.add(s.substring(0, spaceIndex));
//					split.addAll(splitString(s.substring(spaceIndex + 1), size));
//					return split;
//				}
//			}
//		split.add(s);
//		return split;
//	}
//
//	public static final String replaceLast(String string, String toReplace, String replacement) {
//		int pos = string.lastIndexOf(toReplace);
//		if (pos > -1)
//			return string.substring(0, pos)
//					+ replacement
//					+ string.substring(pos + toReplace.length(), string.length());
//		else
//			return string;
//	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>(); 
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; 
	}

	public static final int[][] splitRankData(String[] rdd) {
		int[][] ret = new int[10][3];
		int idx = 2; int mult = 1;
		for(int i2 = 0; i2 < rdd.length; i2++) {
			String rd = rdd[i2];
			for(int i = rd.length()-1; i >= 0; i--) {
				char c = rd.charAt(i);
				if(c == '-') {
					ret[i2][idx] = -ret[i2][idx];
					idx--; mult = 1;
					continue;
				}
				if(mult == 1000) {
					idx--; mult = 1;
				}
				ret[i2][idx] += ((c - '0') * mult);
				mult *= 10;
			}
		}
		return ret;
	}
	public static final int[][] splitRankData(Integer[] rdd) {
		int[][] ret = new int[10][3];
		for(int i2 = 0; i2 < rdd.length; i2++) {
			String rd = ""+rdd[i2];
			int idx = 2; int mult = 1;
			for(int i = rd.length()-1; i >= 0; i--) {
				char c = rd.charAt(i);
				if(c == '-') {
					ret[i2][idx] = -ret[i2][idx];
					idx--; mult = 1;
					continue;
				}
				if(mult == 1000) {
					idx--; mult = 1;
				}
				ret[i2][idx] += ((c - '0') * mult);
				mult *= 10;
			}
		}
		return ret;
	}
}