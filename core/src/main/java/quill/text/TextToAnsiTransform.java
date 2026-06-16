package quill.text;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.fusesource.jansi.Ansi;

import fluff.functions.gen.obj.Func1;

public class TextToAnsiTransform implements TextTransform {
	
	private static final Map<Color, Func1<Ansi, Ansi>> FG = new HashMap<>();
	private static final Map<Color, Func1<Ansi, Ansi>> BG = new HashMap<>();
	
	static {
		FG.put(Text.BLACK, (ansi) -> ansi.fg(Ansi.Color.BLACK));
		FG.put(Text.DARK_GRAY, (ansi) -> ansi.fgBright(Ansi.Color.BLACK));
		FG.put(Text.LIGHT_GRAY, (ansi) -> ansi.fg(Ansi.Color.WHITE));
		FG.put(Text.WHITE, (ansi) -> ansi.fgBright(Ansi.Color.WHITE));
		FG.put(Text.DARK_RED, (ansi) -> ansi.fg(Ansi.Color.RED));
		FG.put(Text.RED, (ansi) -> ansi.fgBright(Ansi.Color.RED));
		FG.put(Text.DARK_GREEN, (ansi) -> ansi.fg(Ansi.Color.GREEN));
		FG.put(Text.GREEN, (ansi) -> ansi.fgBright(Ansi.Color.GREEN));
		FG.put(Text.DARK_YELLOW, (ansi) -> ansi.fg(Ansi.Color.YELLOW));
		FG.put(Text.YELLOW, (ansi) -> ansi.fgBright(Ansi.Color.YELLOW));
		FG.put(Text.DARK_BLUE, (ansi) -> ansi.fg(Ansi.Color.BLUE));
		FG.put(Text.BLUE, (ansi) -> ansi.fgBright(Ansi.Color.BLUE));
		FG.put(Text.DARK_MAGENTA, (ansi) -> ansi.fg(Ansi.Color.MAGENTA));
		FG.put(Text.MAGENTA, (ansi) -> ansi.fgBright(Ansi.Color.MAGENTA));
		FG.put(Text.DARK_CYAN, (ansi) -> ansi.fg(Ansi.Color.CYAN));
		FG.put(Text.CYAN, (ansi) -> ansi.fgBright(Ansi.Color.CYAN));
		
		BG.put(Text.BLACK, (ansi) -> ansi.bg(Ansi.Color.BLACK));
		BG.put(Text.DARK_GRAY, (ansi) -> ansi.bgBright(Ansi.Color.BLACK));
		BG.put(Text.LIGHT_GRAY, (ansi) -> ansi.bg(Ansi.Color.WHITE));
		BG.put(Text.WHITE, (ansi) -> ansi.bgBright(Ansi.Color.WHITE));
		BG.put(Text.DARK_RED, (ansi) -> ansi.bg(Ansi.Color.RED));
		BG.put(Text.RED, (ansi) -> ansi.bgBright(Ansi.Color.RED));
		BG.put(Text.DARK_GREEN, (ansi) -> ansi.bg(Ansi.Color.GREEN));
		BG.put(Text.GREEN, (ansi) -> ansi.bgBright(Ansi.Color.GREEN));
		BG.put(Text.DARK_YELLOW, (ansi) -> ansi.bg(Ansi.Color.YELLOW));
		BG.put(Text.YELLOW, (ansi) -> ansi.bgBright(Ansi.Color.YELLOW));
		BG.put(Text.DARK_BLUE, (ansi) -> ansi.bg(Ansi.Color.BLUE));
		BG.put(Text.BLUE, (ansi) -> ansi.bgBright(Ansi.Color.BLUE));
		BG.put(Text.DARK_MAGENTA, (ansi) -> ansi.bg(Ansi.Color.MAGENTA));
		BG.put(Text.MAGENTA, (ansi) -> ansi.bgBright(Ansi.Color.MAGENTA));
		BG.put(Text.DARK_CYAN, (ansi) -> ansi.bg(Ansi.Color.CYAN));
		BG.put(Text.CYAN, (ansi) -> ansi.bgBright(Ansi.Color.CYAN));
	}
	
	public Ansi ansi = Ansi.ansi();
	
	@Override
	public void onString(TextString text) {
		ansi = ansi.a(text.string());
	}
	
	@Override
	public void onForeground(TextForeground text) {
		Func1<Ansi, Ansi> func = FG.get(text.color());
		
		ansi = func != null ? func.invoke(ansi) : ansi.fgRgb(text.color().getRGB());
	}
	
	@Override
	public void onBackground(TextBackground text) {
		Func1<Ansi, Ansi> func = BG.get(text.color());
		
		ansi = func != null ? func.invoke(ansi) : ansi.bgRgb(text.color().getRGB());
	}
}
