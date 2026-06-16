package quill.text;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TextBuilder {
	
	private final List<Text> list = new LinkedList<>();
	
	public <T extends TextTransform> T to(T transform) {
		for (Text t : list) {
			t.to(transform);
		}
		return transform;
	}
	
	public TextBuilder add(Text text) {
		list.add(text);
		return this;
	}
	
	public TextBuilder s(Object o) {
		return add(new TextString(Objects.toString(o)));
	}
	
	public TextBuilder fg(Color color) {
		return add(new TextForeground(color));
	}
	
	public TextBuilder bg(Color color) {
		return add(new TextBackground(color));
	}
	
	public TextBuilder tab(TextTab.Type type) {
		return add(new TextTab(type));
	}
}
