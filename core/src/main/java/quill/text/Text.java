package quill.text;

import java.awt.Color;
import java.util.Objects;

public interface Text {

	public static final Color BLACK = Color.BLACK;
	public static final Color DARK_GRAY = Color.GRAY;
	public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
	public static final Color WHITE = Color.WHITE;
	public static final Color DARK_RED = Color.RED.darker();
	public static final Color RED = Color.RED;
	public static final Color DARK_GREEN = Color.GREEN.darker();
	public static final Color GREEN = Color.GREEN;
	public static final Color DARK_YELLOW = Color.YELLOW.darker();
	public static final Color YELLOW = Color.YELLOW;
	public static final Color DARK_BLUE = Color.BLUE.darker();
	public static final Color BLUE = Color.BLUE;
	public static final Color DARK_MAGENTA = Color.MAGENTA.darker();
	public static final Color MAGENTA = Color.MAGENTA;
	public static final Color DARK_CYAN = Color.CYAN.darker();
	public static final Color CYAN = Color.CYAN;
	
	public static final TextTab.Type TAB_BLANK = TextTab.Type.BLANK;
	public static final TextTab.Type TAB_PIPE = TextTab.Type.PIPE;
	public static final TextTab.Type TAB_BRANCH = TextTab.Type.BRANCH;
	public static final TextTab.Type TAB_LAST = TextTab.Type.LAST;
	
	void to(TextTransform transform);

	public static TextBuilder s(Object o) {
		return new TextBuilder().s(Objects.toString(o));
	}

	public static TextBuilder fg(Color color) {
		return new TextBuilder().fg(color);
	}

	public static TextBuilder bg(Color color) {
		return new TextBuilder().bg(color);
	}
	
	public static TextBuilder tab(TextTab.Type type) {
		return new TextBuilder().tab(type);
	}
}
