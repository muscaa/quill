from pathlib import Path
from collections.abc import Callable

from quill.setup import SetupManager
from quill.setup.commands import run, owns, copy, delete, chmod, download

def _resolve_prefix(path: str | Path, prefix: str) -> Path | None:
    path_str = str(path)
    if path_str.startswith(prefix):
        return Path(path_str[len(prefix):].lstrip("/").lstrip("\\"))
    return None

class SetupV1:
    def __init__(self, manager: SetupManager):
        self.manager = manager

    def resolve(self, path: str | Path, dir: Path | None = None) -> Path:
        sub_root = _resolve_prefix(path, "@")
        if sub_root:
            return self.manager.root_dir / sub_root

        _path = Path(path)
        if _path.is_absolute():
            return _path
        
        _parent_dir = dir or self.manager.package_dir
        return _parent_dir / _path
    
    def run(self, func_execute: Callable[[], None], func_can_execute: Callable[[], bool] | None = None):
        self.manager._add(run.Run(func_execute, func_can_execute))

    def owns(self, paths: list[str | Path]):
        for path in paths:
            _path = self.resolve(path)

            self.manager._add(owns.Owns(_path))

    def bins(self, paths: list[str | Path], newline: copy.NewLine = "lf"):
        for path in paths:
            _src = self.resolve(path, dir=self.manager.dir)
            _dest = self.manager.root_dir / "bin" / _src.name

            self.manager._add(owns.Owns(_dest))
            self.manager._add(copy.Copy(_src, _dest, True, newline))
            self.manager._add(chmod.Chmod(_dest, exec=True))

    def copy(self, src: str | Path, dest: str | Path | None = None, clean: bool = True, overwrite: bool = True, newline: copy.NewLine = "auto"):
        _src = self.resolve(src, dir=self.manager.dir)
        _dest = self.resolve(src if dest is None else dest)

        if clean and overwrite:
            self.manager._add(delete.Delete(_dest, False))
        self.manager._add(copy.Copy(_src, _dest, overwrite, newline))
    
    def delete(self, path: str | Path):
        _path = self.resolve(path)

        self.manager._add(delete.Delete(_path, True))

    def dl_pip(self, name: str, version: str):
        return download.ResourcePip(name, version)

    def downloads(self, resources: list[download.Resource], dir: str | Path | None = None):
        _dir = self.resolve("." if dir is None else dir)

        for resource in resources:
            self.manager._add(download.Download(_dir, resource))
