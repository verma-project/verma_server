# SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
#
# SPDX-License-Identifier: AGPL-3.0-only
{
  lib,
  stdenv,
  buildMavenRepositoryFromLockFile,
  makeWrapper,
  maven,
  jdk21_headless,
  self,
}: let
  mavenRepository = buildMavenRepositoryFromLockFile {file = "${self}/nix/mvn2nix-lock.json";};
in
  stdenv.mkDerivation rec {
    pname = "verma-server";
    version = "0.1.0";
    name = "${pname}-${version}";
    src = self;

    nativeBuildInputs = [jdk21_headless maven makeWrapper];
    buildPhase = ''
      echo "Building with maven repository ${mavenRepository}"
      mvn package --offline -Dmaven.repo.local=${mavenRepository}
    '';

    installPhase = ''
      # create the bin directory
      mkdir -p $out/bin

      # create a symbolic link for the lib directory
      ln -s ${mavenRepository} $out/lib

      # copy out the WAR
      # Maven already setup the classpath to use m2 repository layout
      # with the prefix of lib/
      cp target/${name}.war $out/

      # create a wrapper that will automatically set the classpath
      # this should be the paths from the dependency derivation
      makeWrapper ${jdk21_headless}/bin/java $out/bin/${pname} \
            --add-flags "-jar $out/${name}.war"
    '';
  }
