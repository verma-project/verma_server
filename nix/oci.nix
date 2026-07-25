# SPDX-FileCopyrightText: 2023-2026 The Verma Developer Group
#
# SPDX-License-Identifier: AGPL-3.0-only

{
  dockerTools,
  lib,
  pkgs,
  self,
  verma_server,
  writeShellScript,
}:
dockerTools.buildLayeredImage {
  config.Cmd = let
    entrypoint = writeShellScript "entrypoint" ''
      ${lib.getExe verma_server} $@
    '';
  in
    lib.singleton entrypoint;
  name = "ghcr.io/verma-project/verma_server";
  contents = with pkgs; [
    verma_server
    bash
  ];
  tag = "latest";
}
