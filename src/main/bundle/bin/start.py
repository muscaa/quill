import subprocess

from quill.package import Package

def main(package: Package, args: list[str]) -> None:
    print("quill start.py")
    print(package.scope, package.name)
    print(package.get_dir())

    subprocess.run(["java", "-cp", f"{package.get_dir()}/java/main", "quill.Quill"])
