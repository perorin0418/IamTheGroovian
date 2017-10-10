package org.net.perorin.groovian;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelenidePOCreator {

	private SelenidePOCreator() {
	}

	public static void createPageObject(File html, File pageObject) {
		List<String> list = getElementList(html);
		System.out.println(list);
	}

	private static List<String> getElementList(File html) {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			FileReader fileReader = new FileReader(html);
			br = new BufferedReader(fileReader);

			String line;
			while ((line = br.readLine()) != null) {
				List<String> buf = selenideId2GroovyLine(line);
				for (String str : buf) {
					list.add(str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ie) {
				}
			}
		}
		return list;
	}

	private static List<String> selenideId2GroovyLine(String line) {
		int i[] = indexOf(line, "selenideID=");
		List<String> list = new ArrayList<>();
		for (int a : i) {
			String str = getConvertedString(line, a);
			list.add(str);
		}
		return list;
	}

	private static String getConvertedString(String line, int index) {
		char[] cs = line.toCharArray();
		int count = 0;
		int beginAttributeIndex = index;
		int endAttributeIndex = 0;
		int beginSelenideIndex = 0;
		int endSelenideIndex = 0;
		for (endAttributeIndex = beginAttributeIndex; endAttributeIndex < cs.length; endAttributeIndex++) {
			if (cs[endAttributeIndex] == '"') {
				count++;
				if (count >= 2) {
					endSelenideIndex = endAttributeIndex;
					endAttributeIndex++;
					break;
				}
				beginSelenideIndex = endAttributeIndex + 1;
			}
		}
		String selenideID = line.substring(beginSelenideIndex, endSelenideIndex);

		String buf[] = selenideID.split("\\.");
		String eleName = "";
		if (buf.length > 1) {
			eleName = buf[1];
		}

		String ret = "SelenideElement " + eleName + " = $(\"[selenideID=" + selenideID + "]\")";
		return ret;
	}

	private static int[] indexOf(String line, String regex) {
		List<Integer> list = new ArrayList<Integer>();
		int index = 0;
		while (index < line.length()) {
			int i = line.indexOf(regex, index);
			if (i < 0) {
				break;
			} else {
				list.add(i);
				index = i + 1;
			}
		}
		return list.stream().mapToInt(i -> i).toArray();
	}

}
