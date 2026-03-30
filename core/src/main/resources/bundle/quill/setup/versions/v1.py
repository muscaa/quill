from __future__ import annotations
from pathlib import Path

from quill.files import PACKAGES, TEMP
from quill.setup import SetupWizard
from quill.setup.commands import owns, copy, delete, chmod

def _resolve_prefix(path: str | Path, prefix: str) -> Path | None:
    path_str = str(path)
    if path_str.startswith(prefix):
        return Path(path_str[len(prefix):].lstrip("/").lstrip("\\"))
    return None

def _resolve(root_dir: Path, parent_dir: Path, path: str | Path) -> Path:
    sub_root = _resolve_prefix(path, "@")
    if sub_root:
        return root_dir / sub_root

    _path = Path(path)
    if _path.is_absolute():
        return _path
    
    return parent_dir / _path

class SetupV1:
    def __init__(self, wizard: SetupWizard):
        self.wizard = wizard

    def owns(self, paths: list[str | Path]):
        for path in paths:
            _path = _resolve(self.wizard.root_dir, self.wizard.package_dir, path)

            self.wizard._add(owns.Owns(_path))

    def bins(self, paths: list[str | Path]):
        for path in paths:
            _src = _resolve(self.wizard.root_dir, self.wizard.dir, path)
            _dest = self.wizard.root_dir / "bin" / _src.name

            self.wizard._add(owns.Owns(_dest))
            self.wizard._add(copy.Copy(_src, _dest))
            self.wizard._add(chmod.Chmod(_dest, exec=True))

    def copy(self, src: str | Path, dest: str | Path | None = None, clean: bool = True):
        _src = _resolve(self.wizard.root_dir, self.wizard.dir, src)
        _dest = _resolve(self.wizard.root_dir, self.wizard.package_dir, src if dest is None else dest)

        if clean:
            self.wizard._add(delete.Delete(_dest, False))
        self.wizard._add(copy.Copy(_src, _dest))
    
    def delete(self, path: str | Path):
        _path = _resolve(self.wizard.root_dir, self.wizard.package_dir, path)

        self.wizard._add(delete.Delete(_path, True))
