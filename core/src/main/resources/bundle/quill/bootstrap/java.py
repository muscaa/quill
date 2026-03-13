import os
import subprocess
from pathlib import Path

from quill.package import Package
from quill.globals import PACKAGE_QUILL

BOOTSTRAP4J = PACKAGE_QUILL.get_path() / "bootstrap4j"
BOOTSTRAP4J_BOOTSTRAP_JAR = BOOTSTRAP4J / "bootstrap.jar"

def run(package: Package, main_class: str, args: list[str]):
    subprocess.run(["java", "-jar", str(BOOTSTRAP4J_BOOTSTRAP_JAR), str(package.get_path()), main_class, *args]) # TODO
    # in Quill.runPackage citim  "package.java.json"  din  "package_absolute_path"  pentru dependinte, versiune etc etc si le incarcam in RuntimeClassLoader in ordine
    # apelam  "package_main_class"  cu  "package_args"
