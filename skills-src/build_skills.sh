#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$SCRIPT_DIR"

# Directory to copy the self-contained skills to, defaults to "../skills" (repo root) if not provided
DEST_DIR="${1:-$SCRIPT_DIR/../skills}"

# Ensure DEST_DIR is an absolute path
if [[ ! "$DEST_DIR" = /* ]]; then
    DEST_DIR="$(pwd)/$DEST_DIR"
fi

echo "Copying skills from $SRC_DIR to $DEST_DIR, resolving symlinks..."

echo "Cleaning up..."
rm -rf "$DEST_DIR"
mkdir -p "$DEST_DIR"

cp -RL "$SRC_DIR/." "$DEST_DIR/"
# Remove every dot-folder (e.g. the .shared source-of-truth folders) and the script itself - their
# real content only ever belongs in skills/ by being resolved through a symlink elsewhere.
find "$DEST_DIR" -mindepth 1 -name ".*" -exec rm -rf {} +
rm -f "$DEST_DIR/build_skills.sh"

echo "Skills copied to $DEST_DIR."
exit 0