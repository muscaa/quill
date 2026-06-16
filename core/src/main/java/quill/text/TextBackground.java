package quill.text;

import java.awt.Color;

record TextBackground(Color color) implements Text {
	
	@Override
	public void to(TextTransform transform) {
		transform.onBackground(this);
	}
}
