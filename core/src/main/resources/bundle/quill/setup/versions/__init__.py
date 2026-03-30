from pathlib import Path

from quill.setup import SetupWizard
from quill.setup.versions.v1 import SetupV1

def V1(src: str | Path | SetupWizard):
    wizard = src if isinstance(src, SetupWizard) else SetupWizard.get(src)
    if not wizard:
        raise Exception("Setup wizard is null")
    
    return SetupV1(wizard)
