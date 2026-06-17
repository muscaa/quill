import sys
sys.dont_write_bytecode = True

from pathlib import Path
import shutil
import zipfile
import tempfile

install_dir = Path.home() / ".quill"

def install():
    delete()
    repair()

def add_path():
    print("add_path wip:")
    print(f"You need to manually set QUILL_HOME to {install_dir} and add (QUILL_HOME)/bin to PATH")

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
