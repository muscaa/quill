import os

from quill.globals import *

class Package:
    def __init__(self, scope: str, name: str) -> None:
        self.scope = scope
        self.name = name
    
    def get_dir(self) -> str:
        return os.path.abspath(f"{DOT_QUILL}/packages/{self.scope}/{self.name}/")
