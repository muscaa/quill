import jpype as j

from quill.package import Package
from quill.globals import PACKAGE_QUILL

BOOTSTRAP4J = PACKAGE_QUILL.get_path() / "bootstrap4j"
BOOTSTRAP4J_BOOTSTRAP_JAR = BOOTSTRAP4J / "bootstrap.jar"

def run(package: Package, main_class: str, args: list[str]):
    j.startJVM(classpath=[BOOTSTRAP4J_BOOTSTRAP_JAR])

    from .impl.pyquill import PyQuillImpl
    
    QuillBootstrap = j.JClass("quill.bootstrap.QuillBootstrap")
    QuillBootstrap.PY = PyQuillImpl()

    jargs = j.JArray(j.JString)([str(package.get_path()), main_class, *args]) # type: ignore
    QuillBootstrap.main(jargs)

    j.shutdownJVM()
