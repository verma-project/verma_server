# SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
#
# SPDX-License-Identifier: AGPL-3.0-only
(
  import
  (
    let
      lock = builtins.fromJSON (builtins.readFile ./flake.lock);
    in
      fetchTarball {
        url = "https://github.com/edolstra/flake-compat/archive/${lock.nodes.flake-compat.locked.rev}.tar.gz";
        sha256 = lock.nodes.flake-compat.locked.narHash;
      }
  )
  {src = ./.;}
)
.shellNix
