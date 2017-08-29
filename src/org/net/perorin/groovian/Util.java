package org.net.perorin.groovian;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.xml.bind.JAXB;

public class Util {

	private static String FILE_SEPARATOR = "\\";
	private static Setting setting;

	public static void readHtmlFile(JEditorPane editor, String file_name) {
		editor.setText("");
		HTMLDocument doc = (HTMLDocument) editor.getDocument();
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		editor.setContentType("text/html");

		File f = new File(file_name);
		try {
			URL u = new URL("file:" + f.getParent() + "/");
			doc.setBase(u);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		EditorKit kit = editor.getEditorKit();

		Reader r = null;
		try {
			r = new FileReader(f);
			kit.read(r, doc, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void execGroovy(String gFile) {
		setting = JAXB.unmarshal(new File("./setting/setting.xml"), Setting.class);
		List<String> command = new ArrayList<>();
		command.add(setting.getGroovyExePath());
		command.add(gFile);

		System.out.print("【実行】");
		for (String str : command) {
			System.out.print(str + " ");
		}
		System.out.println("");

		ProcessBuilder pb = new ProcessBuilder(command);

		try {
			Process proc = pb.start();
			InputStream is = proc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void consoleGroovy(String gFile) {
		setting = JAXB.unmarshal(new File("./setting/setting.xml"), Setting.class);
		List<String> command = new ArrayList<>();
		command.add(setting.getGroovyConsolePath());
		command.add(gFile);

		System.out.print("【実行】");
		for (String str : command) {
			System.out.print(str + " ");
		}
		System.out.println("");

		ProcessBuilder pb = new ProcessBuilder(command);

		try {
			Process proc = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void copyOutputHTML(String date) {
		setting = JAXB.unmarshal(new File("./setting/setting.xml"), Setting.class);
		File from = new File(setting.getSpockOutputPath());
		File to = new File("build\\" + date);
		to.mkdir();
		directoryCopy(new File(from.getAbsolutePath()), new File(to.getAbsolutePath()));
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

}
