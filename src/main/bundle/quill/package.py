from pathlib import Path

from quill.globals import *

class Package:
    def __init__(self, scope: str, name: str) -> None:
        self.scope = scope
        self.name = name
    
    def get_path(self) -> Path:
        return DOT_QUILL / "packages" / self.scope / self.name
