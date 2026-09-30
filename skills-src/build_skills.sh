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
rm -rf "$DEST_DIR"
mkdir -p "$DEST_DIR"

if command -v rsync >/dev/null 2>&1; then
    rsync -aL --exclude=".*" --exclude="build_skills.sh" "$SRC_DIR/" "$DEST_DIR/"
else
    cp -RL "$SRC_DIR/." "$DEST_DIR/"
    find "$DEST_DIR" -mindepth 1 -name ".*" -exec rm -rf {} +
    rm -f "$DEST_DIR/build_skills.sh"
fi

echo "Skills copied to $DEST_DIR."
