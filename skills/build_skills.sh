#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Directory to store zip files, defaults to "artifacts" if not provided
ARTIFACTS_DIR="${1:-$(pwd)/../artifacts}"

# Ensure ARTIFACTS_DIR is an absolute path
if [[ ! "$ARTIFACTS_DIR" = /* ]]; then
    ARTIFACTS_DIR="$(pwd)/$ARTIFACTS_DIR"
fi

echo "Building skills to $ARTIFACTS_DIR..."
mkdir -p "$ARTIFACTS_DIR"

find . -path '*/.*' -prune -o -name "SKILL.md" -print | while read -r skill_file; do
    skill_dir=$(dirname "$skill_file")
    rel_dir="${skill_dir#./}"
    dir_name=$(basename "$skill_dir")
    parent_dir=$(basename "$(dirname "$skill_dir")")
    skill_name="skill-${parent_dir}-${dir_name}"

    zip_name="${skill_name}.zip"
    echo "Zipping $rel_dir to $zip_name"

    tmp_stage=$(mktemp -d)
    mkdir -p "$tmp_stage/$rel_dir"
    if command -v rsync >/dev/null 2>&1; then
        rsync -aL "$skill_dir/" "$tmp_stage/$rel_dir/"
    else
        cp -RL "$skill_dir/." "$tmp_stage/$rel_dir/"
    fi

    echo "TEMP: $tmp_stage"

    (cd "$tmp_stage" && zip -q -r "$ARTIFACTS_DIR/$zip_name" "$rel_dir" -x "*.DS_Store")

    rm -rf "$tmp_stage"
done

echo "Skills build complete."
