from run import *

QUILL_PACKAGE_SCOPE: str = "system"
QUILL_PACKAGE_NAME: str = "muscaa@quill"

if __name__ == "__main__":
    quill_package_path = HOME / "packages" / QUILL_PACKAGE_SCOPE / QUILL_PACKAGE_NAME
    
    run(quill_package_path, "q")
