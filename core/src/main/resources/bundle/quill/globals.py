from quill.repository import Repository
from quill.package import Package

def _init():
    global PACKAGE_QUILL

    PACKAGE_QUILL = Package.from_file(__file__)

_init()
