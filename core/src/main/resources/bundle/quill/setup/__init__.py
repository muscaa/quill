from __future__ import annotations
from abc import ABC, abstractmethod
from pathlib import Path
from types import ModuleType
import shutil

from core.utils import load_module

from quill.package import Package, PackageInfo
from quill.files import HOME, PACKAGES

class Command(ABC):
    def __init__(self):
        super().__init__()
        self._manager: SetupManager | None = None

    @property
    def manager(self):
        if not self._manager:
            raise AttributeError("The 'manager' field has not been set yet!")
        return self._manager
    
    @abstractmethod
    def can_execute(self) -> bool:
        pass

    @abstractmethod
    def execute(self) -> None:
        pass

_SETUP_MANAGERS: dict[str, SetupManager] = {}

class SetupManager:
    def __init__(self, info: PackageInfo, module: ModuleType):
        self._commands: list[Command] | None = None
        self.info = info
        self.module = module
        self.dir = info.dir
        self.root_dir = HOME
        self.package_dir = PACKAGES / info.tag

    def _begin(self):
        if self._commands is not None:
            raise Exception("Can't run a setup command inside another setup command")
        
        self._commands = []

    def _add(self, command: Command):
        if self._commands is None:
            raise Exception("No setup command running")
        if command._manager:
            raise Exception(f"Command {str(command)} already has a setup wizard")
        command._manager = self

        if not command.can_execute():
            raise Exception(f"Command {str(command)} can't execute")
        
        self._commands.append(command)
        
    def _end(self):
        if self._commands is None:
            raise Exception("No setup command running")
        
        commands = self._commands
        self._commands = None

        for command in commands:
            command.execute()

    def install(self) -> bool:
        try:
            if hasattr(self.module, "install"):
                self._begin()
                self.module.install()
                self._end()
                return True
        except Exception as e:
            print(str(e))
        return False

    def uninstall(self, remove: bool) -> bool:
        try:
            if hasattr(self.module, "uninstall"):
                self._begin()
                self.module.uninstall()
                self._end()
            if remove:
                shutil.rmtree(self.dir, ignore_errors=True)
                # TODO also remove every owned file/dir
            return True
        except Exception as e:
            print(str(e))
        return False
    
    def resolve(self) -> bool:
        try:
            if hasattr(self.module, "resolve"):
                self._begin()
                self.module.resolve()
                self._end()
            return True
        except Exception as e:
            print(str(e))
        return False

    @classmethod
    def get(cls, path: str | Path) -> SetupManager | None:
        return _SETUP_MANAGERS.get(str(Path(path).resolve().absolute()))

    @classmethod
    def load(cls, dir: Path, namespace: str, module_name: str) -> SetupManager | None:
        path = dir / f"{module_name}.py"
        # manager = SetupManager.get(path)
        # if manager:
        #     return manager
        
        info = PackageInfo.from_dir(dir, namespace)
        if not info:
            return None

        module = load_module(dir, module_name, cache=False)
        if not module:
            return None

        manager = SetupManager(info, module)
        _SETUP_MANAGERS[str(Path(path).resolve().absolute())] = manager
        return manager

def wizard_install(dir: Path, namespace: str):
    if not dir.exists() or not dir.is_dir():
        raise Exception("Invalid install directory")

    installer = SetupManager.load(dir, namespace, "wizard")
    if not installer:
        raise Exception(f"Directory '{dir}' is not an installable package")
    
    print(f"Installing '{installer.info.tag}'...")
    package = Package.find(installer.info.tag)
    if package:
        uninstaller = SetupManager.load(package.get_path(), package.namespace, "wizard")
        if not uninstaller:
            raise Exception(f"Directory '{package.get_path()}' is not an uninstallable package")
        
        uninstall_result = uninstaller.uninstall(False)
        if not uninstall_result:
            raise Exception(f"Failed to uninstall package '{uninstaller.info.tag}'")

    install_result = installer.install()
    if not install_result:
        raise Exception(f"Failed to install package '{installer.info.tag}'")
    
    package = Package.find(installer.info.tag)
    if package:
        requirements_resolve(package)

    print(f"Done!")

def wizard_uninstall(package: Package, remove: bool = True):
    dir = package.get_path()
    if not dir.exists() or not dir.is_dir():
        raise Exception("Invalid package")

    uninstaller = SetupManager.load(dir, package.namespace, "wizard")
    if not uninstaller:
        raise Exception(f"Directory '{dir}' is not an uninstallable package")
    
    print(f"Uninstalling '{uninstaller.info.tag}'...")
    uninstall_result = uninstaller.uninstall(remove)
    if not uninstall_result:
        raise Exception(f"Failed to uninstall package '{uninstaller.info.tag}'")
    print(f"Done!")

def requirements_resolve(package: Package):
    dir = package.get_path()
    if not dir.exists() or not dir.is_dir():
        raise Exception("Invalid package")
    
    resolver = SetupManager.load(dir, package.namespace, "requirements")
    if not resolver:
        return
    
    resolve_result = resolver.resolve()
    if not resolve_result:
        raise Exception(f"Failed to resolve package '{resolver.info.tag}'")
