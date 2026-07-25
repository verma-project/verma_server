# SPDX-FileCopyrightText: 2023-2026 The Verma Developer Group
#
# SPDX-License-Identifier: AGPL-3.0-only

{
  lib,
  pkg-config,
  rustPlatform,
  self,
  stdenv,
}:
assert stdenv.isLinux;
  rustPlatform.buildRustPackage (finalAttrs: {
    name = "verma_server";
    version = "0.1.0";

    src = lib.cleanSource self;

    cargoLock.lockFile = "${finalAttrs.src}/Cargo.lock";

    nativeBuildInputs = [pkg-config];

    meta = {
      description = "";
      homepage = "https://verma-project.github.io";
      license = lib.licenses.agpl3Only;
      maintainers = with lib.maintainers; [shymega];
      mainProgram = "vermas";
      platforms = lib.platforms.linux;
    };
  })
