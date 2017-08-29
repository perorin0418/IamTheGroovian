package org.net.perorin.groovian;

public class Setting {

	private int splitVertivalValue = 0;
	private int splitHorizontalValue = 0;
	private int frameHeight = 0;
	private int frameWidth = 0;
	private int frameX = 0;
	private int frameY = 0;
	private String groovyExePath = "";
	private String groovyConsolePath = "";
	private String workspacePath = "";
	private String spockOutputPath = "";
	private String driverPath = "";

	public Setting() {
	}

	public String getWorkspacePath() {
		return workspacePath;
	}

	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath;
	}

	public int getSplitVertivalValue() {
		return splitVertivalValue;
	}

	public int getSplitHorizontalValue() {
		return splitHorizontalValue;
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public int getFrameX() {
		return frameX;
	}

	public int getFrameY() {
		return frameY;
	}

	public String getGroovyExePath() {
		return groovyExePath;
	}

	public String getGroovyConsolePath() {
		return groovyConsolePath;
	}

	public String getSpockOutputPath() {
		return spockOutputPath;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public void setSplitVertivalValue(int splitVertivalValue) {
		this.splitVertivalValue = splitVertivalValue;
	}

	public void setSplitHorizontalValue(int splitHorizontalValue) {
		this.splitHorizontalValue = splitHorizontalValue;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public void setFrameX(int frameX) {
		this.frameX = frameX;
	}

	public void setFrameY(int frameY) {
		this.frameY = frameY;
	}

	public void setGroovyExePath(String groovyExePath) {
		this.groovyExePath = groovyExePath;
	}

	public void setGroovyConsolePath(String groovyConsolePath) {
		this.groovyConsolePath = groovyConsolePath;
	}

	public void setSpockOutputPath(String spockOutputPath) {
		this.spockOutputPath = spockOutputPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

}
