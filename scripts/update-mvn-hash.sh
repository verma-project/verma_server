#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
#
# SPDX-License-Identifier: AGPL-3.0-only

set -euo pipefail

HASH_FILE="nix/mvnHash.nix"

# Save original and set fake hash
cp "$HASH_FILE" "$HASH_FILE.bak"
echo '"sha256-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="' > "$HASH_FILE"

# Build and capture the correct hash
HASH=$(nix build .#verma_server 2>&1 | grep -oP 'got:\s+\K\S+' || true)

if [[ -n "$HASH" ]]; then
    echo "\"$HASH\"" > "$HASH_FILE"
    rm "$HASH_FILE.bak"
    echo "Updated $HASH_FILE with: $HASH"
else
    mv "$HASH_FILE.bak" "$HASH_FILE"
    echo "Build succeeded or failed unexpectedly - hash unchanged"
fi
