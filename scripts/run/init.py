import os
from pathlib import Path
import json

from scripts.files import PROJECT_ROOT

def run():
    vscode = PROJECT_ROOT / ".vscode"
    vscode.mkdir(parents=True, exist_ok=True)

    env_quill_home = os.environ.get("QUILL_HOME")
    if not env_quill_home:
        raise Exception("QUILL_HOME environment variable is missing")
    quill_home = Path(env_quill_home)
    
    settings_json = vscode / "settings.json"
    settings_json.write_text(json.dumps({
        "python.analysis.extraPaths": [
            str(quill_home),
            str(quill_home / "packages" / "default" / "muscaa@quill"),
        ],
    }, indent=4))
