# SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
#
# SPDX-License-Identifier: AGPL-3.0-only
{
  lib,
  makeWrapper,
  maven,
  jre,
  self,
}:
maven.buildMavenPackage rec {
  pname = "verma-server";
  version = "0.1.0";
  src = self;

  mvnHash = "sha256-+kEQRbPBeRVPOv8aSdkWZaMxEia2kHuxyzQoiwSzZp4=";

  nativeBuildInputs = [makeWrapper];

  installPhase = ''
    # create the bin directory
    mkdir -p $out/bin $out/share/${pname}

    install -Dm644 target/${pname}-${version}.war $out/share/${pname}

    makeWrapper ${jre}/bin/java $out/bin/${pname} \
      --add-flags "-jar $out/share/verma-server/${pname}-${version}.war"
  '';
}
