package quill.text;

record TextTab(Type type) implements Text {
	
	@Override
	public void to(TextTransform transform) {
		transform.onTab(this);
	}
	
	public static enum Type {
		BLANK("    "),
		PIPE("|   "),
		BRANCH("├── "),
		LAST("└── "),
		;
	    
	    private final String str;
	    
	    private Type(String str) {
	    	this.str = str;
		}
	    
	    @Override
	    public String toString() {
	    	return str;
	    }
	}
}
