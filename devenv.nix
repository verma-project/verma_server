# SPDX-FileCopyrightText: 2023-2026 The Verma Developer Group
#
# SPDX-License-Identifier: AGPL-3.0-only

{
  pkgs,
  lib,
  config,
  inputs,
  ...
}: {
  packages = with pkgs; [
    git
  ];

  languages = {
    rust.enable = true;
    shell.enable = true;
    nix.enable = true;
  };

  services.postgres = {
    enable = true;
    package = pkgs.postgresql;
    listen_addresses = "127.0.0.1";
    initialDatabases = [{name = "verma_db";}];
  };

  devcontainer.enable = true;
  difftastic.enable = true;
  dotenv.enable = true;

  git-hooks.hooks = {
    actionlint.enable = true;
    commitizen.enable = true;
    markdownlint.enable = true;
  };
}
