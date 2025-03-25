from pathlib import Path

from quill import globals

class Package:
    def __init__(self, scope: str, name: str) -> None:
        self.scope = scope
        self.name = name
    
    def get_path(self) -> Path:
        return globals.QUILL_HOME / "packages" / self.scope / self.name
