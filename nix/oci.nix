# SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
#
# SPDX-License-Identifier: AGPL-3.0-only
{
  lib,
  dockerTools,
  verma_server,
  writeShellScript,
  self,
}:
dockerTools.buildLayeredImage {
  config.Cmd = let
    entrypoint = writeShellScript "entrypoint" ''
      echo Hello.
    '';
  in [entrypoint];
  contents = [verma_server];
  name = "ghcr.io/verma-project/verma_server";
  tag = "latest-nix";
}
