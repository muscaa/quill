import sys
sys.dont_write_bytecode = True

from pathlib import Path
import shutil
import zipfile
import tempfile
import platform

install_dir = Path.home() / ".quill"

WIN_MANAGED = "QUILL_MANAGED"
UNIX_SH = ".quill.sh"
UNIX_TARGETS = [
    Path.home() / ".profile",
    Path.home() / ".bash_profile",
    Path.home() / ".bashrc",
    Path.home() / ".zshrc",
]
UNIX_BEGIN = "# QUILL BEGIN"
UNIX_END = "# QUILL END\n"

def env(name: str):
    return Path(f"%{name}%" if platform.system() == "Windows" else f"${name}")

def install_env(env: dict[str, object], path: list[Path]):
    if platform.system() == "Windows":
        import winreg
        import ctypes

        with winreg.OpenKey(winreg.HKEY_CURRENT_USER, "Environment", 0, winreg.KEY_ALL_ACCESS) as key:
            try:
                managed_query, _ = winreg.QueryValueEx(key, WIN_MANAGED)
            except:
                managed_query = ""
            new_managed: list[str] = [value for value in managed_query.split(";")]

            if env:
                for name, value in env.items():
                    winreg.SetValueEx(key, name, 0, winreg.REG_SZ, str(value))
                    m = f"var:{name}"
                    if m not in new_managed:
                        new_managed.append(m)
            if path:
                path_query, _ = winreg.QueryValueEx(key, "Path")
                new_path: list[str] = [value for value in path_query.split(";")]
                for value in path:
                    if value not in new_path:
                        new_path.append(str(value))
                    m = f"path:{value}"
                    if m not in new_managed:
                        new_managed.append(m)
                winreg.SetValueEx(key, "Path", 0, winreg.REG_EXPAND_SZ, ";".join(new_path))

            winreg.SetValueEx(key, WIN_MANAGED, 0, winreg.REG_SZ, ";".join(new_managed))

        HWND_BROADCAST = 0xFFFF
        WM_SETTINGCHANGE = 0x1A
        SMTO_ABORTIFHANG = 0x0002
        ctypes.windll.user32.SendMessageTimeoutW(
            HWND_BROADCAST, WM_SETTINGCHANGE, 0, "Environment",
            SMTO_ABORTIFHANG, 5000, ctypes.byref(ctypes.c_ulong())
        )
    else:
        sh: list[str] = []
        sh_path = Path.home() / UNIX_SH

        if env:
            for name, value in env.items():
                sh.append(f"export {name}=\"{value}\"")
        if path:
            sh.append(f"export PATH=\"{":".join(str(value) for value in path)}:$PATH\"")

        sh_path.write_text("\n".join(sh) + "\n")

        for target in UNIX_TARGETS:
            if not target.exists():
                continue

            lines = [
                UNIX_BEGIN,
                f"[ -f \"$HOME/{UNIX_SH}\" ] && . \"$HOME/{UNIX_SH}\"",
                UNIX_END,
            ]

            text = target.read_text()

            if UNIX_BEGIN in text and UNIX_END in text:
                start = text.index(UNIX_BEGIN)
                end = text.index(UNIX_END) + len(UNIX_END)
                new_text = text[:start] + "\n".join(lines) + text[end:]
            else:
                sep = "" if text.endswith("\n") else "\n"
                new_text = text + sep + "\n".join(lines)

            target.write_text(new_text)

def uninstall_env():
    if platform.system() == "Windows":
        import winreg
        import ctypes

        with winreg.OpenKey(winreg.HKEY_CURRENT_USER, "Environment", 0, winreg.KEY_ALL_ACCESS) as key:
            try:
                managed_query, _ = winreg.QueryValueEx(key, WIN_MANAGED)
            except:
                managed_query = ""
            managed: list[str] = [value for value in managed_query.split(";")]

            path_query, _ = winreg.QueryValueEx(key, "Path")
            new_path: list[str] = [value for value in path_query.split(";")]

            for m in managed:
                type, value = m.split(":", 1)
                if type == "var":
                    try:
                        winreg.DeleteValue(key, value)
                    except:
                        pass
                elif type == "path":
                    try:
                        new_path.remove(value)
                    except:
                        pass
                
            winreg.SetValueEx(key, "Path", 0, winreg.REG_EXPAND_SZ, ";".join(new_path))
            winreg.DeleteValue(key, WIN_MANAGED)

        HWND_BROADCAST = 0xFFFF
        WM_SETTINGCHANGE = 0x1A
        SMTO_ABORTIFHANG = 0x0002
        ctypes.windll.user32.SendMessageTimeoutW(
            HWND_BROADCAST, WM_SETTINGCHANGE, 0, "Environment",
            SMTO_ABORTIFHANG, 5000, ctypes.byref(ctypes.c_ulong())
        )
    else:
        for target in UNIX_TARGETS:
            if not target.exists():
                continue

            text = target.read_text()

            if UNIX_BEGIN in text and UNIX_END in text:
                start = text.index(UNIX_BEGIN)
                end = text.index(UNIX_END) + len(UNIX_END)
                new_text = text[:start] + text[end:]
                target.write_text(new_text)

def install():
    delete()
    repair()

def add_path():
    print("Setting environment variables...")
    install_env(
        env={
            "QUILL_HOME": install_dir,
        },
        path=[
            env("QUILL_HOME") / "bin",
        ]
    )
    print("Done!")

def repair():
    if install_dir.exists():
        print("Repairing installation...")
    else:
        print("Installing...")

    zip_file = Path(__file__).parent

    with tempfile.TemporaryDirectory() as tmp:
        with zipfile.ZipFile(zip_file) as zf:
            zf.extractall(tmp)
        
        sys.path.insert(0, tmp)
        sys.path.insert(0, str(Path(tmp) / "_"))

        from quill import files as native_files
        native_files.HOME = install_dir
        native_files.PACKAGES = install_dir / "packages"

        from quill.setup import install as native_install
        native_install(Path(tmp), "default")

    print("Done!")
    add_path()

def delete():
    if install_dir.exists():
        print("Removing installation...")
        shutil.rmtree(install_dir, ignore_errors=True)
        uninstall_env()
        print("Done!")

def main():
    print("Preparing...")
    print(f"Install dir: {install_dir}")

    if install_dir.exists():
        while True:
            choice = input("The install directory already exists.\n" \
                           "What to do? (i = clean install, r = repair install, d = delete, q = quit): ")
            if choice not in ["i", "r", "d", "q"]:
                print("Invalid choice\n")
            else:
                print()
                break

        if choice == "i":
            install()
        elif choice == "r":
            repair()
        elif choice == "d":
            delete()
    else:
        install()

if __name__ == "__main__":
    main()
