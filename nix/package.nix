# SPDX-FileCopyrightText: 2023-2026 The Verma Developer Group
#
# SPDX-License-Identifier: AGPL-3.0-only

{
  lib,
  makeWrapper,
  maven,
  jre,
  self,
  stdenv,
}:
assert stdenv.isLinux;
  maven.buildMavenPackage rec {
    pname = "verma-server";
    version = "0.1.0";
    src = self;

    mvnHash = "sha256-1wpLaNgBFCW1Kni7gYmjbd/UG6NRhlxyWgexm2bkGLg=";

    nativeBuildInputs = [makeWrapper];

    installPhase = ''
      # create the bin directory
      mkdir -p $out/bin $out/share/${pname}

      install -Dm644 target/${pname}-${version}.war $out/share/${pname}

      makeWrapper ${jre}/bin/java $out/bin/${pname} \
        --add-flags "-jar $out/share/verma-server/${pname}-${version}.war"
    '';

    meta = {
      maintainers = with lib.maintainers; [shymega];
      mainProgram = "verma-server";
      platforms = lib.platforms.linux;
    };
  }
