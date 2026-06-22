import shutil
import json
import subprocess

import __about__ as a
from scripts.files import BUILD_GENERATED

def run():
    shutil.rmtree(BUILD_GENERATED, ignore_errors=True)
    BUILD_GENERATED.mkdir(parents=True, exist_ok=True)

    package_json = BUILD_GENERATED / "package.json"
    package_json.write_text(json.dumps({
        "id": a.ID,
        "author": a.AUTHOR_ID,
        "version": a.VERSION,
        "description": a.DESCRIPTION,
    }, indent=4))

    core = BUILD_GENERATED / "_" / "core"
    core.mkdir(parents=True, exist_ok=True)
    core_version_txt = core / "version.txt"
    core_version_txt.write_text(a.VERSION)

    libs = BUILD_GENERATED / "libs"
    libs.mkdir(parents=True, exist_ok=True)
    libs_pip_txt = libs / "pip.txt"
    subprocess.run(["uv", "export", "--frozen", "--no-dev", "--no-emit-project", "--no-header", "--no-hashes", "-o", libs_pip_txt])
