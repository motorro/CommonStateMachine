#!/bin/bash
set -e

# Directory to store zip files, defaults to "artifacts" if not provided
ARTIFACTS_DIR="${1:-$(pwd)/artifacts}"

echo "Building skills to $ARTIFACTS_DIR..."
mkdir -p "$ARTIFACTS_DIR"

# Ensure ARTIFACTS_DIR is an absolute path
if [[ ! "$ARTIFACTS_DIR" = /* ]]; then
    ARTIFACTS_DIR="$(pwd)/$ARTIFACTS_DIR"
fi

find skills -name "SKILL.md" | while read -r skill_file; do
    skill_dir=$(dirname "$skill_file")
    # Get relative path from skills directory
    rel_path=${skill_dir#skills/}
    # Replace slashes with underscores
    zip_name="skill_${rel_path//\//_}.zip"
    echo "Zipping $skill_dir to $zip_name"

    (cd "$skill_dir" && zip -r "$ARTIFACTS_DIR/$zip_name" . -x "*.DS_Store")
done

echo "Skills build complete."
