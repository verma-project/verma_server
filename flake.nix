# SPDX-FileCopyrightText: 2023-2025 Dom Rodriguez (shymega) <shymega@shymega.org.uk>
#
# SPDX-License-Identifier: AGPL-3.0-only
{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-24.11";
    mvn2nix.url = "github:fzakaria/mvn2nix";
    systems.url = "github:nix-systems/default";
    devenv = {
      url = "github:cachix/devenv";
      inputs.nixpkgs.follows = "nixpkgs";
    };
    utils = {
      url = "github:numtide/flake-utils";
      inputs.systems.follows = "systems";
    };
  };

  outputs = inputs: let
    inherit (inputs) self nixpkgs utils devenv;
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
  in
    utils.lib.eachDefaultSystem (system: let
      pkgs = genPkgs system;
      inherit (pkgs) verma_server;
    in {
      packages = {
        inherit verma_server;
        oci = pkgs.callPackage ./nix/oci.nix {inherit self verma_server;};
        default = self.packages.${system}.verma_server;
        devenv-up = self.devShells.${system}.default.config.procfileScript;
      };

      devShells.default = devenv.lib.mkShell {
        inherit inputs pkgs;
        modules = pkgs.lib.singleton ./devenv.nix;
      };
    });
}
