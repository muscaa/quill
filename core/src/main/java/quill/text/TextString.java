package quill.text;

record TextString(String string) implements Text {
	
	@Override
	public void to(TextTransform transform) {
		transform.onString(this);
	}
}
