# SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
#
# SPDX-License-Identifier: AGPL-3.0-only
{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixpkgs-unstable";
    mvn2nix.url = "github:fzakaria/mvn2nix";
    devenv = {
      url = "github:cachix/devenv";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs = inputs: let
    inherit (inputs) self nixpkgs devenv;
    forEachSystem = let
      genPkgs = system:
        import nixpkgs {
          inherit system;
          overlays = with inputs; [
            mvn2nix.overlay
            (final: _: rec {
              verma_server = final.callPackage ./nix/package.nix {inherit self;};
            })
          ];
        };
      systems = [
        "x86_64-linux"
        "aarch64-linux"
        "aarch64-darwin"
        "x86_64-darwin"
      ];
      inherit (nixpkgs.lib) genAttrs;
    in
      f: genAttrs systems (system: f (genPkgs system));
  in {
    packages = forEachSystem (pkgs: let
      inherit (pkgs) verma_server;
    in {
      inherit verma_server;
      oci = pkgs.callPackage ./nix/oci.nix {inherit self verma_server;};
      default = self.packages.${pkgs.stdenv.hostPlatform.system}.verma_server;
      devenv-up = self.devShells.${pkgs.stdenv.hostPlatform.system}.default.config.procfileScript;
    });

    devShells.default = forEachSystem (pkgs:
      devenv.lib.mkShell {
        inherit inputs pkgs;
        modules = pkgs.lib.singleton ./devenv.nix;
      });
  };
}
