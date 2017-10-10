package org.net.perorin.groovian;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SelenideHtmlCreator {

	private static String FILE_SEPARATOR = "\\";

	private SelenideHtmlCreator() {
	}

	public static void convertSelenideHtmls(File srcDir, File dscDir) {
		directoryCopy(srcDir, dscDir);
		List<File> htmlList = getHtmlList(srcDir);
		for (File file : htmlList) {
			convertHtml(file);
		}
	}

	private static void convertHtml(File html) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			FileReader fileReader = new FileReader(html);
			FileWriter fileWriter = new FileWriter(new File(html.getPath() + "_converting"));
			br = new BufferedReader(fileReader);
			bw = new BufferedWriter(fileWriter);

			String line;
			while ((line = br.readLine()) != null) {
				line = wicketId2SelenideId(line, getPreffix(html.getName()));
				bw.write(line);
				bw.newLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ie) {
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ie) {
				}
			}
		}
		html.delete();
		new File(html.getPath() + "_converting").renameTo(html);
	}

	private static String wicketId2SelenideId(String line, String fileName) {
		int i[] = indexOf(line, "wicket:id=");
		List<String[]> list = new ArrayList<>();
		for (int a : i) {
			String str[] = getConvertedString(line, a, fileName);
			list.add(str);
		}
		for (String[] strs : list) {
			line = line.replaceAll(strs[0], strs[1]);
		}
		return line;
	}

	private static String[] getConvertedString(String line, int index, String fileName) {
		char[] cs = line.toCharArray();
		int count = 0;
		int beginAttributeIndex = index;
		int endAttributeIndex = 0;
		int beginWicketIndex = 0;
		int endWicketIndex = 0;
		for (endAttributeIndex = beginAttributeIndex; endAttributeIndex < cs.length; endAttributeIndex++) {
			if (cs[endAttributeIndex] == '"') {
				count++;
				if (count >= 2) {
					endWicketIndex = endAttributeIndex;
					endAttributeIndex++;
					break;
				}
				beginWicketIndex = endAttributeIndex + 1;
			}
		}
		String attribute = line.substring(beginAttributeIndex, endAttributeIndex);
		String wicketID = line.substring(beginWicketIndex, endWicketIndex);
		String convd = attribute + " selenideID=\"" + fileName + "." + wicketID + "\"";
		String ret[] = { attribute, convd };
		return ret;
	}

	private static String getPreffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(0, point);
		}
		return fileName;
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

	private static List<File> getHtmlList(File dir) {
		List<File> list = findAllFile(dir.getPath());
		List<File> ret = new ArrayList<File>();

		for (File f : list) {
			if (f.getPath().contains(".html")) {
				ret.add(f);
			}
		}

		return ret;
	}

	private static Boolean directoryCopy(File dirFrom, File dirTo) {
		File[] fromFile = dirFrom.listFiles();
		dirTo = new File(dirTo.getPath() + FILE_SEPARATOR + dirFrom.getName());

		dirTo.mkdir();

		if (fromFile != null) {

			for (File f : fromFile) {
				if (f.isFile()) {
					if (!fileCopy(f, dirTo)) {
						return false;
					}
				} else {
					if (!directoryCopy(f, dirTo)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static Boolean fileCopy(File file, File dir) {
		File copyFile = new File(dir.getPath() + FILE_SEPARATOR + file.getName());
		FileChannel channelFrom = null;
		FileChannel channelTo = null;

		try {
			copyFile.createNewFile();
			channelFrom = new FileInputStream(file).getChannel();
			channelTo = new FileOutputStream(copyFile).getChannel();

			channelFrom.transferTo(0, channelFrom.size(), channelTo);

			return true;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (channelFrom != null) {
					channelFrom.close();
				}
				if (channelTo != null) {
					channelTo.close();
				}

				//更新日付もコピー
				copyFile.setLastModified(file.lastModified());
			} catch (IOException e) {
				return false;
			}
		}
	}

	/**
	 * 与えられたディレクトリの下にあるファイルを再帰的に探索する。
	 * @param absolutePath ディレクトリの絶対パス。
	 * @return ファイルの一覧
	 */
	private static List<File> findAllFile(String absolutePath) {

		List<File> files = new ArrayList<>();

		Stack<File> stack = new Stack<>();
		stack.add(new File(absolutePath));
		while (!stack.isEmpty()) {
			File item = stack.pop();
			if (item.isFile())
				files.add(item);

			if (item.isDirectory()) {
				for (File child : item.listFiles())
					stack.push(child);
			}
		}

		return files;
	}

}
