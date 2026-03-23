package quill;

import fluff.functions.gen.obj.Func1;
import quill.bootstrap.QuillBootstrap;

public class QEnv {

	public static final int QPID = QuillBootstrap.ENV_QPID;
	public static final boolean QPOST = QuillBootstrap.ENV_QPOST;
	public static final boolean POST_QUILL_UPDATE = env("POST_QUILL_UPDATE", Boolean::parseBoolean, false);

	private static <V> V env(String name, Func1<V, String> func, V defaultValue) {
		try {
			String envValue = System.getenv(name);
			V value = func.invoke(envValue);
			if (value != null) {
				return value;
			}
		} catch (Exception e) {
		}
		return defaultValue;
	}
}
