package org.net.perorin.groovian;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.JAXB;

public class Window {

	private Setting setting;
	private JFrame frame;
	private JSplitPane splitVertical;
	private JSplitPane splitHorizontal;
	private JPanel logArea;
	private JPanel treeAndBtnArea;
	private JPanel htmlArea;
	private JPanel treeArea;
	private JPanel btnArea;
	private FileTree fileTree;
	private JEditorPane editorPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Window() {
		initialize();
	}

	private void initialize() {

		// UIをWindows風に
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		setting = JAXB.unmarshal(new File("./setting/setting.xml"), Setting.class);

		frameInit();
		splitVerticalInit();
		splitHorizontalInit();
		htmlAreaInit();
		logAreaInit();
		treeAndBtnAreaInit();
		treeAreaInit();
		btnAreaInit();
		layoutInit();
		settingInit();
	}

	private void frameInit() {
		frame = new JFrame("I am The Groovian!!");
		frame.setForeground(Color.black);
		frame.setBackground(Color.lightGray);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(new ImageIcon("./icon/app_icon.png").getImage());
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				frameClosing();
			}
		});
	}

	private void splitVerticalInit() {
		splitVertical = new JSplitPane();
		splitVertical.setOrientation(JSplitPane.VERTICAL_SPLIT);
	}

	private void splitHorizontalInit() {
		splitHorizontal = new JSplitPane();
	}

	private void htmlAreaInit() {
		htmlArea = new JPanel();
		htmlArea.setLayout(new BorderLayout(0, 0));
		editorPane = new JEditorPane();
		editorPane.setContentType("text/html");
		editorPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(editorPane);
		htmlArea.add(scrollPane, BorderLayout.CENTER);
	}

	private void logAreaInit() {
		logArea = new JPanel();
		logArea.setLayout(new BorderLayout(0, 0));
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		logArea.add(scrollPane, BorderLayout.CENTER);
		JTextAreaStream stream = new JTextAreaStream(textArea);
		System.setOut(new PrintStream(stream, true));
	}

	private void treeAndBtnAreaInit() {
		treeAndBtnArea = new JPanel();
		treeAndBtnArea.setLayout(new BorderLayout(0, 0));
	}

	private void treeAreaInit() {
		treeArea = new JPanel();
		treeArea.setLayout(new BorderLayout(0, 0));
		fileTree = new FileTree(new File(setting.getWorkspacePath())) {
			@Override
			public void keyPress() {
				exec();
			}
		};
		treeArea.add(fileTree);
	}

	private void btnAreaInit() {
		btnArea = new JPanel();
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exec();
			}
		});
		btnArea.add(btnRun);
		JButton btnConsole = new JButton("Console");
		btnConsole.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				console();
			}
		});
		btnArea.add(btnConsole);
	}

	private void layoutInit() {
		frame.getContentPane().add(splitVertical, BorderLayout.CENTER);
		splitVertical.setLeftComponent(htmlArea);
		splitVertical.setRightComponent(splitHorizontal);
		splitHorizontal.setLeftComponent(logArea);
		splitHorizontal.setRightComponent(treeAndBtnArea);
		splitHorizontal.setDividerLocation(400);
		treeAndBtnArea.add(treeArea, BorderLayout.CENTER);
		treeAndBtnArea.add(btnArea, BorderLayout.SOUTH);
	}

	private void settingInit() {
		frame.setBounds(setting.getFrameX(), setting.getFrameY(), setting.getFrameWidth(), setting.getFrameHeight());
		splitVertical.setDividerLocation(setting.getSplitVertivalValue());
		splitHorizontal.setDividerLocation(setting.getSplitHorizontalValue());
	}

	private void frameClosing() {
		int frameX = frame.getX();
		int frameY = frame.getY();
		int frameW = frame.getWidth();
		int frameH = frame.getHeight();
		int splitV = splitVertical.getDividerLocation();
		int splitH = splitHorizontal.getDividerLocation();
		setting.setFrameX(frameX);
		setting.setFrameY(frameY);
		setting.setFrameWidth(frameW);
		setting.setFrameHeight(frameH);
		setting.setSplitVertivalValue(splitV);
		setting.setSplitHorizontalValue(splitH);
		try {
			JAXB.marshal(setting, new FileOutputStream("./setting/setting.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void exec() {
		Util.execGroovy(fileTree.selectedNode);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Util.copyOutputHTML(sdf.format(c.getTime()));
		Util.readHtmlFile(editorPane, "./build/" + sdf.format(c.getTime()) + "/spock-reports/index.html");
	}

	private void console() {
		Util.consoleGroovy(fileTree.selectedNode);
	}

}
