# SPDX-FileCopyrightText: 2023-2026 The Verma Developer Group
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
      ${lib.getExe verma_server} $@
    '';
  in
    lib.singleton entrypoint;
  name = "ghcr.io/verma-project/verma_server";
  tag = "latest";
}
