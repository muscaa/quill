import os

import __about__ as a

def run():
    os.environ["QUILL_ID"] = a.ID
    os.environ["QUILL_NAME"] = a.NAME
    os.environ["QUILL_AUTHOR_ID"] = a.AUTHOR_ID
    os.environ["QUILL_AUTHOR_NAME"] = a.AUTHOR_NAME
    os.environ["QUILL_VERSION"] = a.VERSION
    os.environ["QUILL_DESCRIPTION"] = a.DESCRIPTION
