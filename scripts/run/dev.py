import sys

from scripts import utils
from scripts.files import SRC_QUILL_HOME, SRC_QUILL_PACKAGE

def run():
    utils.add_library(SRC_QUILL_HOME)
    utils.add_library(SRC_QUILL_PACKAGE)

    print(sys.path)

    # TODO
