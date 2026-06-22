from pathlib import Path

from quill.setup import SetupManager
from quill.setup.versions.v1 import SetupV1

def V1(src: str | Path | SetupManager):
    wizard = src if isinstance(src, SetupManager) else SetupManager.get(src)
    if not wizard:
        raise Exception("Setup wizard is null")
    
    return SetupV1(wizard)
