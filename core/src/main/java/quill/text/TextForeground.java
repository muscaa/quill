package quill.text;

import java.awt.Color;

record TextForeground(Color color) implements Text {
	
	@Override
	public void to(TextTransform transform) {
		transform.onForeground(this);
	}
}
