package quill.text;

public interface TextTransform {
	
	void onString(TextString text);
	
	void onForeground(TextForeground text);
	
	void onBackground(TextBackground text);
}
