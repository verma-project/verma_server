# SPDX-FileCopyrightText: 2023-2026 The Verma Developer Group
#
# SPDX-License-Identifier: AGPL-3.0-only

{
  inputs = {
    nixpkgs.url = "https://flakehub.com/f/NixOS/nixpkgs/*";
    devenv = {
      url = "github:cachix/devenv";
      inputs.nixpkgs.follows = "nixpkgs";
    };
    systems.url = "github:nix-systems/default-linux";
  };

  nixConfig = {
    substituters = [
      "https://cache.nixos.org/"
      "https://devenv.cachix.org"
    ];
    trusted-public-keys = [
      "cache.nixos.org-1:6NCHdD59X431o0gWypbMrAURkbJ16ZPMQFGspcDShjY="
      "devenv.cachix.org-1:w1cLUi8dv3hnoSPGAuibQv+f9TZLr6cv/Hm9XgU50cw="
    ];
  };

  outputs = inputs: let
    inherit (inputs) self;

    forEachSystem = let
      genPkgs = system: let
        inherit (inputs) nixpkgs;
      in
        import nixpkgs {
          inherit system;
          overlays = [
            (_: prev: {
              verma_server = prev.callPackage ./nix/package.nix {inherit self;};
            })
          ];
        };
      systems = let
        inherit (inputs) systems;
      in
        import systems;
      inherit (inputs.nixpkgs.lib) genAttrs;
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

    devShells = forEachSystem (pkgs: {
      default = let
        inherit (inputs.devenv.lib) mkShell;
      in
        mkShell {
          inherit inputs pkgs;
          modules = [./devenv.nix];
        };
    });
    overlays.default = _: prev: self.packages.${prev.stdenv.hostPlatform.system} or {};
  };
}
