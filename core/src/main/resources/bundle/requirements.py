import sysconfig
import json
from pathlib import Path

from core.utils import add_library

from quill.setup.versions import V1

def _load_pip(dir: Path):
    for file in dir.iterdir():
        if file.is_dir():
            add_library(file)

def resolve():
    v1 = V1(__file__)

    libs_pip_json = v1.resolve("libs/pip.json")
    pip: dict[str, str] = json.loads(libs_pip_json.read_text())

    libs_platform = v1.resolve(f"libs/{sysconfig.get_platform()}")
    v1.downloads([v1.dl_pip(name, version) for name, version in pip.items()], dir=libs_platform)
    v1.run(lambda: _load_pip(libs_platform))
