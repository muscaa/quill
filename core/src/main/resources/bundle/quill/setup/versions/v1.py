from __future__ import annotations
from pathlib import Path

from quill.files import PACKAGES, TEMP
from quill.setup import SetupWizard
from quill.setup.commands import owns, copy, delete

def _resolve(root_dir: Path, parent_dir: Path, path: str | Path) -> Path:
    path_str = str(path)
    if path_str.startswith("@"):
        return root_dir / path_str.lstrip("@/").lstrip("\\")
    
    _path = Path(path)
    if _path.is_absolute():
        return _path
    
    return parent_dir / _path

class SetupV1:
    def __init__(self, wizard: SetupWizard):
        self.wizard = wizard

    def owns(self, paths: list[str | Path]):
        _paths = list(map(lambda path: _resolve(self.wizard.root_dir, self.wizard.package_dir, path), paths))

        self.wizard._add(owns.Owns(_paths))

    def copy(self, src: str | Path, dest: str | Path | None = None):
        _src = _resolve(self.wizard.root_dir, self.wizard.dir, src)
        _dest = _resolve(self.wizard.root_dir, self.wizard.package_dir, src if dest is None else dest)

        self.wizard._add(copy.Copy(_src, _dest))
    
    def delete(self, path: str | Path):
        _path = _resolve(self.wizard.root_dir, self.wizard.package_dir, path)

        self.wizard._add(delete.Delete(_path))

    def replace(self, src: str | Path, dest: str | Path | None = None):
        _src = _resolve(self.wizard.root_dir, self.wizard.dir, src)
        _dest = _resolve(self.wizard.root_dir, self.wizard.package_dir, src if dest is None else dest)

        self.wizard._add(delete.Delete(_dest))
        self.wizard._add(copy.Copy(_src, _dest))
