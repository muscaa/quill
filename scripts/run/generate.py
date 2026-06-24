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

    libs = BUILD_GENERATED / "libs"
    libs.mkdir(parents=True, exist_ok=True)
    libs_pip_json = libs / "pip.json"
    
    proc = subprocess.run([
        "uv", "export", "--frozen", "--no-dev", "--no-emit-workspace", "--no-header", "--no-hashes", "--no-annotate"
        ], check=True, capture_output=True, text=True)
    
    pip = {}
    for line in proc.stdout.split("\n"):
        if not line:
            continue

        split = line.split("==")
        if len(split) != 2:
            continue

        name, version = split
        pip[name] = version

    libs_pip_json.write_text(json.dumps(pip, indent=4))
